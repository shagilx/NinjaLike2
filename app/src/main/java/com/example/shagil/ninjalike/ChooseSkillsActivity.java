package com.example.shagil.ninjalike;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.shagil.ninjalike.Helper.DatabaseHelper;
import com.example.shagil.ninjalike.data.QuizQuestion;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ChooseSkillsActivity extends AppCompatActivity {
    Button nextButton;
    FragmentManager fm=getSupportFragmentManager();
    public static String skill;
    List<QuizQuestion> quizQuestionList;
    RadioButton radioButton1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_skills);

        nextButton=(Button)findViewById(R.id.skill_button);

        DatabaseHelper dbHelper=new DatabaseHelper(this);
        String[] levels=dbHelper.getLevels();
        int achievedLevelCount=dbHelper.getUserAchievedLevels(LoginActivity.userName);
        Log.v("achievedLevel",String.valueOf(achievedLevelCount));

        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.chooseSkillsLayout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams leftMargin=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        leftMargin.leftMargin=50;

        final RadioGroup rg=new RadioGroup(this);
        radioButton1 = new RadioButton(this);
        rg.setOrientation(LinearLayout.VERTICAL);
        for (int i=0;i<levels.length;i++) {
            String url="levelRadioButton"+(i);

            radioButton1 = new RadioButton(this);
            radioButton1.setText(levels[i]);
            radioButton1.setId(i);
            radioButton1.setTag(url);
            radioButton1.setEnabled(false);
            if (i==0)
                radioButton1.setEnabled(true);
            if (i<=achievedLevelCount)
                radioButton1.setEnabled(true);
            rg.addView(radioButton1,leftMargin);
            Log.v("RBid",String.valueOf(radioButton1.getTag()));
        }
        ((ViewGroup)findViewById(R.id.radiogroup)).addView(rg);


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton;
                int selectedId =rg.getCheckedRadioButtonId();

                radioButton = (RadioButton) findViewById(selectedId);
                if (radioButton != null) {
                    skill=radioButton.getText().toString();
                    DatabaseHelper dbHelper=new DatabaseHelper(getApplicationContext());
                    dbHelper.createLevelSolvedTable(skill);
                    quizQuestionList=dbHelper.getQuestionOfSkill(skill);
                    Log.v("questionList2",String.valueOf(quizQuestionList.size()));
                    boolean isExist=dbHelper.isEntryExist(skill);
                    if (!isExist){
                        dbHelper.initaliseScoreTable(skill);
                        for (int i=0;i<quizQuestionList.size();i++) {
                            dbHelper.initializeLevelSolvedTable("false", skill, quizQuestionList.get(i).getQid());
                        }
                        dbHelper.insertIntoUserCurrentLevel(skill);
                    }

                   /* if (!pref.contains("register")){
                        editor=pref.edit();
                        editor.putString("register","true");
                        editor.apply();

                        dbHelper.initaliseScoreTable(skill);
                        for (int i=0;i<quizQuestionList.size();i++) {
                            dbHelper.initializeLevelSolvedTable("false", skill, quizQuestionList.get(i).getQid());
                        }
                    }*/



                    boolean haveQuestions=dbHelper.checkUnsolved(skill);
                    if (haveQuestions) {
                        Intent intent = new Intent(ChooseSkillsActivity.this, QuizActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Bundle bundle=new Bundle();
                        bundle.putString("skill",skill);
                        AlertDialogFragment alertDialogFragment=new AlertDialogFragment();
                        alertDialogFragment.setArguments(bundle);
                        alertDialogFragment.show(fm,"Alert Dialog");
                        Toast.makeText(getApplicationContext(),"Solved all Questions",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(getApplicationContext(),"Please Select an Option",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.mmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                SharedPreferences sharedPreferences=getSharedPreferences(LoginActivity.MY_PREF_NAME,MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.apply();
        }
        return super.onOptionsItemSelected(item);
    }
}
