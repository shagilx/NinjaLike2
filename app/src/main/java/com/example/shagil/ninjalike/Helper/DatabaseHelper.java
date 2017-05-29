package com.example.shagil.ninjalike.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.shagil.ninjalike.QuizQuestion;
import com.example.shagil.ninjalike.QuizQuestions;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * Created by shagil on 25/5/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG="DatabaseHelper";
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="NinjaDB";

    private static final String USERS_TABLE="users";
    private static final String USERS_TABLE_CUR_LEVEL="user_cur_level";
    private static final String USERS_TABLE_ACH_LEVEL="user_achieve_levels";
    private static final String SKILLS_TABLE="skills_table";
    private static final String QUESTIONS_TABLE="questions";
    private static final String OPTIONS_TABLE="options";


    private static final String USERNAME="username";
    private static final String PASSWORD="password";


    private static final String CURRENT_LEVEL="curr_level";

    private static final String ACHIEVED_LEVELS="achieve_level";

    private static final String SKILLS="skills";
    private static final String SKILL_IMAGE="skill_image";

    private static final String QID="qid";
    private static final String QUESTION="question";
    private static final String CORRECTANS="corr_ans";

    private static final String OPTION1="option1";
    private static final String OPTION2="option2";
    private static final String OPTION3="option3";
    private static final String OPTION4="option4";

    private static final String TABLE_USERS="CREATE TABLE IF NOT EXISTS "+USERS_TABLE+" ("+USERNAME+" TEXT PRIMARY KEY,"+PASSWORD+" TEXT"+")";
    private static final String TABLE_USER_CUR_LEVEL="CREATE TABLE IF NOT EXISTS "+USERS_TABLE_CUR_LEVEL+" ("+USERNAME+" TEXT UNIQUE,"+CURRENT_LEVEL+" TEXT,"+"FOREIGN KEY ("+USERNAME+") REFERENCES "+
            USERS_TABLE+" ("+USERNAME+"), FOREIGN KEY ("+CURRENT_LEVEL+") REFERENCES "+ SKILLS_TABLE+" ("+SKILLS+")"+")";
    private static final String TABLE_USER_ACHEIVE_LEVELS="CREATE TABLE IF NOT EXISTS "+USERS_TABLE_ACH_LEVEL+" ("+USERNAME+" TEXT,"+ACHIEVED_LEVELS+" TEXT,"+"FOREIGN KEY ("+USERNAME+") REFERENCES "+
            USERS_TABLE+"   ("+USERNAME+"), "+"FOREIGN KEY ("+ACHIEVED_LEVELS+") REFERENCES "+ SKILLS_TABLE+" ("+SKILLS+")"+")";
    private static final String TABLE_SKILLS_TABLE="CREATE TABLE IF NOT EXISTS "+SKILLS_TABLE+" ("+SKILLS+" TEXT PRIMARY KEY,"+SKILL_IMAGE+ " BLOB "+")";
    private static final String TABLE_QUESTIONS_TABLE="CREATE TABLE IF NOT EXISTS "+QUESTIONS_TABLE+" ("+QID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+QUESTION+" TEXT,"+
            CORRECTANS+" TEXT,"+SKILLS+" TEXT, "+"FOREIGN KEY ("+SKILLS+") REFERENCES "+SKILLS_TABLE+" ("+SKILLS+")"+")";
    private static final String TABLE_OPTIONS_TABLE="CREATE TABLE IF NOT EXISTS "+OPTIONS_TABLE+" ("+QID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+OPTION1+" TEXT,"+
            OPTION2+" TEXT,"+OPTION3+" TEXT,"+OPTION4+" TEXT, "+"FOREIGN KEY ("+QID+") REFERENCES "+QUESTIONS_TABLE+" ("+QID+")"+")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_USERS);
        db.execSQL(TABLE_USER_CUR_LEVEL);
        db.execSQL(TABLE_USER_ACHEIVE_LEVELS);
        db.execSQL(TABLE_SKILLS_TABLE);
        db.execSQL(TABLE_QUESTIONS_TABLE);
        db.execSQL(TABLE_OPTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_USER_CUR_LEVEL);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_USER_ACHEIVE_LEVELS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_SKILLS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_QUESTIONS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_OPTIONS_TABLE);
        onCreate(db);
    }

    public void createUser(String userName, String password) {
        SQLiteDatabase db=this.getWritableDatabase();
        String insertIntoUsers="INSERT INTO "+USERS_TABLE+" ("+USERNAME+", "+PASSWORD+") VALUES ('"+userName+"', '"+password+"')";
        //String insertIntoUserCurLevel="INSERT INTO "+USERS_TABLE_CUR_LEVEL+" ("+USERNAME+", "+CURRENT_LEVEL+") VALUES ("+
        db.execSQL(insertIntoUsers);
        db.close();
    }

    public boolean checkCredentials(String username, String password) {
        String selectUsers="SELECT "+USERNAME+", "+PASSWORD+" FROM "+USERS_TABLE+" WHERE "+USERNAME+" = '"+username+"' AND "+PASSWORD+
                " = '"+password+"'";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery(selectUsers,null);
        if (c.moveToNext()) {

            return true;
        }
        else
            return false;

    }

    public void insertLevels(String level, byte[] bitMapData) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(SKILLS,level);
        cv.put(SKILL_IMAGE,bitMapData);
        db.insert(SKILLS_TABLE,null,cv);
        db.close();
    }

    public String[] getLevels() {
        String getlevel="SELECT "+SKILLS+" FROM "+SKILLS_TABLE;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery(getlevel,null);
        String[] level=new String[c.getCount()];
        int i=0;
        if (c.moveToFirst()){
            do{
                level[i++]=c.getString(0);
            }while (c.moveToNext());
        }
        db.close();
        return level;
    }

    public void insertQuestions() {
        QuizQuestions quizQuestions=new QuizQuestions();
        List<QuizQuestion> quizQuestionsList=quizQuestions.getQuizQuestions();
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        ContentValues cv1=new ContentValues();
        for (int i=0;i<quizQuestionsList.size();i++){
            cv.put(QID,i);
            cv.put(QUESTION,quizQuestionsList.get(i).getQuestion() );
            cv.put(CORRECTANS,quizQuestionsList.get(i).getCorrectAnswer());

            cv.put(SKILLS,quizQuestionsList.get(i).getLevel());

            String[] answers=quizQuestionsList.get(i).getAnswers();
            cv1.put(QID,i);
            cv1.put(OPTION1,answers[0]);
            Log.v("option1",answers[0]);

            cv1.put(OPTION2,answers[1]);
            Log.v("option2",answers[1]);

            cv1.put(OPTION3,answers[2]);
            Log.v("option3",answers[2]);

            cv1.put(OPTION4,answers[3]);
            Log.v("option4",answers[3]);

            db.insert(QUESTIONS_TABLE,null,cv);
            db.insert(OPTIONS_TABLE,null,cv1);

        }
        db.close();
    }

    public void createTempSkillTable(CharSequence text) {
        String TEMP_QUESTION_TABLE="CREATE TABLE IF NOT EXISTS "+QUESTIONS_TABLE+" ("+QID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+QUESTION+" TEXT,"+
                CORRECTANS+" TEXT,"+SKILLS+" TEXT, "+"FOREIGN KEY ("+SKILLS+") REFERENCES "+SKILLS_TABLE+" ("+SKILLS+")"+")";
    }


    public List<QuizQuestion> getQuestionOfSkill(String skill) {
        List<QuizQuestion> quizQuestionList = new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        String getQuestion="SELECT Q."+QID+", Q."+QUESTION+", Q."+CORRECTANS+" FROM "+QUESTIONS_TABLE+ " Q WHERE Q."+SKILLS+" = '"+skill+"'";

        Cursor c=db.rawQuery(getQuestion,null);
        Log.v("Cursor",String.valueOf(c.getCount()));

        if (c.moveToFirst()){
            do {
                QuizQuestion quizQuestion=new QuizQuestion();

                String selectOptions="SELECT O."+OPTION1+", O."+OPTION2+", O."+OPTION3+", O."+OPTION4+" FROM "+OPTIONS_TABLE+ " O WHERE O."+QID+" = "+c.getInt(0);
                Cursor c1=db.rawQuery(selectOptions,null);
                quizQuestion.setQuestion(c.getString(1));
                quizQuestion.setCorrectAnswer(c.getString(2));
                Log.v("correct",c.getString(2));
                if (c1!=null && c1.moveToFirst()) {
                    Log.v("options", String.valueOf(c1.getString(3)));
                    String option1 = c1.getString(0);
                    Log.v("Answers", c1.getString(0));
                    String option2 = c1.getString(1);
                    Log.v("Answers", c1.getString(1));
                    String option3 = c1.getString(2);
                    Log.v("Answers", c1.getString(2));
                    String option4 = c1.getString(3);
                    Log.v("Answers", c1.getString(3));
                    String[] answers = {option1, option2, option3, option4};
                    quizQuestion.setAnswers(answers);
                    quizQuestionList.add(quizQuestion);
                }while (c1.moveToNext());
            }while (c.moveToNext());

        }
        return quizQuestionList;
    }

}
