package com.example.shagil.ninjalike;

import com.example.shagil.ninjalike.data.QuizQuestion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Siddiqui on 5/24/2017.
 */

public class QuizQuestions {


    static final String[] questions={
            "What is the size of wchar_t in C++?","Pick the odd one out","Which datatype is used to represent the absence of parameters?",
            "What does a escape code represent?","Which type is best suited to represent the logical values?","Identify the user-defined types from the following?",
            "Which of the following statements are true?\n" + "    int f(float)","The value 132.54 can represented using which data type?",
            "When a language has the capability to produce new data type mean, it can be called as"};
    static final String[] option1={"2","array type","int","alert","integer","f is a function taking an argument of type int and retruning a floating point number",
            "double","overloaded","integer, character, boolean, floating"};
    static final String[] option2={"4","character type","short","backslash","boolean","f is a function taking an argument of type float and returning a integer.",
            "void","extensible","enumeration, classes"};
    static final String[] option3={"2 or 4","boolean type","void","tab","character","f is a function of type float","int","encapsulated","integer, enum, void"};
    static final String[] option4={"based on the number of bits in the system","integer type","float","form feed","all of the above","none of the mentioned","bool","reprehensible","arrays, pointer, classes"};
    static final int[] correctAnswer={4,1,3,1,2,3,2,1,2};
    public static final String[] levels={"C","C++","Python","Java"};


    private List<QuizQuestion> quizQuestions;

    public QuizQuestions() {
        this.quizQuestions = addQuizQuestions();
    }

    public List<QuizQuestion> getQuizQuestions() {
        return quizQuestions;
    }

    public List<QuizQuestion> addQuizQuestions() {
            List<QuizQuestion> quizQuestions = new ArrayList<>();

            for (int i=0;i<questions.length;i++){
                String[] answers={
                        option1[i],
                        option2[i],
                        option3[i],
                        option4[i]};
                QuizQuestion quizQuestion=new QuizQuestion(questions[i],correctAnswer[i],"Java",answers);
                quizQuestions.add(quizQuestion);
            }
      return quizQuestions;
    }
}
