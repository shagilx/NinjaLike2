package com.example.shagil.ninjalike;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.shagil.ninjalike.Helper.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    static int qNo=0;
    TextView qno,qestion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        String skill=getIntent().getStringExtra("skill");

        qno=(TextView)findViewById(R.id.qno);
        qestion=(TextView)findViewById(R.id.textView6);
        DatabaseHelper db=new DatabaseHelper(getApplicationContext());
        List<QuizQuestion> quizQuestionList=db.getQuestionOfSkill(skill);
        qno.setText(String.valueOf(qNo+1));
        qestion.setText(quizQuestionList.get(0).getQuestion());
        Log.v("Question",quizQuestionList.get(0).getQuestion());
        LinearLayout.LayoutParams leftMargin=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        leftMargin.leftMargin=50;


        final RadioGroup rg=new RadioGroup(this);
        rg.setOrientation(LinearLayout.VERTICAL);

        for (int i=0;i<quizQuestionList.get(qNo).getAnswers().length;i++) {
            String url="optionsRadioButton"+(i);

            RadioButton radioButton1 = new RadioButton(this);
            radioButton1.setText(quizQuestionList.get(qNo).getAnswers()[i]);
            radioButton1.setId(i);
            radioButton1.setTag(url);
            rg.addView(radioButton1,leftMargin);
            Log.v("RBid",String.valueOf(radioButton1.getTag()));
        }
        ((ViewGroup)findViewById(R.id.optionRadioGroup)).addView(rg);
        

    }
}
