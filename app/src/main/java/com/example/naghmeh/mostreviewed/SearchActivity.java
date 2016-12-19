package com.example.naghmeh.mostreviewed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class SearchActivity extends AppCompatActivity {
    private String searchTerm;
    private ListView mListView;
    protected double mLatitude;
    protected double mLongitude;
    private OkHttpClient client;
    private String token;
    private List<Business> businesses;
    private String id;
    private Business business;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        businesses = new ArrayList<>();

        // Intent
        Intent intent = getIntent();
        searchTerm = intent.getExtras().getString("searchTerm");
        mLatitude = intent.getExtras().getDouble("mLatitude");
        mLongitude = intent.getExtras().getDouble("mLongitude");
        token = intent.getExtras().getString("token");

        client = new OkHttpClient();
        Toast.makeText(this, "Search Term: " + searchTerm + ", " + mLatitude + " and long: " + mLongitude, Toast.LENGTH_SHORT).show();
        Log.i("SearchActivity:", "SearchTerm: " + searchTerm + ", mLat: " + mLatitude + ", mLog: " + mLongitude);
        mListView = (ListView) findViewById(R.id.searchList);
        search(searchTerm, mLatitude, mLongitude);
    }

    //Json parser for a list of business it uses a different constructor than the one above
    List<Business> processJson(String jsonStuff) throws JSONException {
        JSONObject json = new JSONObject(jsonStuff);
        JSONArray businesses = json.getJSONArray("businesses");
        ArrayList<Business> businessObjs = new ArrayList<Business>(businesses.length());
//        for (int i = 0; i < businesses.length(); i++) {
//            JSONObject business = businesses.getJSONObject(i);
//            businessObjs.add(new Business(business.optString("name"),
//                    business.optString("rating"), business.optString("review_count"),
//                    business.optString("image_url"), business.optString("id")));
//        }
        return businessObjs;
    }

    //This method searches for businesses with the current users location and given term
    void search(String term, double latitude, double Longitude) {
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
                                // Displaying all the data from the JSON onto custom listview
                                BusinessAdapter adapter = new BusinessAdapter(SearchActivity.this, businesses);
                                mListView.setAdapter(adapter);
                                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Business selectedBusiness = businesses.get(position);
                                        Intent detailIntent = new Intent(SearchActivity.this, RestaurantActivity.class);
                                        // All the details passing to RestaurantActivity
                                        detailIntent.putExtra("id", selectedBusiness.id);
                                        detailIntent.putExtra("token", token);
                                        startActivity(detailIntent);
                                    }
                                });
                            }
                        });
                    } catch (JSONException e) {Collections.emptyList();}
                } else Log.i("SearchA : search()", "Request not successful with currentLocation");
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
}
