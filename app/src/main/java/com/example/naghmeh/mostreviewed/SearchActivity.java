package com.example.naghmeh.mostreviewed;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {

    private String searchTerm;
    private ListView mListView;
    protected double mLatitude;
    protected double mLongitude;
    private OkHttpClient client;
    private MediaType mediaType;
    private String url = "https://api.yelp.com/oauth2/token";
    private String token;
    private List<Business> businesses;
    private String id;
    private Business business;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Most Reviewed");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Intent
        Intent intent = getIntent();
        searchTerm = intent.getExtras().getString("searchTerm");
        mLatitude = intent.getExtras().getDouble("mLatitude");
        mLongitude = intent.getExtras().getDouble("mLongitude");

        client = new OkHttpClient();
        mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Toast.makeText(this, "Searching for mexican cuisine with lat: " + mLatitude + " and long: " + mLongitude, Toast.LENGTH_SHORT).show();
//        This code is for v2
//        new AsyncTask<Void, Void, List<Business>>() {
//            @Override
//            protected List<Business> doInBackground(Void... params) {
//                String businesses = Yelp.getYelp(SearchActivity.this).search("mexican", mLatitude, mLongitude);
//                try {
//                    return processJson(businesses);
//                } catch (JSONException e) {
//                    return Collections.emptyList();
//                }
//            }
//
//            @Override
//            protected void onPostExecute(List<Business> businesses) {
//                for (int i = 0; i < businesses.size(); i++) {
//                    Log.i("Business", businesses.get(i).name);
//                }
//
//            }
//        }.execute();

        search(searchTerm, mLatitude, mLongitude);

        mListView = (ListView) findViewById(R.id.searchList);

        // Displaying all the data from the JSON onto custom listview
        BusinessAdapter adapter = new BusinessAdapter(SearchActivity.this, businesses);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Business selectedBusiness = businesses.get(position);

                Intent detailIntent = new Intent(SearchActivity.this, RestaurantActivity.class);

                // All the details passing to RestaurantActivity
                detailIntent.putExtra("name", selectedBusiness.name);
                detailIntent.putExtra("rating", selectedBusiness.rating);
//                        detailIntent.putExtra("price", selectedBusiness.price);
                detailIntent.putExtra("review_count", selectedBusiness.review_count);
                detailIntent.putExtra("url", selectedBusiness.url);

                startActivity(detailIntent);
            }

        });

    }

//    //Json parser for a list of business it uses a different constructor
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

    //Json parser for a list of business it uses a different constructor than the one above
    List<Business> processJson2(String jsonStuff) throws JSONException {
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

    //This method is needed in order to use a GET request from yelp
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

    //This method searches for businesses with the current users location and given term
    void search(String term, double latitude, double Longitude) {
        String url = "https://api.yelp.com/v3/businesses/search?term=" + term + "&latitude=" + String.valueOf(mLatitude)
                + "&longitude=" + String.valueOf(mLongitude);
        Log.i("url", url);
        get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("onFailure", "Something went wrong");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    Log.i("responseStr", responseStr);
                    try {
                        Log.i("try", "before");
                        businesses = processJson2(responseStr);
                        Log.i("business", String.valueOf(businesses.size()));
//                        for (int i = 0; i < businesses.size(); i++) {
//                            Log.i("Business", businesses.get(i).name);
//                            if (businesses.get(i).name.equals("La Victoria Taqueria"))
//                                id = businesses.get(i).id;
//                        }

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

    //This method searches for business info with a given id
    void searchBusiness(String id) {
        String url = "https://api.yelp.com/v3/businesses/" + id;
        Log.i("url", url);
        get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("onFailure", "Something went wrong");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    Log.i("responseStr", responseStr);
                    try {
                        Log.i("try", "before");
                        business = processBJson(responseStr);
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

    //json parser for business info
    Business processBJson(String jsonStuff) throws JSONException {
        JSONObject json = new JSONObject(jsonStuff);
        Business business = new Business(json.optString("name"),
                json.optString("rating"), json.optString("review_count"),
                json.optString("image_url"), json.optString("id"));
        return business;
    }

}
