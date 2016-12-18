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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
    private String token;
    private OkHttpClient client;
    private Business business;
    private Double distance;
    private List<Review> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_restaurant);
        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        client = new OkHttpClient();
        // Getting data from intent
        Intent intent = getIntent();
        token = intent.getExtras().getString("token");
        String id = intent.getExtras().getString("id");
        distance = intent.getExtras().getDouble("distance");
        Log.i("token", token);
        Log.i("id", id);
        searchBusiness(id);
        searchReviews(id);
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
                        business = processBJson(responseStr); // where json string is gotten. contains 1 business info
                        runOnUiThread(new Runnable() {
                            @Override
                            //TODO: This is where we update the view
                            public void run() {
                                // Displaying all the data from the JSON onto custom listview
                                TextView restaurantName = (TextView) findViewById(R.id.restaurantName);
                                restaurantName.setText(business.name);
                                RatingBar restaurantRating = (RatingBar) findViewById(R.id.restaurantRatingBar);
                                float floatRating = Float.valueOf(business.rating);
                                restaurantRating.setRating(floatRating);
                                TextView restaurantPrice = (TextView) findViewById(R.id.restaurantPrice);
                                restaurantPrice.setText(business.price);
                                TextView restaurantReviewCount = (TextView) findViewById(R.id.restaurantReviewCount);
                                restaurantReviewCount.setText(business.review_count + " reviews");
                                TextView restaurantMiles = (TextView) findViewById(R.id.restaurantMiles);
                                restaurantMiles.setText(business.getDistance()+" miles");
                                TextView restaurantAddress = (TextView) findViewById(R.id.restaurantAddress);
                                restaurantAddress.setText(business.address1 + ", " + business.city + ", " + business.state + " " + business.zip_code);
                                TextView restaurantCuisine = (TextView) findViewById(R.id.restaurantCuisine);
                                restaurantCuisine.setText(business.cuisine);
                                //TODO: HOURS TODAY
                                TextView restaurantHours = (TextView) findViewById(R.id.restaurantHours);
                                if(business.is_closed) restaurantHours.setText("Open");
                                else restaurantHours.setText("Closed");
                                ImageView img1 = (ImageView) findViewById(R.id.img1);
                                ImageView img2 = (ImageView) findViewById(R.id.img2);
                                ImageView img3 = (ImageView) findViewById(R.id.img3);
                                Picasso.with(RestaurantActivity.this).load(business.photo1).placeholder(R.mipmap.ic_launcher).into(img1);
                                Picasso.with(RestaurantActivity.this).load(business.photo2).placeholder(R.mipmap.ic_launcher).into(img2);
                                Picasso.with(RestaurantActivity.this).load(business.photo3).placeholder(R.mipmap.ic_launcher).into(img3);
                            }
                        });
                    } catch (JSONException e) {Collections.emptyList();}
                } else Log.i("requestNotSuccessful", "Request not successful");
            }
        });
    }

    //Getting the reviews
    void searchReviews(String id){
        Log.i("searchReviews","here");
        String url = "https://api.yelp.com/v3/businesses/"+id+"/reviews";
        get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("onFailure", "Something went wrong");
            }

            @Override
            //This is where we need to add code once the json string is received
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("searchReviews","onResponse");
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    Log.i("responseStr", responseStr);
                    try {
                        reviews = processRJson(responseStr); // where json string is gotten. contains 1 business info
                        runOnUiThread(new Runnable() {
                            @Override
                            //TODO: This is where we update the view
                            public void run() {
                                // Displaying all the data from the JSON onto custom listview
                                TextView reviewDate1 = (TextView) findViewById(R.id.reviewDate1);
                                reviewDate1.setText(reviews.get(0).time_created);
                                TextView reviewDate2 = (TextView) findViewById(R.id.reviewDate2);
                                reviewDate2.setText(reviews.get(1).time_created);
                                TextView text1 = (TextView) findViewById(R.id.text1);
                                text1.setText(reviews.get(0).text);
                                TextView text2 = (TextView) findViewById(R.id.text2);
                                text2.setText(reviews.get(1).text);
                                RatingBar miniRatingBar1 = (RatingBar) findViewById(R.id.miniRatingBar1);
                                float floatRating0 = Float.valueOf(reviews.get(0).rating);
                                miniRatingBar1.setRating(floatRating0);
                                RatingBar miniRatingBar2 = (RatingBar) findViewById(R.id.miniRatingBar2);
                                float floatRating1 = Float.valueOf(reviews.get(0).rating);
                                miniRatingBar2.setRating(floatRating1);
                            }
                        });
                    } catch (JSONException e) {Collections.emptyList();}
                } else Log.i("searchReviews()", "Request not successful");
            }
        });
    }

    //This is jason parser for the business once we use searchBusiness.
    private Business processBJson(String jsonStuff) throws JSONException {
        JSONObject json = new JSONObject(jsonStuff);
        business = new Business(json.optString("id"),
                json.optString("name"), json.optString("image_url"), json.optBoolean("is_closed"),
                json.optString("url"),json.optString("price"),json.optString("rating"),
                json.optString("review_count"),json.optString("phone"));
        business.distance = distance;
        //This for accessing and getting the photos
        JSONArray photos = json.getJSONArray("photos");
        business.photo1 = photos.get(0).toString();
        business.photo2 = photos.get(1).toString();
        business.photo3 = photos.get(2).toString();
        //This for accessing categories and getting the title for the cuisine
        JSONArray categories = json.getJSONArray("categories");
        for (int j = 0; j < categories.length(); j++) {
            JSONObject category = categories.getJSONObject(j);
            business.cuisine = category.optString("title");
        }
        //This for accessing coordinates and getting the long and lat
        JSONObject coordinates = json.getJSONObject("coordinates");
        business.latitude = coordinates.optDouble("latitude");
        business.longitude = coordinates.optDouble("longitude");
        //This for accessing location and getting the address
        JSONObject location = json.getJSONObject("location");
        business.address1 = location.optString("address1");
        business.city = location.optString("city");
        business.state = location.optString("state");
        business.country = location.optString("country");
        business.zip_code = location.optString("zip_code");
        Log.i("business.toString(): ", business.toString());
        return business;
    }

    // This parses all the info from reviews API GET request
    private List<Review> processRJson(String jsonStuff) throws JSONException {
        Log.i("processRJson","start");
        JSONObject json = new JSONObject(jsonStuff);
        JSONArray reviews = json.getJSONArray("reviews");
        ArrayList<Review> reviewsObjs = new ArrayList<>(reviews.length());
        for (int i = 0; i < reviews.length(); i++) {
            JSONObject review = reviews.getJSONObject(i);
            Review r =new Review(
                    review.optString("rating"), review.optString("text"), review.optString("time_created"));
            //This for accessing user and getting the image_url and name
            JSONObject user = review.getJSONObject("user");
            r.image_url = user.optString("image_url");
            r.name = user.optString("name");
            Log.i("review: ", r.toString());
            reviewsObjs.add(r);
        }
        return reviewsObjs;
    }

    //This method is for v3 to use get request, no need to change.
    private Call get(String url, Callback callback) {
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
}
