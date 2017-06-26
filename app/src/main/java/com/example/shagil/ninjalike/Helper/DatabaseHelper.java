package com.example.shagil.ninjalike.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.shagil.ninjalike.Activity.LoginActivity;
import com.example.shagil.ninjalike.data.FeedItem;
import com.example.shagil.ninjalike.data.QuizQuestion;
import com.example.shagil.ninjalike.data.QuizQuestions;
import com.example.shagil.ninjalike.data.ScoreCard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by shagil on 25/5/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    protected static final String LOG="DatabaseHelper";
    protected static final int DATABASE_VERSION=1;
    protected static final String DATABASE_NAME="NinjaDB";

    static final String USERS_TABLE="users";
    protected static final String USERS_TABLE_CUR_LEVEL="user_cur_level";
    protected static final String USERS_TABLE_ACH_LEVEL="user_achieve_levels";
    protected static final String SKILLS_TABLE="skills_table";
    protected static final String QUESTIONS_TABLE="questions";
    protected static final String OPTIONS_TABLE="options";
    protected static final String CORRECT_ANS_TABLE="correct_ans";
    private static final String TEMP_QUESTION_TABLE="temp_questions";


    protected static final String USERNAME="username";
    protected static final String PASSWORD="password";


    protected static final String CURRENT_LEVEL="curr_level";

    protected static final String ACHIEVED_LEVELS="achieved_level";

    protected static final String SKILLS="skills";
    protected static final String SKILL_IMAGE="skill_image";

    protected static final String QID="qid";
    protected static final String QUESTION="question";
    protected static final String CORRECTANS="corr_ans";
    protected static final String LEVEL="level";

    protected static final String OPTION1="option1";
    protected static final String OPTION2="option2";
    protected static final String OPTION3="option3";
    protected static final String OPTION4="option4";

    protected static final String TABLE_USERS="CREATE TABLE IF NOT EXISTS "+USERS_TABLE+" ("+USERNAME+" TEXT PRIMARY KEY,"+PASSWORD+" TEXT"+")";
    protected static final String TABLE_USER_CUR_LEVEL="CREATE TABLE IF NOT EXISTS "+USERS_TABLE_CUR_LEVEL+" ("+USERNAME+" TEXT ,"+CURRENT_LEVEL+" TEXT,"+"FOREIGN KEY ("+USERNAME+") REFERENCES "+
            USERS_TABLE+" ("+USERNAME+"), FOREIGN KEY ("+CURRENT_LEVEL+") REFERENCES "+ SKILLS_TABLE+" ("+SKILLS+")"+")";
    protected static final String TABLE_USER_ACHIEVED_LEVELS="CREATE TABLE IF NOT EXISTS "+USERS_TABLE_ACH_LEVEL+" ("+USERNAME+" TEXT,"+ACHIEVED_LEVELS+" TEXT,"+SKILLS+" TEXT,"+"FOREIGN KEY ("+USERNAME+") REFERENCES "+
            USERS_TABLE+"   ("+USERNAME+"), "+"FOREIGN KEY ("+SKILLS+") REFERENCES "+ SKILLS_TABLE+" ("+SKILLS+")"+")";
    protected static final String TABLE_SKILLS_TABLE="CREATE TABLE IF NOT EXISTS "+SKILLS_TABLE+" ("+SKILLS+" TEXT PRIMARY KEY,"+SKILL_IMAGE+ " BLOB "+")";
    protected static final String TABLE_QUESTIONS_TABLE="CREATE TABLE IF NOT EXISTS "+QUESTIONS_TABLE+" ("+QID+" INTEGER ,"+QUESTION+" TEXT,"+
            SKILLS+" TEXT,"+LEVEL+" TEXT, "+"FOREIGN KEY ("+SKILLS+") REFERENCES "+SKILLS_TABLE+" ("+SKILLS+")"+")";
    protected static final String TABLE_OPTIONS_TABLE="CREATE TABLE IF NOT EXISTS "+OPTIONS_TABLE+" ("+QID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+OPTION1+" TEXT,"+
            OPTION2+" TEXT,"+OPTION3+" TEXT,"+OPTION4+" TEXT, "+"FOREIGN KEY ("+QID+") REFERENCES "+QUESTIONS_TABLE+" ("+QID+")"+")";
    protected static final String TABLE_CORRECT_ANS="CREATE TABLE IF NOT EXISTS "+CORRECT_ANS_TABLE+" ("+QID+" INTEGER ,"+CORRECTANS+" TEXT, "+"FOREIGN KEY ("+QID+") REFERENCES "+QUESTIONS_TABLE+" ("+QID+")"+")";

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
        db.execSQL(TABLE_USER_ACHIEVED_LEVELS);
        db.execSQL(TABLE_SKILLS_TABLE);
        db.execSQL(TABLE_QUESTIONS_TABLE);
        db.execSQL(TABLE_OPTIONS_TABLE);
        db.execSQL(TABLE_CORRECT_ANS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_USER_CUR_LEVEL);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_USER_ACHIEVED_LEVELS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_SKILLS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_QUESTIONS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_OPTIONS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CORRECT_ANS);
        onCreate(db);
    }

    public void createUser(String userName, String password) {
        SQLiteDatabase db=this.getWritableDatabase();
        String insertIntoUsers="INSERT INTO "+USERS_TABLE+" ("+USERNAME+", "+PASSWORD+") VALUES ('"+userName+"', '"+password+"')";
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

    public void insertSkills(String skill, byte[] bitMapData) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(SKILLS,skill);
        cv.put(SKILL_IMAGE,bitMapData);
        db.insert(SKILLS_TABLE,null,cv);
        db.close();
    }

    public String[] getSkills() {
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
    public List<QuizQuestion> getQuestionOfSkill(String skill, ArrayList<Integer> incorrectAns) {
        SQLiteDatabase db=this.getReadableDatabase();
        List<QuizQuestion> questionList=new ArrayList<>();

        Iterator<Integer> iterator=incorrectAns.iterator();
        while (iterator.hasNext()) {
            String getQuestion = "SELECT Q." + QID + ", Q." + QUESTION + ", Q." + LEVEL + " FROM " + QUESTIONS_TABLE + " Q WHERE Q." + SKILLS + " = '" + skill + "' AND " + QID + " = '" + iterator.next() + "'";
            Cursor c = db.rawQuery(getQuestion, null);
            if (c.moveToFirst()) {
                do {
                    QuizQuestion quizQuestion=new QuizQuestion();
                    String selectOptions = "SELECT O." + OPTION1 + ", O." + OPTION2 + ", O." + OPTION3 + ", O." + OPTION4 + " FROM " + OPTIONS_TABLE + " O WHERE O." + QID + " = " + c.getInt(0);
                    Cursor c1 = db.rawQuery(selectOptions, null);
                    quizQuestion.setQid(c.getInt(0));
                    quizQuestion.setQuestion(c.getString(1));
                    Log.v("question", c.getString(1));
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
                    Log.v("correct", c.getString(2));
                    if (c1 != null && c1.moveToFirst()) {

                        String option1 = c1.getString(0);
                        Log.v("Answers", c1.getString(0));
                        String option2 = c1.getString(1);
                        Log.v("Answers", c1.getString(1));
                        String option3 = c1.getString(2);
                        Log.v("Answers", c1.getString(2));
                        String option4 = c1.getString(3);
                        Log.v("Answers", c1.getString(3));
                        String[] options = {option1, option2, option3, option4};
                        quizQuestion.setOptions(answers);

                    }
                    questionList.add(quizQuestion);
                } while (c.moveToNext());
            }

        }
        return questionList;
    }

    public void insertQuestions() {
        SQLiteDatabase db = this.getWritableDatabase();

            QuizQuestions quizQuestions = new QuizQuestions();
            List<QuizQuestion> quizQuestionsList = quizQuestions.getQuizQuestions();


            ContentValues cv = new ContentValues();
            ContentValues cv1 = new ContentValues();
            ContentValues correctAnswers=new ContentValues();
            for (int i = 0; i < quizQuestionsList.size(); i++) {
                cv.put(QID, i);
                cv.put(QUESTION, quizQuestionsList.get(i).getQuestion());
                Log.v("correct",quizQuestionsList.get(i).getCorrectAnswer());
                correctAnswers.put(QID,i);
                correctAnswers.put(CORRECTANS, quizQuestionsList.get(i).getCorrectAnswer());

                cv.put(SKILLS, quizQuestionsList.get(i).getSkill());
                cv.put(LEVEL,quizQuestionsList.get(i).getLevel());

                String[] options = quizQuestionsList.get(i).getOptions();
                cv1.put(QID, i);
                Log.v("qid",String.valueOf(i));
                cv1.put(OPTION1, options[0]);
                Log.v("option1", options[0]);

                cv1.put(OPTION2, options[1]);
                Log.v("option2", options[1]);

                cv1.put(OPTION3, options[2]);
                Log.v("option3", options[2]);

                cv1.put(OPTION4, options[3]);
                Log.v("option4", options[3]);

                db.insert(QUESTIONS_TABLE, null, cv);
                db.insert(OPTIONS_TABLE, null, cv1);
                db.insert(CORRECT_ANS_TABLE,null,correctAnswers);

            }

        db.close();
        }









    public ArrayList<Integer> getQidLevelSolvedTable(String skill) {
        ArrayList<Integer> falseQuestion=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        String getQID="SELECT "+QID+" FROM `"+LoginActivity.userName+"_"+skill+"` WHERE solved = 'false'";
        Cursor c=db.rawQuery(getQID,null);
        if (c.moveToFirst()){
            do {
                falseQuestion.add(c.getInt(0));
            }while (c.moveToNext());
        }
        return falseQuestion;
    }



    public void updateStatus(int qid, String skill) {
        String updateTable="UPDATE `"+LoginActivity.userName+"_"+skill+"` SET solved = 'true' where "+QID+" = '"+qid+"'";
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(updateTable);
        Log.v("InsertIntoSolved","inserted");
    }

    public void createScoreTable(String userName) {
       // String createTable="DROP TABLE `"+LoginActivity.userName+"_score`";
        String createTable="CREATE TABLE IF NOT EXISTS `"+userName+"_score` ( skill text, level text, score integer, solved integer, unsolved integer,primary key (skill, level) )";
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(createTable);
        Log.v("scoreTable","table created");

    }



    public ScoreCard getScores(String skill) {
        SQLiteDatabase db=this.getReadableDatabase();
        String getCount="SELECT COUNT(solved) from `"+LoginActivity.userName+"_"+skill+"` where solved= 'true'";

        Cursor c1=db.rawQuery(getCount,null);
        c1.moveToFirst();
        int solved=c1.getInt(0);
        c1.close();
        String allCount= "SELECT COUNT ("+QID+") FROM `"+LoginActivity.userName+"_"+skill+"` ";
        Cursor c2=db.rawQuery(allCount,null);
        c2.moveToFirst();
        int countAll=c2.getInt(0);
        int unsolved=countAll-solved;
        int score=(solved*4)-(unsolved*1);
        c2.close();
        String insertIntoTable="UPDATE `"+LoginActivity.userName+"_score` SET solved = "+solved+", unsolved = "+unsolved+", score = "+score+" where skill = '"+skill+"'";
        db.execSQL(insertIntoTable);
        String getScores="SELECT * FROM `"+LoginActivity.userName+"_score` where skill = '"+skill+"'";

        Cursor c=db.rawQuery(getScores,null);
        ScoreCard scoreCard=new ScoreCard();
        if (c.moveToFirst()){
            scoreCard.setSkill(c.getString(0));
            Log.v("String",c.getString(0));
            scoreCard.setScore(c.getInt(1));
            Log.v("String",c.getString(1));
            scoreCard.setSolved(c.getInt(2));
            Log.v("String",c.getString(2));
            scoreCard.setUnsolved(c.getInt(3));
        }
        c.close();
        return scoreCard;

    }

    public void resetMyScore(String skill) {
        SQLiteDatabase db=this.getWritableDatabase();
        String resetScore="UPDATE `"+LoginActivity.userName+"_score` SET solved = 0, unsolved = 0, score = 0 where skill = '"+skill+"'";
        db.execSQL(resetScore);
        String resetSolvedTable="UPDATE `"+LoginActivity.userName+"_"+skill+"` SET solved= 'false'";
        db.execSQL(resetSolvedTable);
    }



    public int getUserAchievedLevels(String userName) {
        SQLiteDatabase db=this.getReadableDatabase();
        String getAchievedLevels="SELECT count( DISTINCT "+ACHIEVED_LEVELS+") FROM "+USERS_TABLE_ACH_LEVEL+" WHERE "+USERNAME+" = '"+userName+"'";
        Cursor c=db.rawQuery(getAchievedLevels,null);
        c.moveToFirst();
        return c.getInt(0);

    }

    public void setAchievedLevel(String skill) {
        String setAchieved="INSERT INTO "+USERS_TABLE_ACH_LEVEL+" VALUES ('"+LoginActivity.userName+"','"+skill+"')";
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(setAchieved);
    }


    public void createTempQuestionTable(ArrayList<String> selectedStrings) {
        String dropTable="DROP TABLE IF EXISTS '"+TEMP_QUESTION_TABLE+"'";
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(dropTable);

        String createTable="CREATE TABLE IF NOT EXISTS "+TEMP_QUESTION_TABLE+"("+QID+" INTEGER ,"+QUESTION+" TEXT,"+
                SKILLS+" TEXT,"+LEVEL+" TEXT, "+"FOREIGN KEY ("+SKILLS+") REFERENCES "+SKILLS_TABLE+" ("+SKILLS+")"+")";
        db.execSQL(createTable);

        String skills=android.text.TextUtils.join("','",selectedStrings);
        Log.v("skills",skills);
        String getQuestions="SELECT "+QID+","+QUESTION+","+SKILLS+","+LEVEL+" FROM "+QUESTIONS_TABLE+" WHERE "+SKILLS+" IN ('"+skills+"')";
        ContentValues quesConentValues=new ContentValues();
        Cursor cursor=db.rawQuery(getQuestions,null);
        if (cursor.moveToFirst())
            do {
                quesConentValues.put(QID,cursor.getString(0));

                quesConentValues.put(QUESTION,cursor.getString(1));

                quesConentValues.put(SKILLS,cursor.getString(2));
                Log.v("skill",cursor.getString(2));
                quesConentValues.put(LEVEL,cursor.getString(3));
                Log.v("level",""+cursor.getString(3));

                db.insert(TEMP_QUESTION_TABLE,null,quesConentValues);
            }while (cursor.moveToNext());
    }

    public String[] getLevels(String selectedSkill) {
        String getlevel="SELECT DISTINCT "+LEVEL+" FROM "+TEMP_QUESTION_TABLE+" WHERE "+SKILLS+" = '"+selectedSkill+"'";
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
    public List<String> getTempSkills(){
        String getSkills="SELECT DISTINCT "+SKILLS+" FROM "+TEMP_QUESTION_TABLE;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery(getSkills,null);
        List<String> skills=new ArrayList<>();

        if (c.moveToFirst())
            do{
                skills.add(c.getString(0));
            }while (c.moveToNext());
        return skills;
    }

    public List<QuizQuestion> getQuestionOfLevel(String skill, String level) {
        String getQuestion="SELECT * FROM "+QUESTIONS_TABLE+" WHERE "+SKILLS+" = '"+skill+"' AND "+LEVEL+" = '"+level+"'";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor questionCursor=db.rawQuery(getQuestion,null);
        List<QuizQuestion> quizQuestionList=new ArrayList<>();

        if (questionCursor.moveToFirst())
            do{
                QuizQuestion quizQuestion=new QuizQuestion();
                quizQuestion.setQid(questionCursor.getInt(0));
                quizQuestion.setQuestion(questionCursor.getString(1));
                Log.v("ques",questionCursor.getString(1));
                quizQuestion.setSkill(questionCursor.getString(2));
                quizQuestion.setLevel(questionCursor.getString(3));
                String getCorrectAnswer="SELECT "+CORRECTANS+" FROM "+CORRECT_ANS_TABLE+" WHERE "+QID+" = "+questionCursor.getInt(0);
                Cursor correctAnsCursor=db.rawQuery(getCorrectAnswer,null);
                String[] correctAnswers=new String[correctAnsCursor.getCount()];
                Log.v("correctCursor",String.valueOf(correctAnsCursor.getCount()));
                int i=0;
                if (correctAnsCursor.moveToFirst())

                    do{
                        correctAnswers[i++]=correctAnsCursor.getString(0);
                    }while (correctAnsCursor.moveToNext());
                quizQuestion.setCorrectAnswer(correctAnswers);
                String getOptions="SELECT * FROM "+ OPTIONS_TABLE+" WHERE "+QID+" = "+questionCursor.getInt(0);
                Cursor OptionsCursor=db.rawQuery(getOptions,null);
                if (OptionsCursor.moveToFirst()){
                    String[] options = {OptionsCursor.getString(1), OptionsCursor.getString(2), OptionsCursor.getString(3), OptionsCursor.getString(4)};
                    quizQuestion.setOptions(options);
                }
                quizQuestionList.add(quizQuestion);
            }while (questionCursor.moveToNext());
        Log.v("size",String.valueOf(quizQuestionList.size()));
        return quizQuestionList;
    }
}
