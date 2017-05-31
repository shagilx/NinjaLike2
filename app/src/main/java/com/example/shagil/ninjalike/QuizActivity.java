package com.example.shagil.ninjalike;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shagil.ninjalike.Helper.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    static int qNo=0;
    ArrayList<Integer> incorrectAns;
    TextView qno,question;
    Button submitButton;
    List<QuizQuestion> quizQuestionList;
    RadioGroup rg;
    RadioButton option1,option2,option3,option4;
    static int score=0;
    String skill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        skill = getIntent().getStringExtra("skill");
        incorrectAns=new ArrayList<>();

        qno = (TextView) findViewById(R.id.qno);
        question = (TextView) findViewById(R.id.textView6);
        submitButton = (Button) findViewById(R.id.button);

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        quizQuestionList = db.getQuestionOfSkill(skill);
        for (int i=qNo;i<quizQuestionList.size();i++){
            incorrectAns.add(i,quizQuestionList.get(i).getQid());
        }
        Log.v("Incorrect",String.valueOf(incorrectAns.toString()));

        rg = (RadioGroup) findViewById(R.id.optionRadioGroup);
        option1 = (RadioButton) findViewById(R.id.option1);
        option2 = (RadioButton) findViewById(R.id.option2);
        option3 = (RadioButton) findViewById(R.id.option3);
        option4 = (RadioButton) findViewById(R.id.option4);

            getNextQuestion(incorrectAns.iterator().next());


    }


    private void getNextQuestion(final int qNum) {


        rg.clearCheck();
        qno.setText(String.valueOf(qNum + 1));
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
                        DatabaseHelper dbHelper=new DatabaseHelper(getBaseContext());
                        dbHelper.insertIntoLevelSolvedTable("true",skill,quizQuestionList.get(qNum).getQid());
                        //incorrectAns.remove(incorrectAns.indexOf(qNum));
                        Log.v("Incorrect",incorrectAns.toString());
                        score += 4;
                        Log.v("Qnum",String.valueOf(qNum));
                        getNextQuestion(incorrectAns.iterator().next());


                    } else {
                        Toast.makeText(getApplicationContext(), "Incorrect Answer", Toast.LENGTH_SHORT).show();
                        score -= 1;
                        DatabaseHelper dbHelper=new DatabaseHelper(getBaseContext());
                        dbHelper.insertIntoLevelSolvedTable("false",skill, quizQuestionList.get(qNum).getQid());
                        Log.v("Qnum",String.valueOf(qNum));
                        Log.v("Incorrect",incorrectAns.toString());
                        getNextQuestion(incorrectAns.indexOf(qNum)+1);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please select an Option", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
