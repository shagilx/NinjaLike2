package com.example.shagil.ninjalike;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shagil.ninjalike.Helper.DatabaseHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    static int qNo=0;
    static int i;
    ArrayList<Integer> incorrectAns;
    TextView qno,question;
    Button submitButton;
    List<QuizQuestion> quizQuestionList;
    RadioGroup rg;
    RadioButton option1,option2,option3,option4;
    static int score=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        String skill = getIntent().getStringExtra("skill");
        incorrectAns=new ArrayList<>();

        qno = (TextView) findViewById(R.id.qno);
        question = (TextView) findViewById(R.id.textView6);
        submitButton = (Button) findViewById(R.id.button);

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        quizQuestionList = db.getQuestionOfSkill(skill);
        for (int i=qNo;i<quizQuestionList.size();i++){
            incorrectAns.add(qNo,qNo);
        }
        Log.v("Incorrect",String.valueOf(incorrectAns.size()));

        rg = (RadioGroup) findViewById(R.id.optionRadioGroup);
        option1 = (RadioButton) findViewById(R.id.option1);
        option2 = (RadioButton) findViewById(R.id.option2);
        option3 = (RadioButton) findViewById(R.id.option3);
        option4 = (RadioButton) findViewById(R.id.option4);

        getNextQuestion(qNo++);
    }


    private void getNextQuestion(final int qNum) {


            rg.clearCheck();
            qno.setText(String.valueOf(i + 1));
            question.setText(quizQuestionList.get(qNum).getQuestion());
            option1.setText(quizQuestionList.get(qNum).getAnswers()[0]);
            option2.setText(quizQuestionList.get(qNum).getAnswers()[1]);
            option3.setText(quizQuestionList.get(qNum).getAnswers()[2]);
            option4.setText(quizQuestionList.get(qNum).getAnswers()[3]);

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioButton radioButton;
                    int selectedId = rg.getCheckedRadioButtonId();
                    radioButton = (RadioButton) findViewById(selectedId);
                    if (radioButton != null) {
                        if (radioButton.getText().toString().equals(quizQuestionList.get(qNum).getLocalCorrectAnswer())) {
                            Toast.makeText(getApplicationContext(), "Correct Answer", Toast.LENGTH_SHORT).show();
//                            incorrectAns.remove(qNum);
                            score += 4;
                            getNextQuestion(++qNo);
                        } else {
                            Toast.makeText(getApplicationContext(), "Incorrect Answer", Toast.LENGTH_SHORT).show();
                            score -= 1;
                            getNextQuestion(++qNo);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please select an Option", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    }
