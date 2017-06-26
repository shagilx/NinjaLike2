package com.example.shagil.ninjalike.Helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.shagil.ninjalike.Activity.LoginActivity;
import com.example.shagil.ninjalike.data.QuizQuestion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by siddiqui on 19/6/17.
 */

public class SkillsDbHelper extends DatabaseHelper {
    public SkillsDbHelper(Context context) {
        super(context);
    }
    public void createSkillSolvedTable(String skill) {
        String createTable="CREATE TABLE IF NOT EXISTS `"+ LoginActivity.userName+"_"+skill+"` ("+USERNAME+" text, "+QID+" text primary key , "+LEVEL+" TEXT, solved text default 'false' )";
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(createTable);
        Log.v("SolvedTable","table created");

    }

    public List<QuizQuestion> getQuestionOfSkill(String skill) {
        List<QuizQuestion> quizQuestionList = new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        String getQuestion="SELECT Q."+QID+", Q."+QUESTION+", Q."+LEVEL+" FROM "+QUESTIONS_TABLE+ " Q WHERE Q."+SKILLS+" = '"+skill+"'";

        Cursor c=db.rawQuery(getQuestion,null);
        Log.v("Cursor",String.valueOf(c.getCount()));

        if (c.moveToFirst()){
            do {
                QuizQuestion quizQuestion=new QuizQuestion();

                String selectOptions="SELECT O."+OPTION1+", O."+OPTION2+", O."+OPTION3+", O."+OPTION4+" FROM "+OPTIONS_TABLE+ " O WHERE O."+QID+" = "+c.getInt(0);
                Cursor c1=db.rawQuery(selectOptions,null);
                quizQuestion.setQid(c.getInt(0));
                quizQuestion.setQuestion(c.getString(1));
                quizQuestion.setLevel(c.getString(2));
                String correctAnswers="SELECT "+CORRECTANS+" FROM "+CORRECT_ANS_TABLE+" WHERE "+QID+"="+c.getInt(0);
                Cursor answersCursor=db.rawQuery(correctAnswers,null);
                String[] answers=new String[answersCursor.getCount()];
                if (answersCursor.moveToFirst()){
                    for (int i=0;i<answersCursor.getCount();i++){
                        answers[i++]=answersCursor.getString(0);
                    }
                }

                quizQuestion.setCorrectAnswer(answers);
                //Log.v("correct",c.getString(2));
                if (c1!=null && c1.moveToFirst()) {
                    // Log.v("options", String.valueOf(c1.getString(3)));
                    String option1 = c1.getString(0);
                    // Log.v("Answers", c1.getString(0));
                    String option2 = c1.getString(1);
                    //Log.v("Answers", c1.getString(1));
                    String option3 = c1.getString(2);
                    // Log.v("Answers", c1.getString(2));
                    String option4 = c1.getString(3);
                    // Log.v("Answers", c1.getString(3));
                    String[] options = {option1, option2, option3, option4};
                    quizQuestion.setOptions(options);
                    quizQuestionList.add(quizQuestion);
                }
                while (c1.moveToNext());
            }while (c.moveToNext());

        }
        return quizQuestionList;
    }

    public boolean isEntryExist(String skill) {
        String exist="SELECT * FROM "+USERS_TABLE_CUR_LEVEL+" WHERE "+USERNAME+" = '"+LoginActivity.userName+"' AND "+CURRENT_LEVEL+" = '"+skill+"'";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery(exist,null);
        Log.v("exist",String.valueOf(c.getCount()));
        if (c.getCount()>=1)
            return true;
        else
            return false;

    }

    public void initaliseScoreTable(String skill){
        // String createTable="DELETE FROM `"+ LoginActivity.userName+"_score`";
        String createTable="INSERT INTO `"+ LoginActivity.userName+"_score` VALUES ( '"+skill+"' , 0, 0, 0 )";
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(createTable);
        Log.v("ScoreTable","table initialised");
    }
    public void initializeLevelSolvedTable(String solved, String skill, int qid) {
        //String insertIntoTable="DELETE FROM `"+LoginActivity.userName+"_"+skill+"`";
        String insertIntoTable="INSERT INTO `"+LoginActivity.userName+"_"+skill+"` values ('"+LoginActivity.userName+"','"+qid+"', '"+solved+"')";
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(insertIntoTable);
        Log.v("Initialize table","initialized");
    }
    public void insertIntoUserCurrentLevel(String skill) {
        SQLiteDatabase db=this.getWritableDatabase();
        String insertFirstLevel="INSERT INTO "+USERS_TABLE_CUR_LEVEL+" VALUES ('"+LoginActivity.userName+"', '"+skill+"')";
        db.execSQL(insertFirstLevel);
    }

    public boolean checkUnsolved(String skill) {
        SQLiteDatabase db=this.getReadableDatabase();
        String checkFalse="Select count(solved) from `"+LoginActivity.userName+"_"+skill+"` where solved='false'";
        Cursor c=db.rawQuery(checkFalse,null);
        c.moveToFirst();
        int falseCount=c.getInt(0);
        if (falseCount>0)
            return true;
        else
            return false;

    }

}
