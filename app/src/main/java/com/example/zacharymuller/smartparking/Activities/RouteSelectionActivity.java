package com.example.zacharymuller.smartparking.Activities;

// Use the JSON streaming API

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.example.zacharymuller.smartparking.Adapters.CardAdapter;
import com.example.zacharymuller.smartparking.Entities.Destination;
import com.example.zacharymuller.smartparking.Entities.Garages;
import com.example.zacharymuller.smartparking.Entities.User;
import com.example.zacharymuller.smartparking.Models.CardModel;
import com.example.zacharymuller.smartparking.R;
import com.google.gson.Gson;
import com.huxq17.swipecardsview.SwipeCardsView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class RouteSelectionActivity extends Activity {
    // UI members
    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;
    private ArrayList<CardModel> cardModelList = new ArrayList<>();

    // Singleton objects
    private Destination dest;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_route_selection);

        // unpack
        Gson gs = new Gson();
        final String currentUserJSON = getIntent().getStringExtra("current user");
        String destinationJSON = getIntent().getStringExtra("destination");
        String garagesJSON = getIntent().getStringExtra("garages");
        String closestGaragesJSON = getIntent().getStringExtra("closest garages");

        currentUser = gs.fromJson(currentUserJSON, User.class);
        dest = gs.fromJson(destinationJSON, Destination.class);

        // Grab object references from layout
        // Recycler view
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        double destinationLat;
        double destinationLng;
        double userLat = currentUser.getLatitude();
        double userLng = currentUser.getLongitude();
        String url;
        String destinationTime = "";
        // Get live data to display to user
        if(Garages.getClosestGarages().size() >= 1) {

            try {
                destinationLat = Garages.getClosestGarages().get(0).getLatitude();
                destinationLng = Garages.getClosestGarages().get(0).getLongitude();
                url = "https://api.mapbox.com/directions/v5/mapbox/driving/" +
                        destinationLng + "," + destinationLat + ";" + userLng + "," +
                        userLat + "?access_token=" +
                        "pk.eyJ1Ijoibjhob3VsIiwiYSI6ImNqZWQ1c2g4MzFmdHMycGxuZW03aW44ZnoifQ.tctQ1Hd69Bw3zWdqn56How";
                destinationTime = new getDestinationTime().execute(url).get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            cardModelList.add(new CardModel(
                    Garages.getClosestGarages().get(0).getName(),
                    Garages.getClosestGarages().get(0).getSize(),
                    destinationTime,
                    "Closest to your destination"));
        }
        if(Garages.getClosestGarages().size() >= 2) {

            try {
                destinationLat = Garages.getClosestGarages().get(1).getLatitude();
                destinationLng = Garages.getClosestGarages().get(1).getLongitude();
                url = "https://api.mapbox.com/directions/v5/mapbox/driving/" +
                        destinationLng + "," + destinationLat + ";" + userLng + "," +
                        userLat + "?access_token=" +
                        "pk.eyJ1Ijoibjhob3VsIiwiYSI6ImNqZWQ1c2g4MzFmdHMycGxuZW03aW44ZnoifQ.tctQ1Hd69Bw3zWdqn56How";
                destinationTime = new getDestinationTime().execute(url).get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            cardModelList.add(new CardModel(
                    Garages.getClosestGarages().get(1).getName(),
                    Garages.getClosestGarages().get(1).getSize(),
                    destinationTime,
                    "Second closest to your destination"));
        }
        if(Garages.getClosestGarages().size() >= 3) {

            try {
                destinationLat = Garages.getClosestGarages().get(2).getLatitude();
                destinationLng = Garages.getClosestGarages().get(2).getLongitude();
                url = "https://api.mapbox.com/directions/v5/mapbox/driving/" +
                        destinationLng + "," + destinationLat + ";" + userLng + "," +
                        userLat + "?access_token=" +
                        "pk.eyJ1Ijoibjhob3VsIiwiYSI6ImNqZWQ1c2g4MzFmdHMycGxuZW03aW44ZnoifQ.tctQ1Hd69Bw3zWdqn56How";
                destinationTime = new getDestinationTime().execute(url).get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            cardModelList.add(new CardModel(
                    Garages.getClosestGarages().get(2).getName(),
                    Garages.getClosestGarages().get(2).getSize(),
                    destinationTime,
                    "Closest to your current location"));
        }

        // Create adapter
        cardAdapter = new CardAdapter(currentUser, Garages.getClosestGarages(), cardModelList, this);
        recyclerView.setAdapter(cardAdapter);
    }

    private class getDestinationTime extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... params) {
            String arrivalTime = "";

            try {
                // Send HTTP GET request
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");

                // Read in response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                } in.close();

                // Convert to JSON
                JSONObject myJSON = new JSONObject(response.toString());

                // Get destination time for driving from mapbox matrix
                Double travelTimeDouble = myJSON.getJSONArray("routes").getJSONObject(0).getDouble("duration");
                travelTimeDouble /= 60.0;

                // Convert to minutes and seconds and round down
                double dec =  travelTimeDouble - Math.floor(travelTimeDouble);
                dec -= .59;
                dec += 1.0;
                travelTimeDouble += dec;
                travelTimeDouble = Math.floor(travelTimeDouble);

                // Get current time
                @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                Date date = new Date();

                String am_or_pm = "AM";
                String formatted_date = df.format(date);
                String formatted_hour = formatted_date.substring(9, 11);
                String formatted_minutes = formatted_date.substring(12, 14);

                if(Double.parseDouble(formatted_hour) > 11.0)
                    am_or_pm = "PM";
                double hour = Double.parseDouble(formatted_hour) - 12;
                double minutes = Double.parseDouble(formatted_minutes) / 100.0;
                double currentTime = hour + minutes;

                // add travel time to current time
                travelTimeDouble = currentTime + (travelTimeDouble / 100);
                // If minutes are > 59 add another hour on
                if((travelTimeDouble - Math.floor(travelTimeDouble)) > .59) {
                    travelTimeDouble -= (travelTimeDouble - Math.floor(travelTimeDouble));
                    travelTimeDouble += 1.0;
                }

                // Truncate to 2 decimal places and convert to string
                arrivalTime = new DecimalFormat("#.##").format(travelTimeDouble);
                arrivalTime = arrivalTime.substring(0, 1) + ":" + arrivalTime.substring(2);
                if(arrivalTime.length() == 3)
                    arrivalTime += "0";
                else if(arrivalTime.length() == 2)
                    arrivalTime += "00";
                // Format to Time
                arrivalTime += " " + am_or_pm;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {

                e.printStackTrace();
            }

            return arrivalTime;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }
}