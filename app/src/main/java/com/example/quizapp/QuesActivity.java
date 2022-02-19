package com.example.quizapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class QuesActivity extends AppCompatActivity {

    private String numOfQues, difficulty, category;
    private String URL = "";
    private TextView progressTxt;
    private static TextView  scoreTxt;
    private int quesIdx = 1;
    private static int score = 0;
    private ProgressBar progressBar;
    private JSONArray questions;
    private static AppCompatButton btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ques);
        score = 0;
        Intent intent = getIntent();
        numOfQues = intent.getStringExtra("NUM_OF_QUES");
        difficulty = intent.getStringExtra("DIFFICULTY");
        category = intent.getStringExtra("CATEGORY");
        progressTxt = findViewById(R.id.progressTxt);
        scoreTxt = findViewById(R.id.scoreTV);
        progressBar = findViewById(R.id.progressBar);
        progressTxt.setText("Ques : " + quesIdx + "/" + numOfQues);
        scoreTxt.setText("Score : " + score);
        btn = findViewById(R.id.nextBtn);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressBar.setProgress((100 * quesIdx)/Integer.parseInt(numOfQues), true);
        }
        getURL();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    questions = response.getJSONArray("results");
                    showQuestion(questions.get(quesIdx-1).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(QuesActivity.this, "Could Not Load Question. Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
        btn.setEnabled(false);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quesIdx - 1 < Integer.parseInt(numOfQues)) {
                    try {
                        showQuestion(questions.get(quesIdx - 1).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    new AlertDialog.Builder(QuesActivity.this)
                            .setTitle("Quiz Completed")
                            .setMessage("Your score is " + score)
                            .setPositiveButton("FINISH", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    score = 0;
                                    startActivity(new Intent(QuesActivity.this, MainActivity.class));
                                    finish();
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            }
        });
    }
    private void getURL(){
        URL += "https://opentdb.com/api.php?";
        URL += "amount="+numOfQues;
        if(MainActivity.getCategoryMap().containsKey(category)){
            URL += "&category="+MainActivity.getCategoryMap().get(category);
        }
        if(!difficulty.equals("Any Difficulty")){
            URL += "&difficulty=" + (char)('a' + difficulty.charAt(0) - 'A') + difficulty.substring(1);
        }
        URL += "&encode=url3986";
    }

    private void showQuestion(String ques){
        btn.setEnabled(false);
        if(quesIdx == Integer.parseInt(numOfQues)){
            btn.setText("Finish Quiz");
        }
        progressTxt.setText("Ques : " + quesIdx + "/" + numOfQues);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressBar.setProgress((100 * quesIdx)/Integer.parseInt(numOfQues), true);
        }

        QuesFragment quesFragment = new QuesFragment();
        Bundle args = new Bundle();
        args.putString("question", ques);
        quesFragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.questionContainer, quesFragment);
        fragmentTransaction.commit();
        quesIdx++;
    }
    public static void setScore(int sc){
        score = sc;
    }
    public static int getScore(){
        return score;
    }
    public static TextView getScoreTxt(){
        return scoreTxt;
    }
    public static AppCompatButton getBtn(){
        return btn;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(QuesActivity.this)
                .setTitle("Quit Quiz")
                .setMessage("Do you want to exit the quiz?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        score = 0;
                        startActivity(new Intent(QuesActivity.this, MainActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("NO", null)
                .show();

    }
}