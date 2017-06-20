package com.example.shagil.ninjalike.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.shagil.ninjalike.Activity.LoginActivity;
import com.example.shagil.ninjalike.Helper.DatabaseHelper;
import com.example.shagil.ninjalike.app.AppController;
import com.example.shagil.ninjalike.data.QuizQuestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
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
    public static final String[] skills={"C","C++","Python","Java"};
    public static final String[] levels={"Beginner","Medium","Advanced","Professional"};
    public static final String TAG=QuizQuestion.class.getSimpleName();
    private static final String URL_FEED="http://api.androidhive.info/feed/feed.json";
    static LoginActivity loginActivityy;

    private List<QuizQuestion> quizQuestions;

    public QuizQuestions() {
        this.quizQuestions = addQuizQuestions();
    }

    public List<QuizQuestion> getQuizQuestions() {
        return quizQuestions;
    }

    public List<QuizQuestion> addQuizQuestions() {
            List<QuizQuestion> quizQuestions = new ArrayList<>();
        for (int k=0;k<skills.length;k++) {
            for (int j = 0; j < levels.length; j++) {

                for (int i = 0; i < questions.length; i++) {
                    String[] answers = {
                            option1[i],
                            option2[i],
                            option3[i],
                            option4[i]};
                    QuizQuestion quizQuestion = new QuizQuestion(questions[i], correctAnswer[i], skills[k], answers, levels[j]);
                    quizQuestions.add(quizQuestion);
                }
            }
        }
      return quizQuestions;
    }
    // This method gets JSON data from an URL.
    public static void insertQuestions(LoginActivity loginActivity) {
       loginActivityy=loginActivity;
        // get Cache instance.. A class of Volley Library
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
    //This method parses the Json data received and inserts it into the local database.
    private static void parseJsonFeed(JSONObject response) {
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
            DatabaseHelper dbHelper=new DatabaseHelper(loginActivityy);
            dbHelper.insertQuestions(feedItems);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    // This method inserts skill skill to local database.

}
