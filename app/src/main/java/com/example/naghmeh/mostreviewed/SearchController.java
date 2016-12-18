package com.example.naghmeh.mostreviewed;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchController extends AppCompatActivity {
    private ListView mListView;
    protected double mLatitude;
    protected double mLongitude;
    private OkHttpClient client;
    private String token;
    private List<Business> businesses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        businesses = new ArrayList<>();
        client = new OkHttpClient();
        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Most Reviewed");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Intent
        Intent intent = getIntent();
        String searchTerm = intent.getExtras().getString("searchTerm");
        String searchLocation = intent.getExtras().getString("searchLocation");
        mLatitude = intent.getExtras().getDouble("mLatitude");
        mLongitude = intent.getExtras().getDouble("mLongitude");
        token = intent.getExtras().getString("token");
        Boolean surprise = intent.getExtras().getBoolean("surprise");
        mListView = (ListView) findViewById(R.id.searchList);
        //Searches using location given from previous homeController
        if(!surprise){
            Toast.makeText(this, "Searching for: " + searchTerm + " in " + searchLocation, Toast.LENGTH_SHORT).show();
            search(searchTerm, searchLocation);
        }
        //Searches using Current Location
        else{
            Toast.makeText(this, "Searching for: " + searchTerm + " with current location", Toast.LENGTH_SHORT).show();
            search(searchTerm, mLatitude, mLongitude);
        }

    }

    //This method searches for businesses with the current users location and given term
    private void search(String term, String searchLocation) {
        String url = "https://api.yelp.com/v3/businesses/search?term=" + term + "&location=" + searchLocation;
        Log.i("url", url);
        Log.i("token: ", "" + token);
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
                        businesses = processJson(responseStr);
                        Log.i("business", String.valueOf(businesses.size()));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Displaying all the data from the JSON onto custom list view
                                BusinessAdapter adapter = new BusinessAdapter(SearchController.this, businesses);
                                mListView.setAdapter(adapter);
                                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Business selectedBusiness = businesses.get(position);
                                        Intent detailIntent = new Intent(SearchController.this, RestaurantActivity.class);
                                        // All the details passing to RestaurantActivity
                                        detailIntent.putExtra("id", selectedBusiness.id);
                                        Log.i("id", selectedBusiness.id);
                                        detailIntent.putExtra("token", token);
                                        detailIntent.putExtra("distance", selectedBusiness.distance);
                                        Log.i("distance", String.valueOf(selectedBusiness.distance));
                                        detailIntent.putExtra("cuisine", selectedBusiness.cuisine);
                                        Log.i("cuisine", String.valueOf(selectedBusiness.cuisine));
                                        startActivity(detailIntent);
                                    }
                                });
                            }
                        });
                    } catch (JSONException e) {Collections.emptyList();}
                } else Log.i("Search: search()", "Request not successful with given location");
            }
        });
    }

    //This method searches for businesses with the current users location and given term
    private void search(String term, double latitude, double Longitude) {
        String url = "https://api.yelp.com/v3/businesses/search?term=" + term + "&latitude=" + String.valueOf(latitude)
                + "&longitude=" + String.valueOf(Longitude);
        Log.i("url", url);
        Log.i("token: ", "" + token);
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
                        businesses = processJson(responseStr);
                        Log.i("business", String.valueOf(businesses.size()));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Displaying all the data from the JSON onto custom list view
                                BusinessAdapter adapter = new BusinessAdapter(SearchController.this, businesses);
                                mListView.setAdapter(adapter);
                                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Business selectedBusiness = businesses.get(position);
                                        Intent detailIntent = new Intent(SearchController.this, RestaurantActivity.class);
                                        // All the details passing to RestaurantActivity
                                        detailIntent.putExtra("id", selectedBusiness.id);
                                        Log.i("id", selectedBusiness.id);
                                        detailIntent.putExtra("token", token);
                                        detailIntent.putExtra("distance", selectedBusiness.distance);
                                        Log.i("distance", String.valueOf(selectedBusiness.distance));
                                        detailIntent.putExtra("cuisine", selectedBusiness.cuisine);
                                        Log.i("cuisine", String.valueOf(selectedBusiness.cuisine));
                                        startActivity(detailIntent);
                                    }
                                });
                            }
                        });
                    } catch (JSONException e){Collections.emptyList();}
                } else Log.i("SearchA : search()", "Request not successful with current location");
            }
        });
    }

    //This method is needed in order to use a GET request from yelp
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

    // This parses all the info from business_search API GET request
    private List<Business> processJson(String jsonStuff) throws JSONException {
        JSONObject json = new JSONObject(jsonStuff);
        JSONArray businesses = json.getJSONArray("businesses");
        ArrayList<Business> businessObjs = new ArrayList<>(businesses.length());
        for (int i = 0; i < businesses.length(); i++) {
            JSONObject business = businesses.getJSONObject(i);
            Business b =new Business(
                    business.optString("rating"), business.optString("price"), business.optString("phone"),
                    business.optString("id"),business.optBoolean("isClosed"), business.optString("review_count"),
                    business.optString("name"), business.optString("url"), business.optString("image_url"),
                    business.optDouble("distance"));
            //This for accessing categories and getting the title for the cuisine
            JSONArray categories = business.getJSONArray("categories");
            for (int j = 0; j < categories.length(); j++) {
                JSONObject category = categories.getJSONObject(j);
                b.cuisine = category.optString("title");
            }
            //This for accessing coordinates and getting the long and lat
            JSONObject coordinates = business.getJSONObject("coordinates");
            b.latitude = coordinates.optDouble("latitude");
            b.longitude = coordinates.optDouble("longitude");
            //This for accessing location and getting the address
            JSONObject location = business.getJSONObject("location");
            b.address1 = location.optString("address1");
            b.city = location.optString("city");
            b.state = location.optString("state");
            b.country = location.optString("country");
            b.zip_code = location.optString("zip_code");
            Log.i("business: ", b.toString());
            businessObjs.add(b);
        }
        return businessObjs;
    }
}
