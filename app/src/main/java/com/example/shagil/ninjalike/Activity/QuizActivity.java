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
import com.example.shagil.ninjalike.data.ScoreTable;
import com.example.shagil.ninjalike.data.QuizQuestion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class QuizActivity extends AppCompatActivity {
    static int qNo=0;
    ArrayList<Integer> incorrectAns;
    TextView qno,question;
    Button submitButton;
    List<QuizQuestion> quizQuestionList,quizQuestionList2;
    RadioGroup rg;
    RadioButton option1,option2,option3,option4;
    static int score=0;
    String skill;
    static final String MY_PREF_NAME="sharedPref";
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

        incorrectAns=new ArrayList<>();
        DatabaseHelper dbHelper=new DatabaseHelper(this);
        quizQuestionList=dbHelper.getQuestionOfSkill(skill);
     /*   for (int i=0;i<quizQuestionList.size();i++){
            incorrectAns.add(i,quizQuestionList.get(i).getQid());
        }*/

        incorrectAns=dbHelper.getQidLevelSolvedTable(skill);
        Log.v("false",incorrectAns.toString());

        quizQuestionList2=dbHelper.getQuestionOfSkill(skill,incorrectAns);

        iterator=quizQuestionList2.iterator();
            try{
                getNextQuestion(iterator.next());
            }catch (NoSuchElementException e){
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
                    if (radioButton.getText().toString().equals(next.getLocalCorrectAnswer())){
                        Toast.makeText(getApplicationContext(),"Correct Answer",Toast.LENGTH_SHORT).show();
                        dbHelper.updateStatus(next.getQid(),skill);
                        //dbHelper.updateScoreTable(skill,"+1","+0","+4");
                        try {
                            getNextQuestion(iterator.next());
                        }catch (NoSuchElementException e){
                            Intent intent=new Intent(QuizActivity.this,ScoreTable.class);
                            startActivity(intent);
                            finish();
                        }


                    }else {
                        Toast.makeText(getApplicationContext(),"InCorrect Answer",Toast.LENGTH_SHORT).show();
                        //dbHelper.updateScoreTable(skill,"+0","+1","-1");
                        try {
                            getNextQuestion(iterator.next());
                        }catch (NoSuchElementException e){
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
