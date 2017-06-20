package com.example.shagil.ninjalike.data;

/**
 * Created by Siddiqui on 5/24/2017.
 */

public class QuizQuestion {
    private int qid;
    private int correctAnswerIndex;
    private String[] correctAnswer;
    private String skill;
    private String level;

    private String question;

    private String[] options;


    public QuizQuestion() {
    }

    public QuizQuestion(String question, int correctAnswerIndex, String skill,
                        String[] options,String level) {
        this.question = question;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
        this.skill=skill;
        this.level=level;
    }


    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public void setCorrectAnswer(String[] correctAnswer) {
        this.correctAnswer = correctAnswer;
    }


    public String[] getLocalCorrectAnswer(){
        return correctAnswer;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public String[] getOptions() {
        return options;
    }
    public String getSkill(){
        return skill;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return options[correctAnswerIndex-1];
    }
}
