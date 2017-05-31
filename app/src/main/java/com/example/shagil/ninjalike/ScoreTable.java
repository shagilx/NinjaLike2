package com.example.shagil.ninjalike;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.shagil.ninjalike.Helper.DatabaseHelper;

public class ScoreTable extends AppCompatActivity {
    String skill;
    TextView skillTV,score,solved,unsolved;
    Button playAgain, mainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_table);
        skill=getIntent().getStringExtra("skill");
        skillTV=(TextView)findViewById(R.id.skillTV);
        score=(TextView)findViewById(R.id.scoreTV);
        solved=(TextView)findViewById(R.id.solvedTV);
        unsolved=(TextView)findViewById(R.id.unsolvedTV);
        playAgain=(Button)findViewById(R.id.playagain);
        mainMenu=(Button)findViewById(R.id.mainmenu);


        DatabaseHelper dbHelper=new DatabaseHelper(this);
        ScoreCard scoreCard= dbHelper.getScores(skill);

        skillTV.setText(scoreCard.getSkill());
        score.setText(String.valueOf(scoreCard.getScore()));
        solved.setText(String.valueOf(scoreCard.getSolved()));
        unsolved.setText(String.valueOf(scoreCard.getUnsolved()));

        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ScoreTable.this,QuizActivity.class);
                startActivity(intent);
            }
        });

        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ScoreTable.this,ChooseSkillsActivity.class);
                startActivity(intent);
            }
        });

    }
}
