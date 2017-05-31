package com.example.shagil.ninjalike;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.shagil.ninjalike.Helper.DatabaseHelper;

import java.io.ByteArrayOutputStream;

public class ChooseSkillsActivity extends AppCompatActivity {
    Button nextButton;
    public static final String[] levels={"C++","C","Python","Java"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_skills);

        nextButton=(Button)findViewById(R.id.skill_button);

        DatabaseHelper dbHelper=new DatabaseHelper(this);
        String[] levels=dbHelper.getLevels();

      // insertLevels();
     //   insertQuestions();

        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.chooseSkillsLayout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams leftMargin=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        leftMargin.leftMargin=50;

        final RadioGroup rg=new RadioGroup(this);
        rg.setOrientation(LinearLayout.VERTICAL);
        for (int i=0;i<levels.length;i++) {
            String url="levelRadioButton"+(i);

            RadioButton radioButton1 = new RadioButton(this);
            radioButton1.setText(levels[i]);
            radioButton1.setId(i);
            radioButton1.setTag(url);
            rg.addView(radioButton1,leftMargin);
            Log.v("RBid",String.valueOf(radioButton1.getTag()));
        }
        ((ViewGroup)findViewById(R.id.radiogroup)).addView(rg);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton=new RadioButton(ChooseSkillsActivity.this);
                int selectedId=rg.getCheckedRadioButtonId();

                radioButton=(RadioButton)findViewById(selectedId);
                if (radioButton!=null) {
                    Toast.makeText(getApplicationContext(), radioButton.getText(), Toast.LENGTH_SHORT).show();
                    DatabaseHelper dbHelper=new DatabaseHelper(getApplicationContext());
                    dbHelper.createLevelSolvedTable(radioButton.getText());
                    dbHelper.createScoreTable();
//                    dbHelper.initaliseScoreTable(radioButton.getText());
                   // dbHelper.createTempSkillTable(radioButton.getText());
                    Intent intent = new Intent(ChooseSkillsActivity.this, QuizActivity.class);
                    intent.putExtra("skill",radioButton.getText());
                    startActivity(intent);
                }
                else
                    Toast.makeText(getApplicationContext(),"Please Select an Option",Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void insertQuestions() {
        DatabaseHelper db=new DatabaseHelper(getApplicationContext());
        db.insertQuestions();
    }

    private void insertLevels() {
        for (int i=0;i<levels.length;i++) {
            DatabaseHelper db=new DatabaseHelper(getApplicationContext());
            String url="drawable/"+"level_"+(i+1);

            int imageKey=getResources().getIdentifier(url,"drawable",getPackageName());
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageKey);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bitMapData = stream.toByteArray();
            db.insertLevels(levels[i],bitMapData);
        }
    }
}
