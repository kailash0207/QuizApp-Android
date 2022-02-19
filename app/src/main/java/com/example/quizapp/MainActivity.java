package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView numOfQuesDD, difficultyDD, categoryDD;
    private Button startBtn;
    private String[] selections = new String[]{"10", "Any Category", "Any Difficulty"};
    private static HashMap<String, String> categoryMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numOfQuesDD = findViewById(R.id.numOfQuesDD);
        categoryDD = findViewById(R.id.categoryDD);
        difficultyDD = findViewById(R.id.difficultyDD);


        makeDropDown(numOfQuesDD, R.array.num_of_questions_options, 0);
        makeDropDown(categoryDD, R.array.category_options, 1);
        makeDropDown(difficultyDD, R.array.difficulty_options, 2);


        startBtn = findViewById(R.id.startBtn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(MainActivity.this, QuesActivity.class);
                intent.putExtra("NUM_OF_QUES", selections[0]);
                intent.putExtra("CATEGORY", selections[1]);
                intent.putExtra("DIFFICULTY", selections[2]);

                startActivity(intent);
                finish();
            }
        });


        categoryMap.put("General Knowledge", "9");
        categoryMap.put("Books", "10");
        categoryMap.put("Film", "11");
        categoryMap.put("Music", "12");
        categoryMap.put("Television", "12");
        categoryMap.put("Science and Nature", "17");
        categoryMap.put("Computers", "18");
        categoryMap.put("Mathematics", "19");
        categoryMap.put("Mythology", "20");
        categoryMap.put("Sports", "21");
        categoryMap.put("Geography", "22");
        categoryMap.put("History", "23");
        categoryMap.put("Politics", "24");
        categoryMap.put("Comics", "29");
        categoryMap.put("Animals", "27");
        categoryMap.put("Vehicles", "28");
    }

    public static HashMap<String, String> getCategoryMap(){
        return categoryMap;
    }

    @Override
    protected void onResume() {
        super.onResume();
        makeDropDown(numOfQuesDD, R.array.num_of_questions_options, 0);
        makeDropDown(categoryDD, R.array.category_options, 1);
        makeDropDown(difficultyDD, R.array.difficulty_options, 2);
    }

    private void makeDropDown(AutoCompleteTextView view, int resId, int idx){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item,getResources().getStringArray(resId));
        view.setAdapter(adapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selections[idx] = parent.getItemAtPosition(position).toString();
            }
        });
    }

}