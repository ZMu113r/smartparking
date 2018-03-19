package com.example.zacharymuller.smartparking.APIClient;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.zacharymuller.smartparking.Activities.DestinationSelectionActivity;
import com.example.zacharymuller.smartparking.Activities.HomeActivity;
import com.example.zacharymuller.smartparking.Activities.SplashScreenActivity;
import com.example.zacharymuller.smartparking.Entities.Garage;
import com.example.zacharymuller.smartparking.Entities.Garages;
import com.example.zacharymuller.smartparking.Entities.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Nathaniel on 2/28/2018.
 */

public class RequestTask extends AsyncTask<String, Void, ArrayList<Garage>> {
    private SplashScreenActivity activity;
    private Context context;

    private FusedLocationProviderClient mFusedLocationClient;
    private int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private User currentUser;

    public RequestTask(SplashScreenActivity activity) {
        this.context = activity.getApplicationContext();
        this.activity = activity;
    }

    @Override
    protected ArrayList<Garage> doInBackground(String... params) {
        Garage g = null;
        ArrayList<Garage> garageList = new ArrayList<Garage>();
        try {
            for (String garage : params) {
                g = APIClient.getAllSpots(garage);
                garageList.add(g);
                g = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return garageList;
    }

    @Override
    protected void onPostExecute(ArrayList<Garage> garageList) {
        super.onPostExecute(garageList);
        // Figure out toast stuff

        for (Garage g : garageList) {
            Garages.setGarage(g.getName(), g);
        }

        Garages.isThisNull();

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            // region ask for permission
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            0);
                    // MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
                return;
            }
            // end region
        }


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
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
                            Intent intent = new Intent(activity,DestinationSelectionActivity.class);

                            String currentUserJSON = gs.toJson(currentUser);

                            intent.putExtra("curruser", currentUserJSON);
                            activity.startActivity(intent);
                            activity.finish();
                        }

                    }
                });
    }
}
