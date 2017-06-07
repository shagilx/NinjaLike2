package com.example.shagil.ninjalike.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.shagil.ninjalike.Helper.DatabaseHelper;
import com.example.shagil.ninjalike.R;
import com.example.shagil.ninjalike.data.ScoreCard;

/*Activity class to show the score
* */

public class ScoreTable extends AppCompatActivity {
    String skill;
    TextView skillTV,score,solved,unsolved;
    Button playAgain, mainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_table);
        skill= ChooseSkillsActivity.skill;
        skillTV=(TextView)findViewById(R.id.skillTV);
        score=(TextView)findViewById(R.id.scoreTV);
        solved=(TextView)findViewById(R.id.solvedTV);
        unsolved=(TextView)findViewById(R.id.unsolvedTV);
        playAgain=(Button)findViewById(R.id.playagain);
        mainMenu=(Button)findViewById(R.id.mainmenu);


        DatabaseHelper dbHelper=new DatabaseHelper(this);
        ScoreCard scoreCard= dbHelper.getScores(skill);

        skillTV.setText(scoreCard.getSkill());
        Log.v("Skill",scoreCard.getSkill());
        score.setText(String.valueOf(scoreCard.getScore()));
        Log.v("Score",String.valueOf(scoreCard.getScore()));
        solved.setText(String.valueOf(scoreCard.getSolved()));
        unsolved.setText(String.valueOf(scoreCard.getUnsolved()));
        if (scoreCard.getUnsolved()==0){
            dbHelper.setAchievedLevel(skill);
        }

        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ScoreTable.this,QuizActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ScoreTable.this,ChooseSkillsActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
