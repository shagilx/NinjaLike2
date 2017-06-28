package com.example.shagil.ninjalike.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shagil.ninjalike.Helper.DatabaseHelper;
import com.example.shagil.ninjalike.R;

import java.util.List;

public class LevelActivity extends AppCompatActivity {
    DatabaseHelper dbHelper=new DatabaseHelper(this);
    Spinner skillSpinner;
    LinearLayout.LayoutParams leftMargin;
    RadioButton radioButton1;
    Button levelButton;
    String skill;
    String[] levels;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        linearLayout=(LinearLayout)findViewById(R.id.chooseLevelsLayout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        leftMargin=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        leftMargin.leftMargin=50;
        //int achievedLevelCount=dbHelper.getUserAchievedLevels(LoginActivity.userName);
        skillSpinner=(Spinner)findViewById(R.id.skill_spinner);
        final List<String> skills=dbHelper.getTempSkills();
        ArrayAdapter<String> skillsAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,skills);
        skillsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        skillSpinner.setAdapter(skillsAdapter);

        final RadioGroup rg=(RadioGroup)findViewById(R.id.levelRadioGroup);

        rg.setOrientation(LinearLayout.VERTICAL);

        skillSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rg.removeAllViews();
                skill=parent.getSelectedItem().toString();
                levels=dbHelper.getLevels(skill);
                int levelSolved=dbHelper.getLevelSolved(skill);

                for (int i=0;i<levels.length;i++) {
                    String url="levelRadioButton"+(i);

                    radioButton1 = new RadioButton(LevelActivity.this);
                    radioButton1.setText(levels[i]);
                    radioButton1.setId(i);
                    radioButton1.setTag(url);
                    radioButton1.setEnabled(false);
                    //setting radio buttons in disabled state
                    if (i==0)
                        radioButton1.setEnabled(true);
                    if (i<=levelSolved)
                        radioButton1.setEnabled(true);
                    rg.addView(radioButton1,leftMargin);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
     ((ViewGroup)findViewById(R.id.levelRadioGroup)).removeView(rg);



        //setting up radio buttons


        levelButton=(Button)findViewById(R.id.level_button);

        levelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton;
                int selectedID=rg.getCheckedRadioButtonId();
                radioButton=(RadioButton)findViewById(selectedID);
                String selectedLevel=radioButton.getText().toString();
                if (radioButton!=null) {
                    Toast.makeText(getApplicationContext(), selectedLevel, Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(LevelActivity.this,QuizActivity.class);
                    intent.putExtra("level",selectedLevel);
                    intent.putExtra("skill",skill);
                    startActivity(intent);
                }
            }

        });

    }

}
