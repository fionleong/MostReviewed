package com.example.naghmeh.mostreviewed;

import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_restaurant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new AsyncTask<Void, Void, List<Business>>() {
            @Override
            protected List<Business> doInBackground(Void... params) {
                String businesses = Yelp.getYelp(RestaurantActivity.this).getBusiness("urban-curry-san-francisco");
                try {
                    return processJson(businesses);
                } catch (JSONException e) {
                    return Collections.emptyList();
                }
            }

            @Override
            protected void onPostExecute(List<Business> businesses) {
                for (int i = 0; i < businesses.size(); i++) {
                    if(businesses.get(i).name.equals("Tacorea")) {
                        TextView name = (TextView) findViewById(R.id.restaurantName);
                        name.setText(businesses.get(i).name);
                        RatingBar rating = (RatingBar) findViewById(R.id.smallRatingBar);
                        rating.setNumStars((int)Double.parseDouble(businesses.get(i).rating));

                    }
                    Log.i("Business", businesses.get(i).name);
                }

            }
        }.execute();

    }

    List<Business> processJson(String jsonStuff) throws JSONException {
        JSONObject json = new JSONObject(jsonStuff);
        JSONArray businesses = json.getJSONArray("businesses");
        ArrayList<Business> businessObjs = new ArrayList<Business>(businesses.length());
        for (int i = 0; i < businesses.length(); i++) {
            JSONObject business = businesses.getJSONObject(i);
            businessObjs.add(new Business(business.optString("name"),
                    business.optString("rating"), business.optString("review_count"),
                    business.optString("image_url"), business.optString("display_address"),
                    business.optString("latitude"), business.optString("longitude")));
        }
        return businessObjs;
    }
}
