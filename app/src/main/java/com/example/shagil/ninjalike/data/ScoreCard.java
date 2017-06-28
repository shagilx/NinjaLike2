package com.example.shagil.ninjalike.data;

/**
 * Created by Siddiqui on 5/31/2017.
 */

public class ScoreCard {
    String skill;
    String level;
    int score;

    public ScoreCard(String skill,String level, int score) {
        this.skill = skill;
        this.level=level;
        this.score = score;
    }



    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public ScoreCard() {
    }
}
