package com.example.shagil.ninjalike;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.shagil.ninjalike.Helper.DatabaseHelper;

public class ScoreTable extends AppCompatActivity {
    String skill;
    TextView skillTV,score,solved,unsolved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_table);
        skill=getIntent().getStringExtra("skill");
        skillTV=(TextView)findViewById(R.id.skillTV);
        score=(TextView)findViewById(R.id.scoreTV);
        solved=(TextView)findViewById(R.id.solvedTV);
        unsolved=(TextView)findViewById(R.id.unsolvedTV);


        DatabaseHelper dbHelper=new DatabaseHelper(this);
        ScoreCard scoreCard= dbHelper.getScores(skill);

        skillTV.setText(scoreCard.getSkill());
        score.setText(String.valueOf(scoreCard.getScore()));
        solved.setText(String.valueOf(scoreCard.getSolved()));
        unsolved.setText(String.valueOf(scoreCard.getUnsolved()));

    }
}
