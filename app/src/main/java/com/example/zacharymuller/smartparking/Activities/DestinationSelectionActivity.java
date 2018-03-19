package com.example.zacharymuller.smartparking.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zacharymuller.smartparking.Entities.Destination;
import com.example.zacharymuller.smartparking.Entities.Garage;
import com.example.zacharymuller.smartparking.Entities.Garages;
import com.example.zacharymuller.smartparking.Entities.User;

import com.example.zacharymuller.smartparking.Handlers.DestinationSortingHandler;
import com.example.zacharymuller.smartparking.Handlers.UserLocationSortingHandler;
import com.example.zacharymuller.smartparking.Tasks.CoordinateFetchTask;
import com.example.zacharymuller.smartparking.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.services.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.services.api.geocoding.v5.models.GeocodingResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DestinationSelectionActivity extends Activity implements AdapterView.OnItemClickListener {

    private ListView lvData;

    private Destination dest = new Destination();

    private ArrayList<Garage> garages = new ArrayList<>();
    private ArrayList<Garage> closestGarages = new ArrayList<>();

    private FusedLocationProviderClient locationClient;
    private LatLng latLng = null;

    private User currentUser;

    private String pwdDecrypted = "";
    private String[] indices = {"A", "B", "C", "D", "H", "I", "Libra", "Test"};

    private static int OPEN = 0;
    private static int CLOSED = 1;

    public Destination getDestination() {
        return this.dest;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_destination_selection);
        // Get current user from intent
        Gson gs = new Gson();

        Bundle bundle = getIntent().getExtras();

        String currentUserJSON = bundle.getString("curruser");


        currentUser = gs.fromJson(currentUserJSON, User.class);


        // Create list view access
        lvData = findViewById(R.id.lvData);
        // Listen for a user to choose their destination
        lvData.setOnItemClickListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Garages.resetClosestGarages();
        garages = new ArrayList<>();
        closestGarages = new ArrayList<>();
        latLng = null;
        dest.setName("");
    }

    // General method to show that moving between screens works
    public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
        Garages.resetClosestGarages();

        // get name from clicked item
        String selectedDestination = lvData.getItemAtPosition(position).toString();
        dest.setName(selectedDestination);

        // Add Orlando, FL specification to location
        selectedDestination = "UCF " + selectedDestination + " Orlando, FL 32816";

        // Spin up an async task to...
        // Retrieve coordinates and put them into Destination object
        // in the background
        //new CoordinateFetchTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, selectedDestination);

        com.mapbox.services.api.geocoding.v5.MapboxGeocoding mapboxGeocoding = new com.mapbox.services.api.geocoding.v5.MapboxGeocoding.Builder()
                .setAccessToken("pk.eyJ1Ijoibjhob3VsIiwiYSI6ImNqZWQ1c2g4MzFmdHMycGxuZW03aW44ZnoifQ.tctQ1Hd69Bw3zWdqn56How")
                .setLocation(selectedDestination)
                .build();

        mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                List<CarmenFeature> results = response.body().getFeatures();
                if (results.size() > 0) {
                    // Log the first results position.
                    latLng = new LatLng(results.get(0).asPosition().getLatitude(), results.get(0).asPosition().getLongitude());
                    dest.setLatitude(latLng.latitude);
                    dest.setLongitude(latLng.longitude);

                    garages = new ArrayList<>();

                    for(Garage g: Garages.getGarages())
                    {
                        garages.add(g);
                    }


                    for (Garage g : garages) {
                        g.setDestinationDistance(Math.hypot((g.getLatitude() - dest.getLatitude()), (g.getLongitude() - dest.getLongitude())));
                        g.setUserDistance(Math.hypot((currentUser.getLatitude() - g.getLatitude()), (currentUser.getLongitude() - g.getLongitude())));
                    }


                    // sort destinationDistance
                    Collections.sort(garages, new DestinationSortingHandler());
                    // Get 1st and 2nd items in destinationDistances as options for user
                    closestGarages.add(garages.get(0));
                    if(!closestGarages.contains(garages.get(1))) {
                        closestGarages.add(garages.get(1));
                    }

                    // sort userDistance
                    Collections.sort(garages, new UserLocationSortingHandler());
                    // Get 1st item from userLocationDistances as option for user
                    if(!closestGarages.contains(garages.get(0))) {
                        closestGarages.add(garages.get(0));
                    }

                    for(Garage g : closestGarages)
                        Garages.addClosestGarage(g);

                    Intent intent = new Intent(DestinationSelectionActivity.this, RouteSelectionActivity.class);

                    // Convert objects to JSON strings
                    Gson gs = new Gson();
                    String currentUserJSON = gs.toJson(currentUser);
                    String destinationJSON = gs.toJson(dest);
                    String garagesJSON = gs.toJson(garages);
                    String closestGaragesJSON = gs.toJson(closestGarages);

                    Log.i("COORDFETCH", closestGarages.size() + "");

                    Log.i("COORDFETCH", currentUserJSON);
                    Log.i("COORDFETCH", destinationJSON);
                    Log.i("COORDFETCH", garagesJSON);
                    Log.i("COORDFETCH", closestGaragesJSON);

                    // Pass the JSON strings to the next activity through intent
                    intent.putExtra("current user", currentUserJSON);
                    intent.putExtra("destination", destinationJSON);

                    startActivity(intent);
                    Log.i("CoordFetch", latLng.latitude + ", " + latLng.longitude);
                } else {
                    // No result for your request were found.
                    Log.d("CoordFetch", "onResponse: No result found");
                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                Log.i("CoordFetch", "Failed to get coordinatates");
                throwable.printStackTrace();
            }
        });

        // Create a new activity intent
    }
}
