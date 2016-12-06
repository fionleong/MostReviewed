package com.example.naghmeh.mostreviewed;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchController extends AppCompatActivity {

    String searchTerm;
    String searchLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = getIntent();
        searchTerm = intent.getExtras().getString("searchTerm");
        searchLocation = intent.getExtras().getString("searchLocation");
        Toast.makeText(this, "Welcome " + searchTerm + "!" + searchLocation, Toast.LENGTH_SHORT).show();
        new AsyncTask<Void, Void, List<Business>>() {
            @Override
            protected List<Business> doInBackground(Void... params) {
                String businesses = Yelp.getYelp(SearchController.this).search(searchTerm, searchLocation);
                try {
                    return processJson(businesses);
                } catch (JSONException e) {
                    return Collections.emptyList();
                }
            }

            @Override
            protected void onPostExecute(List<Business> businesses) {
                for (int i = 0; i < businesses.size(); i++) {
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
