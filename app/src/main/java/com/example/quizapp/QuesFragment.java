package com.example.quizapp;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;

public class QuesFragment extends Fragment {

    public QuesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ques, container, false);
        TextView questionTxt = view.findViewById(R.id.questionTxt);
        LinearLayout ll = view.findViewById(R.id.ll);
        ColorStateList colorStateList1 = new ColorStateList(
                new int[][] {
                        new int[] { -android.R.attr.state_enabled },
                        new int[] {  android.R.attr.state_enabled }
                },
                new int[] {
                        getResources().getColor(R.color.grey),
                        getResources().getColor(R.color.black)
                }
        );
        ColorStateList colorStateList2 = new ColorStateList(
                new int[][] {
                        new int[] { android.R.attr.state_checked }
                },
                new int[] {
                        getResources().getColor(R.color.green)
                }
        );
        ColorStateList colorStateList3 = new ColorStateList(
                new int[][] {
                        new int[] { android.R.attr.state_checked }
                },
                new int[] {
                        getResources().getColor(R.color.red)
                }
        );

        try {
            assert getArguments() != null;
            JSONObject jsonObject = new JSONObject(getArguments().getString("question"));
            String type = jsonObject.getString("type");
            String question = URLDecoder.decode(jsonObject.getString("question"), String.valueOf(StandardCharsets.UTF_8));
            String correctAns = URLDecoder.decode(jsonObject.getString("correct_answer"), String.valueOf(StandardCharsets.UTF_8));
            String[] arr = jsonObject.get("incorrect_answers").toString().split(",");
            arr[0] = arr[0].substring(1);
            arr[arr.length - 1] = arr[arr.length - 1].substring(0, arr[arr.length - 1].length() - 1);
            for (int i = 0; i < arr.length; i++) {
                arr[i] = URLDecoder.decode(arr[i], String.valueOf(StandardCharsets.UTF_8));
                arr[i] = arr[i].substring(1, arr[i].length() - 1);
            }
            questionTxt.setText(question);
            RadioGroup optionsRg = new RadioGroup(getContext());
            optionsRg.setOrientation(RadioGroup.VERTICAL);

            int numOfOpt = type.equals("boolean") ? 2 : 4;
            int correctAnsIdx = new Random().nextInt(numOfOpt);
            int j = 0;
            for (int i = 0; i < numOfOpt; i++) {
                AppCompatRadioButton rb = new AppCompatRadioButton(requireContext());
                if (i == correctAnsIdx) rb.setText(correctAns);
                else {
                    rb.setText(arr[j]);
                    j++;
                }
                rb.setId(i);
                rb.setTextSize(24);
                rb.setTextColor(colorStateList1);
                rb.setButtonTintList(colorStateList1);
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(getContext(), null);
                params.setMargins(16, 24, 16, 0);
                rb.setLayoutParams(params);
                optionsRg.addView(rb);
            }
            ll.addView(optionsRg);


            optionsRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    for (int i = 0; i < group.getChildCount(); i++) {
                        AppCompatRadioButton rb = (AppCompatRadioButton) group.getChildAt(i);
                        rb.setEnabled(false);
                        if (i == checkedId) {
                            if (i == correctAnsIdx) {
                                rb.setTextColor(getResources().getColor(R.color.green));
                                rb.setButtonTintList(colorStateList2);
                                rb.setText(rb.getText() + "     " + "Correct");
                                QuesActivity.setScore(QuesActivity.getScore() + 10);
                                QuesActivity.getScoreTxt().setText("Score : " + QuesActivity.getScore());
                            }
                            else {
                                rb.setTextColor(getResources().getColor(R.color.red));
                                rb.setButtonTintList(colorStateList3);
                                rb.setText(rb.getText() + "     " + "Incorrect");
                            }
                            rb.setTypeface(rb.getTypeface(), Typeface.BOLD_ITALIC);
                        }
                    }

                   QuesActivity.getBtn().setEnabled(true);
                }
            });

        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return view;
    }
}