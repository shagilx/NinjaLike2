package com.example.shagil.ninjalike.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.shagil.ninjalike.AlertDialogFragment;
import com.example.shagil.ninjalike.Helper.DatabaseHelper;
import com.example.shagil.ninjalike.Helper.SkillsDbHelper;
import com.example.shagil.ninjalike.R;
import com.example.shagil.ninjalike.data.QuizQuestion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*This class will help the user to choose from skill levels in form of radio buttons.
* It will first get the levels from local DB.
* then check the user's achieved levels.
* These achieved levels will help to unlock the next level.
* Next level will only be unlocked when its previous level is 100% solved.
*
* Initially the first level will be unlocked by default*/

public class ChooseSkillsActivity extends AppCompatActivity {
    Button nextButton;
    FragmentManager fm=getSupportFragmentManager();
    public static String skill;
    List<QuizQuestion> quizQuestionList;
    //RadioButton radioButton1;
    LinearLayout.LayoutParams leftMargin;
    //RadioGroup rg;
    String[] levels;
    int achievedLevelCount;
    CheckBox checkBox;
    DatabaseHelper dbHelper=new DatabaseHelper(this);
    ArrayList<String> selectedStrings = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_skills);
        //initialising widgets
        nextButton=(Button)findViewById(R.id.skill_button);
        //get skill levels from the local database

        levels=dbHelper.getSkills();
        //get user achieved levels from local db.
        achievedLevelCount=dbHelper.getUserAchievedLevels(LoginActivity.userName);

        setLayout();



        nextButton.setOnClickListener(goToQuiz);

    }
    private View.OnClickListener goToQuiz=new View.OnClickListener(){

        public void onClick(View v){

            Iterator<String> iterator=selectedStrings.iterator();
            while(iterator.hasNext())
            Log.v("checkBox",String.valueOf(iterator.next()));
            dbHelper.createTempQuestionTable(selectedStrings);
            /*
            RadioButton radioButton;
            int selectedId =rg.getCheckedRadioButtonId();

            radioButton = (RadioButton) findViewById(selectedId);
            if (radioButton != null) {
                skill=radioButton.getText().toString();
                //create table that have an account of questions solved or unsolved state
                initializeTables(skill,dbHelper);

                //check for the unsolved questions. if exist then continue to quiz else create a prompt to reset the score and play again.
                boolean haveQuestions=dbHelper.checkUnsolved(skill);
                if (haveQuestions) {
                    Intent intent = new Intent(ChooseSkillsActivity.this, QuizActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    createAlertDialog();

                    Toast.makeText(getApplicationContext(),"Solved all Questions",Toast.LENGTH_SHORT).show();
                }
            }
            else
                Toast.makeText(getApplicationContext(),"Please Select an Option",Toast.LENGTH_SHORT).show();


        */}
    };

    private void createAlertDialog() {
        Bundle bundle=new Bundle();
        bundle.putString("skill",skill);
        AlertDialogFragment alertDialogFragment=new AlertDialogFragment();
        alertDialogFragment.setArguments(bundle);
        alertDialogFragment.show(fm,"Alert Dialog");
    }

    private void initializeTables(String skill, SkillsDbHelper dbHelper) {
        dbHelper.createSkillSolvedTable(skill);
        //fetch the questions of selected skill
        quizQuestionList= dbHelper.getQuestionOfSkill(skill);

        boolean isExist= dbHelper.isEntryExist(skill);
        //if playing for the first time, then initialise the score table to 0, and initialise the solved status of all questions to false
        if (!isExist){
            //dbHelper.initaliseScoreTable(skill);
            for (int i=0;i<quizQuestionList.size();i++) {
                dbHelper.initializeLevelSolvedTable("false", skill, quizQuestionList.get(i).getQid());
            }
            dbHelper.insertIntoUserCurrentLevel(skill);
        }
    }

    private void setLayout() {
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.chooseSkillsLayout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        leftMargin=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        leftMargin.leftMargin=50;

        //rg=new RadioGroup(ChooseSkillsActivity.this);
        //radioButton1 = new RadioButton(ChooseSkillsActivity.this);
        //rg.setOrientation(LinearLayout.VERTICAL);

        for (int i=0;i<levels.length;i++) {
            String url="skillCheckBox"+(i);
            checkBox=new CheckBox(getApplicationContext());
            checkBox.setText(levels[i]);
            checkBox.setId(i);
            checkBox.setLayoutParams(leftMargin);
            linearLayout.addView(checkBox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b)
                    selectedStrings.add(compoundButton.getText().toString());
                    else
                        selectedStrings.remove(compoundButton.getText().toString());
                }
            });
            //checkBox.setTag(url);
            //setting radio buttons in disabled state
            /*radioButton1.setEnabled(false);
            //setting the first skill level radio button enabled initially
            if (i==0)
                radioButton1.setEnabled(true);
            //setting next skill level radio button enabled depending upon the achievement of previous level
            if (i<=achievedLevelCount)
                radioButton1.setEnabled(true);
            rg.addView(radioButton1,leftMargin);
            Log.v("RBid",String.valueOf(radioButton1.getTag()));*/

        }

        //adding the radio buttons to UI
      //  ((ViewGroup)findViewById(R.id.radiogroup)).addView(rg);
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
