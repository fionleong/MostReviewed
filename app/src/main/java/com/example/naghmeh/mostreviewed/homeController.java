package com.example.naghmeh.mostreviewed;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

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
import okhttp3.RequestBody;
import okhttp3.Response;

public class homeController extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int REQUEST_MULTIPLE_LOCATION = 0;
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected double mLatitude;
    protected double mLongitude;
    private String searchTerm;
    private String searchLocation;
    private OkHttpClient client;
    MediaType mediaType;
    String url = "https://api.yelp.com/oauth2/token";
    YelpToken token;
    List<Business> businesses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_home);
        buildGoogleApiClient();
        client = new OkHttpClient();
        mediaType = MediaType.parse("application/x-www-form-urlencoded");
        post(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("onFailure", "Something went wrong");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    Log.i("TokenStr", responseStr);
                    try {
                        token = tokenJson(responseStr);
                        Log.i("token",token.access_token);
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

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void search(View view) {
        MultiAutoCompleteTextView term = (MultiAutoCompleteTextView) findViewById(R.id.searchTerm);
        String searchTerm = term.getText().toString();

        MultiAutoCompleteTextView location = (MultiAutoCompleteTextView) findViewById(R.id.searchLocation);
        searchLocation = location.getText().toString();

        Intent intent = new Intent(this, SearchController.class);
        intent.putExtra("searchTerm", searchTerm);
        intent.putExtra("searchLocation", searchLocation);
        intent.putExtra("mLatitude", mLatitude);
        intent.putExtra("mLongitude", mLongitude);
        startActivity(intent);
    }

    public void surpriseMe(View view) {
        // Testing if the other pages are working correctly
        Intent intent = new Intent(homeController.this, SearchActivity.class);
        intent.putExtra("mLatitude", mLatitude);
        intent.putExtra("mLongitude", mLongitude);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, "Need Permission", Toast.LENGTH_LONG).show();
            Log.i("onConnected", "Need permission");
            Log.i("ACCESS_FINE_LOCATION", String.valueOf(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED));
            Log.i("ACCESS_COARSE_LOCATION", String.valueOf(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED));
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_MULTIPLE_LOCATION);
            Log.i("ACCESS_FINE_LOCATION", String.valueOf(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED));
            Log.i("ACCESS_COARSE_LOCATION", String.valueOf(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED));
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.i("onConnected", "onConnected");
        if (mLastLocation != null) {
            mLatitude = mLastLocation.getLatitude();
            mLongitude = mLastLocation.getLongitude();
            Log.i("mLatitude", String.valueOf(mLatitude));
            Log.i("mLongitude", String.valueOf(mLongitude));
        } else {
            Toast.makeText(this, "No Connection Detected", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_MULTIPLE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Toast.makeText(this, "Connection suspended", Toast.LENGTH_LONG).show();
        Log.i("onConnectionSuspended", "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // An unresolvable error has occurred and a connection to Google APIs
        // could not be established. Display an error message, or handle
        // the failure silently
        // ...
        Toast.makeText(this, "Connection failed: ConnectionResult.getErrorCode() = \" + result.getErrorCode()", Toast.LENGTH_LONG).show();
        Log.i("onConnectionFailed", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    void get(){
        get("https://api.yelp.com/v3/businesses/search?term=delis&latitude=37.786882&longitude=-122.399972", new Callback() {
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
                        Log.i("try","before");
                        businesses = processJson(responseStr);
                        Log.i("business", String.valueOf(businesses.size()));
                        for (int i = 0; i < businesses.size(); i++) {
                            Log.i("Business", businesses.get(i).name);
                        }
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

    Call post(String url, Callback callback) {
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=fNujAxN1dz_J8KNL9RgVzQ&client_secret=Ozb9wCtaVtAlUajF573TNE7f3Z038PaCKCNcEfNaqw38cpU7gMLxqHcKxtyWXYow");
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("bearer", "access_token")
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "3627f452-4512-4983-ffda-96f1316eb6ae")
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    Call get(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("authorization", "Bearer " + token.access_token)
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "6b78b101-5407-cabe-aaa6-df38f2766c71")
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    YelpToken tokenJson(String jsonStuff) throws JSONException {
        JSONObject json = new JSONObject(jsonStuff);
        YelpToken token = new YelpToken(json.optString("access_token"),
                json.optString("token_type"), json.optString("expires_in"));
        return token;
    }

    List<Business> processJson(String jsonStuff) throws JSONException {
        JSONObject json = new JSONObject(jsonStuff);
        JSONArray businesses = json.getJSONArray("businesses");
        ArrayList<Business> businessObjs = new ArrayList<Business>(businesses.length());
        for (int i = 0; i < businesses.length(); i++) {
            JSONObject business = businesses.getJSONObject(i);
            businessObjs.add(new Business(business.optString("name"),
                    business.optString("rating"), business.optString("price"), business.optString("review_count"),
                    business.optString("image_url"), business.optString("url"), business.optString("latitude"),
                    business.optString("longitude")));
        }
        return businessObjs;
    }
}
