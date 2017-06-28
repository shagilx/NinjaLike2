package com.example.shagil.ninjalike.Activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shagil.ninjalike.AlertDialogFragment;
import com.example.shagil.ninjalike.Helper.DatabaseHelper;
import com.example.shagil.ninjalike.R;
import com.example.shagil.ninjalike.data.QuizQuestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class QuizActivity extends AppCompatActivity {
    //ArrayList<Integer> incorrectAns;
    TextView qno,question;
    Button submitButton;
    List<QuizQuestion> quizQuestionList;
    CheckBox option1,option2,option3,option4;
    static String skill;
    FragmentManager fm=getSupportFragmentManager();
    DatabaseHelper dbHelper=new DatabaseHelper(this);
    static Iterator<QuizQuestion> iterator;
    ArrayList<String> selectedOptions = new ArrayList<String>();
    int totalScore=0,userScore=0;
    String level;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        skill=getIntent().getStringExtra("skill");
        level=getIntent().getStringExtra("level");
        qno = (TextView) findViewById(R.id.qno);
        question = (TextView) findViewById(R.id.textView6);
        submitButton = (Button) findViewById(R.id.button);
        option1 = (CheckBox) findViewById(R.id.option1);
        option2 = (CheckBox) findViewById(R.id.option2);
        option3 = (CheckBox) findViewById(R.id.option3);
        option4 = (CheckBox) findViewById(R.id.option4);

        quizQuestionList=dbHelper.getQuestionOfLevel(skill,level);
        iterator=quizQuestionList.iterator();
        Log.v("listsize",String.valueOf(quizQuestionList.size()));
        totalScore=4*quizQuestionList.size();


        try{
            getNextQuestion(iterator.next());

        }catch(NoSuchElementException e){
            Toast.makeText(getApplicationContext()," ",Toast.LENGTH_SHORT).show();
        }
    }


    private void getNextQuestion(final QuizQuestion next) {
        selectedOptions.clear();
        if (option1.isChecked())
            option1.setChecked(false);
        if (option2.isChecked())
            option2.setChecked(false);
        if (option3.isChecked())
            option3.setChecked(false);
        if (option4.isChecked())
            option4.setChecked(false);

        qno.setText(String.valueOf(next.getQid() + 1));
        question.setText(next.getQuestion());
        option1.setText(next.getOptions()[0]);
        option2.setText(next.getOptions()[1]);
        option3.setText(next.getOptions()[2]);
        option4.setText(next.getOptions()[3]);
        option1.setOnCheckedChangeListener(new getAnswerListener());
        option2.setOnCheckedChangeListener(new getAnswerListener());
        option3.setOnCheckedChangeListener(new getAnswerListener());
        option4.setOnCheckedChangeListener(new getAnswerListener());
        Log.v("correct",String.valueOf(selectedOptions.size()));
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Arrays.asList(next.getLocalCorrectAnswer()).containsAll(selectedOptions)) {
                        Toast.makeText(getApplicationContext(), "true", Toast.LENGTH_SHORT).show();
                        userScore+=4;
                        try {
                            getNextQuestion(iterator.next());
                        }catch (NoSuchElementException e){
                            checkScore(userScore);
                            Intent intent=new Intent(QuizActivity.this,ScoreTable.class);
                            intent.putExtra("level",level);
                            startActivity(intent);
                            finish();
                        }

                    }
                    else {
                        Toast.makeText(getApplicationContext(),"false",Toast.LENGTH_SHORT).show();
                        userScore-=1;
                        try {
                            getNextQuestion(iterator.next());
                        }catch (NoSuchElementException e){
                            checkScore(userScore);
                            Intent intent=new Intent(QuizActivity.this,ScoreTable.class);
                            intent.putExtra("level",level);
                            startActivity(intent);
                            finish();
                        }

                    }
                }
            });

        }

    private void checkScore(int userScore) {
        if (userScore==totalScore){
            DatabaseHelper dbHelper=new DatabaseHelper(this);
            dbHelper.insertScore(skill,level,userScore,"true");
        }else{
            dbHelper.insertScore(skill,level,userScore,"false");
        }
    }

    private class getAnswerListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                if (buttonView==option1)
                    selectedOptions.add(buttonView.getText().toString());
                if (buttonView==option2)
                    selectedOptions.add(buttonView.getText().toString());
                if (buttonView==option3)
                    selectedOptions.add(buttonView.getText().toString());
                if (buttonView==option4)
                    selectedOptions.add(buttonView.getText().toString());
            }else{
                if (buttonView==option1)
                    selectedOptions.remove(buttonView.getText().toString());
                if (buttonView==option2)
                    selectedOptions.remove(buttonView.getText().toString());
                if (buttonView==option3)
                    selectedOptions.remove(buttonView.getText().toString());
                if (buttonView==option4)
                    selectedOptions.remove(buttonView.getText().toString());
            }
        }
    }
}
