package com.example.naghmeh.mostreviewed;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class homeController extends AppCompatActivity {
    private LocationManager mManager;
    private Location mCurrentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_home);
    }

    public void search(View view) {
        MultiAutoCompleteTextView term = (MultiAutoCompleteTextView) findViewById(R.id.searchTerm);
        final String searchTerm = term.getText().toString();

        MultiAutoCompleteTextView location = (MultiAutoCompleteTextView) findViewById(R.id.searchLocation);
        final String searchLocation = location.getText().toString();

        Intent intent = new Intent(this, SearchController.class);
        intent.putExtra("searchTerm", searchTerm);
        intent.putExtra("searchLocation", searchLocation);
        startActivity(intent);
    }


    public void surpriseMe(View view) {
        // Testing if the other pages are working correctly
        Intent intent = new Intent(homeController.this, SearchActivity.class);
        startActivity(intent);
    }

    private void updateDisplay() {
        TextView term = (TextView) findViewById(R.id.locationLabel);
        if (mCurrentLocation == null) {
            term.setText("Determining Your Location...");
        } else {
            term.setText(String.format("Your Location:\n%.2f, %.2f",
                    mCurrentLocation.getLatitude(),
                    mCurrentLocation.getLongitude()));
        }
    }

    private LocationListener mListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mCurrentLocation = location;
            updateDisplay();
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    };

//    @Override
//    public void onResume() {
//        super.onResume();
//        if (!mManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Location Manager");
//            builder.setMessage("We would like to use your location, "
//                    + "but GPS is currently disabled.\n"
//                    + "Would you like to change these settings now?");
//            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    //Launch settings, allowing user to make a change
//                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    startActivity(i);
//                }
//            });
//            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    //No location service, no Activity
//                    finish();
//                }
//            });
//            builder.create().show();
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mManager.removeUpdates(mListener);
//    }
}
