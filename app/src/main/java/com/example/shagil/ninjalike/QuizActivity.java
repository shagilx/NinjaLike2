package com.example.shagil.ninjalike;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.shagil.ninjalike.Helper.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        String skill=getIntent().getStringExtra("skill");
        DatabaseHelper db=new DatabaseHelper(getApplicationContext());
        String[] question=db.getQuestionOfSkill(skill);
        List<QuizQuestion> quizQuestionList=db.getQuestionOfSkill(skill);
        quizQuestionList.
        String question=
    }
}
