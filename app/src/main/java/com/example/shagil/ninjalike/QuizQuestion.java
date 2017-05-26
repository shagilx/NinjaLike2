package com.example.shagil.ninjalike;

/**
 * Created by Siddiqui on 5/24/2017.
 */

public class QuizQuestion {
    private final int correctAnswerIndex;
    private final String level;

    private final String question;

    private final String[] answers;

    public QuizQuestion(String question, int correctAnswerIndex, String level,
                        String[] answers) {
        this.question = question;
        this.answers = answers;
        this.correctAnswerIndex = correctAnswerIndex;
        this.level=level;
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
