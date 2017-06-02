package com.example.shagil.ninjalike;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.shagil.ninjalike.app.AppController;
import com.example.shagil.ninjalike.data.FeedItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Siddiqui on 6/2/2017.
 */

public class MainActivity extends AppCompatActivity{
    private static final String TAG=MainActivity.class.getSimpleName();
    private List<FeedItem> feedItems;
    private String URL_FEED="http://api.androidhive.info/feed/feed.json";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    private void parseJsonFeed(JSONObject response) {
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
    }

}
