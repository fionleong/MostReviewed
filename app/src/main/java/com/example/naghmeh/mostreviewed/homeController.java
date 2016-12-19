package com.example.naghmeh.mostreviewed;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.Random;

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
    private static final String URL = "https://api.yelp.com/oauth2/token";
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected double mLatitude;
    protected double mLongitude;
    private OkHttpClient client;
    private MediaType mediaType;
    private YelpToken token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_home);
        buildGoogleApiClient();
        client = new OkHttpClient();
        mediaType = MediaType.parse("application/x-www-form-urlencoded");
        post(URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("onFailure", "Something went wrong getting Yelp's token");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    Log.i("TokenStr", responseStr);
                    try {
                        token = tokenJson(responseStr);
                        Log.i("token", token.access_token);
                    } catch (JSONException e) {
                        Collections.emptyList();
                    }
                } else {
                    Log.i("requestNotSuccessful", "Request  not successful");
                }
            }
        });

        Resources res = getResources();
        String[] auto_complete_cuisine = res.getStringArray(R.array.auto_complete_cuisine);
        String[] auto_complete_locations = res.getStringArray(R.array.auto_complete_location);

        // Autocomplete text field for searchLocation
        ArrayAdapter<String> cuisineAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, auto_complete_cuisine);
        AutoCompleteTextView textView1 = (AutoCompleteTextView) findViewById(R.id.searchTerm);
        textView1.setAdapter(cuisineAdapter);

        // Autocomplete text field for searchTerm
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, auto_complete_locations);
        AutoCompleteTextView textView2 = (AutoCompleteTextView) findViewById(R.id.searchLocation);
        textView2.setAdapter(locationAdapter);

    }

    //TODO: Implement the rating to be pass
    //TODO: Implement Use current location button or option or something
    public void search(View view) {
        AutoCompleteTextView term = (AutoCompleteTextView) findViewById(R.id.searchTerm);
        String searchTerm = term.getText().toString();
        AutoCompleteTextView location = (AutoCompleteTextView) findViewById(R.id.searchLocation);
        String searchLocation = location.getText().toString();
        Intent intent = new Intent(this, SearchController.class);
        intent.putExtra("searchTerm", searchTerm);
        intent.putExtra("searchLocation", searchLocation);
        intent.putExtra("mLatitude", mLatitude);
        intent.putExtra("mLongitude", mLongitude);
        intent.putExtra("token", token.access_token);
        // Right now it reads the input but it would be nice if we have a button or somthing
        if (searchLocation.toLowerCase().contains("current")) intent.putExtra("surprise", true);
        else intent.putExtra("surprise", false);
        startActivity(intent);
    }

    // For now this checks the Use Current Location with term
    public void surpriseMe(View view) {
        Intent intent = new Intent(homeController.this, SearchController.class);
        Resources res = getResources();
        String[] cuisine = res.getStringArray(R.array.cuisine_array);
        int n = new Random().nextInt(cuisine.length);
        String searchTerm = cuisine[n];
        intent.putExtra("searchTerm", searchTerm);
        intent.putExtra("mLatitude", mLatitude);
        intent.putExtra("mLongitude", mLongitude);
        intent.putExtra("token", token.access_token);
        intent.putExtra("surprise", true);
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


    // Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    // Runs when a GoogleApiClient object successfully connects.
    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // here to request the missing permissions, and then overriding
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
            Toast.makeText(this, "No Connection Detected. Please enable GPS", Toast.LENGTH_LONG).show();
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
        Toast.makeText(this, "Connection failed: ConnectionResult.getErrorCode() = \" + result.getErrorCode()", Toast.LENGTH_LONG).show();
        Log.i("onConnectionFailed", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    //This method is to get the Yelp's token
    //Only needed at the beginning
    private Call post(String url, Callback callback) {
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

    //This method parses the JSON after POST request to get the token
    //Only needed at the beginning, in order to get the token.
    private YelpToken tokenJson(String jsonStuff) throws JSONException {
        JSONObject json = new JSONObject(jsonStuff);
        token = new YelpToken(json.optString("access_token"),
                json.optString("token_type"), json.optString("expires_in"));
        return token;
    }
}
