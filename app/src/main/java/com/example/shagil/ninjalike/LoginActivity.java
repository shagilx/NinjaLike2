package com.example.shagil.ninjalike;

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
    //public static final String[] levels={"C","C++","Python","Java"};
    public static final String MY_PREF_NAME="userProfile";
    private String URL_FEED="http://api.androidhive.info/feed/feed.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userNameText=(EditText)findViewById(R.id.userLoginText);
        passwordText=(EditText)findViewById(R.id.passwordLoginText);
        loginButton=(Button)findViewById(R.id.Loginbutton);
        signUpButton=(Button)findViewById(R.id.gotosignupbutton);
        sharedPreferences=getPreferences(MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("AlreadyHere",false)) {
            insertLevels();
            insertQuestions();
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            dbHelper.insertQuestions();
            editor=sharedPreferences.edit();
            editor.putBoolean("AlreadyHere",true);
            editor.apply();
        }

         signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignUpActivity.class);
                    startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=userNameText.getText().toString().toLowerCase().trim();
                String password=passwordText.getText().toString();
                DatabaseHelper dbHelper=new DatabaseHelper(getApplicationContext());
                boolean valid=dbHelper.checkCredentials(username,password);
                if (valid){
                    Intent intent=new Intent(LoginActivity.this,ChooseSkillsActivity.class);
                    userName=username;
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void insertQuestions() {
        Cache cache= AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry=cache.get(URL_FEED);
        if (entry!=null){
            try {
                String data=new String(entry.data,"UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }else {
            JsonObjectRequest jsonReq=new JsonObjectRequest(Request.Method.GET, URL_FEED, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        parseJsonFeed(response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG,"Error: "+error.getMessage());
                }
            });
            AppController.getInstance().addToRequestQueue(jsonReq);
        }
    }

    private void insertLevels() {
        String[] levels=QuizQuestions.levels;
        for (int i=0;i<levels.length;i++) {
            DatabaseHelper db=new DatabaseHelper(getApplicationContext());
            String url="drawable/"+"level_"+(i+1);

            int imageKey=getResources().getIdentifier(url,"drawable",getPackageName());
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageKey);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bitMapData = stream.toByteArray();
            db.insertLevels(levels[i],bitMapData);
        }
    }
    private void parseJsonFeed(JSONObject response) {
        List<FeedItem> feedItems=new ArrayList<>();
        try{
            JSONArray feedArray=response.getJSONArray("feed");
            for (int i=0;i<feedArray.length();i++){
                JSONObject feedObj=(JSONObject)feedArray.get(i);
                FeedItem item=new FeedItem();
                item.setId(feedObj.getInt("id"));
                item.setName(feedObj.getString("name"));
                item.setStatus(feedObj.getString("status"));

                feedItems.add(item);
                Log.v("FeedItems",feedItems.toString());
            }
            DatabaseHelper dbHelper=new DatabaseHelper(this);
           // dbHelper.insertQuestions(feedItems);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
