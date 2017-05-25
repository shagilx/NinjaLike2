package com.example.shagil.ninjalike.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    private static final String USERNAME="username";
    private static final String PASSWORD="password";


    private static final String CURRENT_LEVEL="curr_level";

    private static final String ACHIEVED_LEVELS="achieve_level";

    private static final String SKILLS="skills";
    private static final String SKILL_IMAGE="skill_image";

    private static final String TABLE_USERS="CREATE TABLE IF NOT EXISTS "+USERS_TABLE+" ("+USERNAME+" TEXT PRIMARY KEY,"+PASSWORD+" TEXT"+")";
    private static final String TABLE_USER_CUR_LEVEL="CREATE TABLE IF NOT EXISTS "+USERS_TABLE_CUR_LEVEL+" ("+USERNAME+" TEXT,"+CURRENT_LEVEL+" TEXT,"+"FOREIGN KEY ("+USERNAME+") REFERENCES "+
            TABLE_USERS+" ("+USERNAME+"), FOREIGN KEY ("+CURRENT_LEVEL+") REFERENCES "+ SKILLS_TABLE+" ("+SKILLS+")"+")";
    private static final String TABLE_USER_ACHEIVE_LEVELS="CREATE TABLE IF NOT EXISTS "+USERS_TABLE_ACH_LEVEL+" ("+USERNAME+" TEXT,"+ACHIEVED_LEVELS+" TEXT,"+"FOREIGN KEY ("+USERNAME+") REFERENCES "+
            TABLE_USERS+"   ("+USERNAME+"), "+"FOREIGN KEY ("+ACHIEVED_LEVELS+") REFERENCES "+ SKILLS_TABLE+" ("+SKILLS+")"+")";
    private static final String TABLE_SKILLS_TABLE="CREATE TABLE IF NOT EXISTS "+SKILLS_TABLE+" ("+SKILLS+" TEXT PRIMARY KEY,"+SKILL_IMAGE+ "BLOB "+")";




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
