package com.example.shagil.ninjalike.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.shagil.ninjalike.Helper.DatabaseHelper;
import com.example.shagil.ninjalike.data.QuizQuestion;
import com.example.shagil.ninjalike.data.QuizQuestions;
import com.example.shagil.ninjalike.R;
import com.example.shagil.ninjalike.app.AppController;
import com.example.shagil.ninjalike.data.FeedItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG=LoginActivity.class.getSimpleName();
    EditText userNameText,passwordText;
    Button loginButton,signUpButton;
    public static String userName;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String MY_PREF_NAME="userProfile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //initialising widgets
        userNameText=(EditText)findViewById(R.id.userLoginText);
        passwordText=(EditText)findViewById(R.id.passwordLoginText);
        loginButton=(Button)findViewById(R.id.Loginbutton);
        signUpButton=(Button)findViewById(R.id.gotosignupbutton);
        //get sharedPref to check if the activity is first time opened
        sharedPreferences=getPreferences(MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("AlreadyHere",false)) {
            //insert skill levels to local db
            insertSkills();
            //insert questions to local db
            //QuizQuestions.insertQuestions(this);
            DatabaseHelper dbHelper=new DatabaseHelper(this);
            dbHelper.insertQuestions();
            //change the value of key stored in sharedPef
            editor=sharedPreferences.edit();
            editor.putBoolean("AlreadyHere",true);
            editor.apply();
        }
        //signUpButton Click Listener to open the registration activity
         signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignUpActivity.class);
                    startActivity(intent);
            }
        });

        //Login Button Click Listener which checks the credentials entered into the textFields
        loginButton.setOnClickListener(goToSkills);

    }

    private void insertSkills() {

        String[] skills = QuizQuestions.skills;
        for (int i = 0; i < skills.length; i++) {
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            String url = "drawable/" + "level_" + (i + 1);

            int imageKey = getResources().getIdentifier(url, "drawable", getPackageName());
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageKey);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bitMapData = stream.toByteArray();
            db.insertSkills(skills[i], bitMapData);

        }
    }

    private View.OnClickListener goToSkills=new View.OnClickListener(){
        public void onClick(View v){
            String username=userNameText.getText().toString().toLowerCase().trim();
            String password=passwordText.getText().toString();
            DatabaseHelper dbHelper=new DatabaseHelper(getApplicationContext());
            boolean valid=dbHelper.checkCredentials(username,password);
            if (valid){
                //if credentials are valid, go to next activity
                Intent intent=new Intent(LoginActivity.this,ChooseSkillsActivity.class);
                userName=username;
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_SHORT).show();
            }
        }
    };




}
