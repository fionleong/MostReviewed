package com.example.naghmeh.mostreviewed;

import android.content.Intent;
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

    protected double mLatitude;
    protected double mLongitude;
    private String searchTerm;
    private String searchLocation;
    private boolean isSurprised;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_restaurant);
        Intent intent = getIntent();
//        searchTerm = intent.getExtras().getString("searchTerm");
//        searchLocation = intent.getExtras().getString("searchLocation");
//        mLatitude = intent.getExtras().getDouble("mLatitude");
//        mLongitude = intent.getExtras().getDouble("mLongitude");
//        isSurprised = intent.getExtras().getBoolean("isSurprised");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        if(isSurprised) useYelpCurrentLocation();
//        else useYelpLocation();
    }

    public void useYelpLocation(){
        new AsyncTask<Void, Void, List<Business>>() {
            @Override
            protected List<Business> doInBackground(Void... params) {
                String businesses = Yelp.getYelp(RestaurantActivity.this).search(searchTerm, searchLocation);
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
                        RatingBar rating = (RatingBar) findViewById(R.id.restaurantRatingBar);
                        rating.setNumStars((int)Double.parseDouble(businesses.get(i).rating));

                    }
                    Log.i("Business", businesses.get(i).name);
                }

            }
        }.execute();
    }

    public void useYelpCurrentLocation(){
        new AsyncTask<Void, Void, List<Business>>() {
            @Override
            protected List<Business> doInBackground(Void... params) {
                String businesses = Yelp.getYelp(RestaurantActivity.this).search(searchTerm, mLatitude, mLongitude);
                try {
                    return processJson(businesses);
                } catch (JSONException e) {
                    return Collections.emptyList();
                }
            }

            @Override
            protected void onPostExecute(List<Business> businesses) {
                for (int i = 0; i < businesses.size(); i++) {
                    if(businesses.get(i).name.equals("La Victoria Taqueria")) {
                        TextView name = (TextView) findViewById(R.id.restaurantName);
                        name.setText(businesses.get(i).name);
                        RatingBar rating = (RatingBar) findViewById(R.id.restaurantRatingBar);
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
