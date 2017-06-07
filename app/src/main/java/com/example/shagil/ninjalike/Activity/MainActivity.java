package com.example.shagil.ninjalike.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.shagil.ninjalike.R;
import com.example.shagil.ninjalike.app.AppController;
import com.example.shagil.ninjalike.data.FeedItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Siddiqui on 6/2/2017.
 */

public class MainActivity extends AppCompatActivity{
    private static final String TAG=MainActivity.class.getSimpleName();
    private List<FeedItem> feedItems;
    private String URL_FEED="http://api.androidhive.info/feed/feed.json";
    TextView qno,question;
    Button answerButton;
    RadioGroup optionRG;
    RadioButton option1,option2,option3,option4;
    static Iterator<FeedItem> iterator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        feedItems=new ArrayList<FeedItem>();

        question=(TextView)findViewById(R.id.questionTV);
        answerButton=(Button)findViewById(R.id.answerButton);
        optionRG=(RadioGroup)findViewById(R.id.optionsRG);
        option1=(RadioButton)findViewById(R.id.option1RB);
        option2=(RadioButton)findViewById(R.id.option2RB);
        option3=(RadioButton)findViewById(R.id.option3RB);
        option4=(RadioButton)findViewById(R.id.option4RB);

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

    private void getNextQuestion(FeedItem next) {
        optionRG.clearCheck();
        question.setText(next.getName());
        option1.setText("YES");
        option2.setText("NO");
        option3.setText("TRUE");
        option4.setText("FALSE");
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNextQuestion(iterator.next());
            }
        });
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
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
       getNextQuestion(feedItems.iterator().next());
    }

}
