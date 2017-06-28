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
import com.example.shagil.ninjalike.data.QuizQuestion;
import com.example.shagil.ninjalike.data.ScoreCard;

/*Activity class to show the score
* */

public class ScoreTable extends AppCompatActivity {
    String skill,level;
    TextView skillTV,score,levelTV;
    Button playAgain, mainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_table);
        skill= QuizActivity.skill;
        level=getIntent().getStringExtra("level");
        skillTV=(TextView)findViewById(R.id.skillTV);
        score=(TextView)findViewById(R.id.score_TV);
        levelTV=(TextView)findViewById(R.id.level_TV);

        playAgain=(Button)findViewById(R.id.playagain);
        mainMenu=(Button)findViewById(R.id.mainmenu);


        DatabaseHelper dbHelper=new DatabaseHelper(this);
        ScoreCard scoreCard= dbHelper.getScores(skill,level);

        skillTV.setText(scoreCard.getSkill());

        score.setText(String.valueOf(scoreCard.getScore()));

        levelTV.setText(scoreCard.getLevel());

        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ScoreTable.this,LevelActivity.class);
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
