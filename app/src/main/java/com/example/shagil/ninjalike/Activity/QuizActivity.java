package com.example.shagil.ninjalike.Activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shagil.ninjalike.AlertDialogFragment;
import com.example.shagil.ninjalike.Helper.DatabaseHelper;
import com.example.shagil.ninjalike.R;
import com.example.shagil.ninjalike.data.QuizQuestion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class QuizActivity extends AppCompatActivity {
    ArrayList<Integer> incorrectAns;
    TextView qno,question;
    Button submitButton;
    List<QuizQuestion> quizQuestionList,quizQuestionList2;
    RadioGroup rg;
    RadioButton option1,option2,option3,option4;
    String skill;
    static Iterator<QuizQuestion> iterator;
    FragmentManager fm=getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        skill=ChooseSkillsActivity.skill;

        qno = (TextView) findViewById(R.id.qno);
        question = (TextView) findViewById(R.id.textView6);
        submitButton = (Button) findViewById(R.id.button);
        
        rg = (RadioGroup) findViewById(R.id.optionRadioGroup);
        option1 = (RadioButton) findViewById(R.id.option1);
        option2 = (RadioButton) findViewById(R.id.option2);
        option3 = (RadioButton) findViewById(R.id.option3);
        option4 = (RadioButton) findViewById(R.id.option4);

        //List to store the question ID of unsolved questions
        incorrectAns=new ArrayList<>();
        DatabaseHelper dbHelper=new DatabaseHelper(this);
        //get unsolved questions
        incorrectAns=dbHelper.getQidLevelSolvedTable(skill);
        Log.v("false",incorrectAns.toString());
        //get unsolved questions of the skill
        quizQuestionList2=dbHelper.getQuestionOfSkill(skill,incorrectAns);

        //iterator to start quiz for each unsolved question
        iterator=quizQuestionList2.iterator();
            try{
                //method to get next question
                getNextQuestion(iterator.next());
            }catch (NoSuchElementException e){
                //when no more questions left, create a prompt to reset the score and play again
                Bundle bundle=new Bundle();
                bundle.putString("skill",skill);
                AlertDialogFragment alertDialogFragment=new AlertDialogFragment();
                alertDialogFragment.setArguments(bundle);
                alertDialogFragment.show(fm,"Alert Dialog");

                Toast.makeText(getApplicationContext()," ",Toast.LENGTH_SHORT).show();
            }
    }

        private void getNextQuestion(final QuizQuestion next) {
        rg.clearCheck();
        qno.setText(String.valueOf(next.getQid() + 1));
        question.setText(next.getQuestion());
        option1.setText(next.getAnswers()[0]);
        option2.setText(next.getAnswers()[1]);
        option3.setText(next.getAnswers()[2]);
        option4.setText(next.getAnswers()[3]);
        final DatabaseHelper dbHelper=new DatabaseHelper(this);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton;
                int selectedId = rg.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                if (radioButton!=null){
                    //if the option is correct
                    if (radioButton.getText().toString().equals(next.getLocalCorrectAnswer())){
                        Toast.makeText(getApplicationContext(),"Correct Answer",Toast.LENGTH_SHORT).show();
                        //update the solved status of the question to true
                        dbHelper.updateStatus(next.getQid(),skill);
                        try {
                            getNextQuestion(iterator.next());

                        }catch (NoSuchElementException e){
                            //when all the questions have been attempt
                            Intent intent=new Intent(QuizActivity.this,ScoreTable.class);
                            startActivity(intent);
                            finish();
                        }

                    //if false
                    }else {
                        Toast.makeText(getApplicationContext(),"InCorrect Answer",Toast.LENGTH_SHORT).show();
                        try {
                            getNextQuestion(iterator.next());
                        }catch (NoSuchElementException e){
                            //when all the questions have been attempt
                            Intent intent=new Intent(QuizActivity.this,ScoreTable.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Please Select Option",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
