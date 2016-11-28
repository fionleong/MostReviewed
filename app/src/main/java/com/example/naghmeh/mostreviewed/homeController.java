package com.example.naghmeh.mostreviewed;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class homeController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void search(View view) {
        MultiAutoCompleteTextView term = (MultiAutoCompleteTextView) findViewById(R.id.searchTerm);
        final String searchTerm= term.getText().toString();

        MultiAutoCompleteTextView location = (MultiAutoCompleteTextView) findViewById(R.id.searchLocation);
        final String searchLocation= location.getText().toString();

        Log.i("test", searchTerm+" , "+searchLocation);

        new AsyncTask<Void, Void, List<Business>>() {
            @Override
            protected List<Business> doInBackground(Void... params) {
                String businesses = Yelp.getYelp(homeController.this).search(searchTerm, searchLocation);
                try {
                    return processJson(businesses);
                } catch (JSONException e) {
                    return Collections.emptyList();
                }
            }

            @Override
            protected void onPostExecute(List<Business> businesses) {
                for(int i = 0; i < businesses.size(); i++)
                {
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
                    business.optString("rating"),business.optString("review_count"),
                    business.optString("image_url"),business.optString("display_address"),
                    business.optString("latitude"),business.optString("longitude")));
        }
        return businessObjs;
    }

    public void surpriseMe(View view) {
        // Testing if the other pages are working correctly
        Intent intent = new Intent(homeController.this, SearchActivity.class);
        startActivity(intent);

    }
}
