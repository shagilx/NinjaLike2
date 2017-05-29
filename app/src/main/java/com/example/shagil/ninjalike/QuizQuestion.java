package com.example.shagil.ninjalike;

/**
 * Created by Siddiqui on 5/24/2017.
 */

public class QuizQuestion {
    private int correctAnswerIndex;
    private String correctAnswer;
    private String level;

    private String question;

    private String[] answers;

    public QuizQuestion() {
    }

    public QuizQuestion(String question, int correctAnswerIndex, String level,
                        String[] answers) {
        this.question = question;
        this.answers = answers;
        this.correctAnswerIndex = correctAnswerIndex;
        this.level=level;
    }

    public QuizQuestion(String question, String correctAnswer, String skill, String[] answers) {
        this.question = question;
        this.answers = answers;
        this.correctAnswer= correctAnswer;

    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
    public String getLocalCorrectAnswer(){
        return correctAnswer;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public String[] getAnswers() {
        return answers;
    }
    public String getLevel(){
        return level;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return answers[correctAnswerIndex-1];
    }
}
