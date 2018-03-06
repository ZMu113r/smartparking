package com.example.zacharymuller.smartparking.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.zacharymuller.smartparking.APIClient.RequestTask;
import com.example.zacharymuller.smartparking.Entities.Garages;
import com.example.zacharymuller.smartparking.Entities.User;
import com.example.zacharymuller.smartparking.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

public class HomeActivity extends AppCompatActivity {
    private int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;

    private FusedLocationProviderClient mFusedLocationClient;

    private Button destinationSelectionButton;

    private User currentUser;

    private int cnt = 0;

    public void enableEnterButton() {
        this.destinationSelectionButton.setEnabled(true);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] keys = {"A", "B", "C", "D", "H", "I", "Libra", "Test"};
        Garages.initGarages(keys);

        setContentView(R.layout.activity_home);

        new RequestTask(this.getApplicationContext(), this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "A", "B", "C", "D", "H", "I", "Libra", "Test");
        // Faster, call initialization on 5 garages at once, and execute the remaining as the other threads are freed up
        //new InitializationCallerTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        //final PollTask pollTask = new PollTask(g[7], HomeActivity.this);
        //pollTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        // Create an instance of the fused location provider client
        // in order to get users current location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Home Screen -> Destination Selection
        destinationSelectionButton = findViewById(R.id.destinationSelectionButton);

        destinationSelectionButton.setEnabled(false);

        destinationSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get last known location of currentUsers device
                if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {


                    // region ask for permission
                    if (ContextCompat.checkSelfPermission(HomeActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                                Manifest.permission.ACCESS_FINE_LOCATION)) {

                            // Show an explanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.
                        } else {
                            // No explanation needed, we can request the permission.
                            ActivityCompat.requestPermissions(HomeActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                            // MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                        return;
                    }
                    // end region
                }


                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(HomeActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location
                                // CAUTION: Can be NULL - If it's not, continue to next activity
                                if (location != null) {
                                    // Create user and move onto next activity
                                    currentUser = new User("not parked", location.getLongitude(), location.getLatitude());

                                    // Pass the current user to the next activity
                                    // via JSON string intent
                                    Gson gs = new Gson();
                                    Intent intent = new Intent(HomeActivity.this, DestinationSelectionActivity.class);

                                    String currentUserJSON = gs.toJson(currentUser);
                                    Log.i("HOMEACTIVITY", currentUserJSON);

                                    intent.putExtra("curruser", currentUserJSON);

                                    startActivity(intent);
                                }
                            }
                        });

            }
        });
    }
}
