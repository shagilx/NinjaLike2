package com.example.shagil.ninjalike.data;

/**
 * Created by Siddiqui on 5/31/2017.
 */

public class ScoreCard {
    String skill;
    int solved;
    int unsolved;
    int score;

    public ScoreCard(String skill, int solved, int unsolved, int score) {
        this.skill = skill;
        this.solved = solved;
        this.unsolved = unsolved;
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

    public int getSolved() {
        return solved;
    }

    public void setSolved(int solved) {
        this.solved = solved;
    }

    public int getUnsolved() {
        return unsolved;
    }

    public void setUnsolved(int unsolved) {
        this.unsolved = unsolved;
    }

    public ScoreCard() {
    }
}
