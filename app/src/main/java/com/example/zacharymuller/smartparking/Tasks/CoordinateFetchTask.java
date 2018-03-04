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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Nathaniel on 3/3/2018.
 */

public class CoordinateFetchTask extends AsyncTask<String, Void, String> {
    private ArrayList<Garage> closestGarages = new ArrayList<>();
    private ArrayList<Garage> garages;

    private DestinationSelectionActivity activity;
    private Destination dest;
    private User currentUser;

    public CoordinateFetchTask(DestinationSelectionActivity activity) {
        this.activity = activity;
        this.dest = activity.getDestination();
        this.currentUser = activity.getCurrentUser();

        this.garages = new ArrayList<>();
        for(Garage g : Garages.getGarages())
            garages.add(g);
    }

    @Override
    protected String doInBackground(String... strings) {
        String response = null;
        try {
            String address = strings[0];

            HttpDataHandler http = new HttpDataHandler();

            String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s", address);

            response = http.getHttpData(url);

            //onPostExecute(response);

            return  response;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            // Get users destination
            JSONObject jsonObject = new JSONObject(s);

            String lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                    .getJSONObject("location").get("lat").toString();
            String lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                    .getJSONObject("location").get("lng").toString();

            dest.setLatitude(Double.parseDouble(lat));
            dest.setLongitude(Double.parseDouble(lng));


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
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }
}
