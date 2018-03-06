package com.example.zacharymuller.smartparking.Tasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.zacharymuller.smartparking.Activities.DestinationSelectionActivity;
import com.example.zacharymuller.smartparking.Activities.RouteSelectionActivity;
import com.example.zacharymuller.smartparking.Entities.Destination;
import com.example.zacharymuller.smartparking.Entities.Garage;
import com.example.zacharymuller.smartparking.Entities.Garages;
import com.example.zacharymuller.smartparking.Entities.User;
import com.example.zacharymuller.smartparking.Handlers.DestinationSortingHandler;
import com.example.zacharymuller.smartparking.Handlers.HttpDataHandler;
import com.example.zacharymuller.smartparking.Handlers.UserLocationSortingHandler;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.mapbox.services.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.services.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.services.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.services.commons.models.Position;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nathaniel on 3/3/2018.
 */

public class CoordinateFetchTask extends AsyncTask<String, Void, LatLng> {
    private ArrayList<Garage> closestGarages = new ArrayList<>();
    private ArrayList<Garage> garages;

    private DestinationSelectionActivity activity;
    private Destination dest;
    private User currentUser;

    private LatLng latLong = null;

    public CoordinateFetchTask(DestinationSelectionActivity activity) {
        this.activity = activity;
        this.dest = activity.getDestination();
        this.currentUser = activity.getCurrentUser();

        this.garages = new ArrayList<>();
        for(Garage g : Garages.getGarages())
            garages.add(g);
    }

    @Override
    protected LatLng doInBackground(String... strings) {
            String address = strings[0];
            Log.i("CoordFetch", address);



        return  latLong;
    }

    @Override
    protected void onPostExecute(LatLng pos) {
        super.onPostExecute(pos);
            dest.setLatitude(pos.latitude);
            dest.setLongitude(pos.longitude);


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

            Intent intent = new Intent(activity.getBaseContext(), RouteSelectionActivity.class);

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

            activity.startActivity(intent);
    }
}
