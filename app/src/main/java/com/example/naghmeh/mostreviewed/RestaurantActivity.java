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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RestaurantActivity extends AppCompatActivity {

    protected double mLatitude;
    protected double mLongitude;
    private String searchTerm;
    private String searchLocation;
    private boolean isSurprised;
    private String token;
    private String id;
    private OkHttpClient client;
    private Business business;
    private TextView nameTV;
    private RatingBar ratingRB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_restaurant);
        Intent intent = getIntent();
        client = new OkHttpClient();
//        searchTerm = intent.getExtras().getString("searchTerm");
//        searchLocation = intent.getExtras().getString("searchLocation");
//        mLatitude = intent.getExtras().getDouble("mLatitude");
//        mLongitude = intent.getExtras().getDouble("mLongitude");
//        isSurprised = intent.getExtras().getBoolean("isSurprised");
//        token = intent.getExtras().getString("token");
//        id = intent.getExtras().getString("id");
//        String name = intent.getExtras().getString("name");
//        int rating =(int) intent.getExtras().getDouble("rating");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        nameTV = (TextView) findViewById(R.id.restaurantName);
//        nameTV.setText(name);
//        ratingRB = (RatingBar) findViewById(R.id.smallRatingBar);
//        ratingRB.setNumStars(rating);
//        if(isSurprised) useYelpCurrentLocation();
//        else useYelpLocation();
    }

    //Old code from V2, can't use.
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

    //Old code from v2 Can't use
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

    //This method uses Yelp's business API where id is passing from the previous activity
    void searchBusiness(String id){
        String url = "https://api.yelp.com/v3/businesses/"+id;
        Log.i("url", url);
        get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("onFailure", "Something went wrong");
            }

            @Override
            //This is where we need to add code once the json string is received
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    Log.i("responseStr", responseStr);
                    try {
                        Log.i("try","before");
                        business = processBJson(responseStr); // where json string is gotten. contains 1 business info
                        Log.i("business", business.name);
                    } catch (JSONException e) {
                        Collections.emptyList();
                    }
                    // Do what you want to do with the response.
                } else {
                    Log.i("requestNotSuccesful", "Request  not succesful");
                    // Request not successful
                }
            }
        });
    }

    //This method is for v3 to use get request, no need to change.
    Call get(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("authorization", "Bearer " + token)
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "6b78b101-5407-cabe-aaa6-df38f2766c71")
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    //This is jason parser for the business once we use searchBusiness.
    Business processBJson(String jsonStuff) throws JSONException {
        JSONObject json = new JSONObject(jsonStuff);
        Business business = new Business(json.optString("name"),
                json.optString("rating"), json.optString("review_count"),
                json.optString("image_url"),json.optString("id"));
        return business;
    }

    //json parser for a list of business
    List<Business> processJson(String jsonStuff) throws JSONException {
        JSONObject json = new JSONObject(jsonStuff);
        JSONArray businesses = json.getJSONArray("businesses");
        ArrayList<Business> businessObjs = new ArrayList<Business>(businesses.length());
        for (int i = 0; i < businesses.length(); i++) {
            JSONObject business = businesses.getJSONObject(i);
            businessObjs.add(new Business(business.optString("name"),
                    business.optString("rating"), business.optString("review_count"),
                    business.optString("image_url"), business.optString("id")));
        }
        return businessObjs;
    }

    //same thing as above, different parsing
//    List<Business> processJson(String jsonStuff) throws JSONException {
//        JSONObject json = new JSONObject(jsonStuff);
//        JSONArray businesses = json.getJSONArray("businesses");
//        ArrayList<Business> businessObjs = new ArrayList<Business>(businesses.length());
//        for (int i = 0; i < businesses.length(); i++) {
//            JSONObject business = businesses.getJSONObject(i);
//            businessObjs.add(new Business(business.optString("name"),
//                    business.optString("rating"), business.optString("review_count"),
//                    business.optString("image_url"), business.optString("display_address"),
//                    business.optString("latitude"), business.optString("longitude")));
//        }
//        return businessObjs;
//    }
}
