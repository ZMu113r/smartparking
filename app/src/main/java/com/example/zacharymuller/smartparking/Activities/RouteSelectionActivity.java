package com.example.zacharymuller.smartparking.Activities;

// Use the JSON streaming API

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.example.zacharymuller.smartparking.Adapters.CardAdapter;
import com.example.zacharymuller.smartparking.Entities.Destination;
import com.example.zacharymuller.smartparking.Entities.Garages;
import com.example.zacharymuller.smartparking.Entities.User;
import com.example.zacharymuller.smartparking.Models.CardModel;
import com.example.zacharymuller.smartparking.R;
import com.example.zacharymuller.smartparking.Remote.GeoTask;
import com.google.gson.Gson;
import com.huxq17.swipecardsview.SwipeCardsView;

import java.util.ArrayList;

public class RouteSelectionActivity extends Activity {
    // UI members
    private RecyclerView recyclerView;
    private SwipeCardsView swipeCardsView;
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


        // Get live data to display to user
        if(Garages.getClosestGarages().size() >= 1) {
            cardModelList.add(new CardModel(
                    Garages.getClosestGarages().get(0).getName(),
                    Garages.getClosestGarages().get(0).getSize(),
                    "",
                    ""));
        }
        if(Garages.getClosestGarages().size() >= 2) {
            cardModelList.add(new CardModel(
                    Garages.getClosestGarages().get(1).getName(),
                    Garages.getClosestGarages().get(1).getSize(),
                    "",
                    ""));
        }
        if(Garages.getClosestGarages().size() >= 3) {
            cardModelList.add(new CardModel(
                    Garages.getClosestGarages().get(2).getName(),
                    Garages.getClosestGarages().get(2).getSize(),
                    "",
                    ""));
        }

        // Create adapter
        cardAdapter = new CardAdapter(currentUser, Garages.getClosestGarages(), cardModelList, this);
        recyclerView.setAdapter(cardAdapter);
    }

    private String getDestinationTime(String garageName) {
        Location location = new Location("current location");
        location.setLatitude(currentUser.getLatitude());
        location.setLongitude(currentUser.getLongitude());

        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" +
                location.getLatitude() + "," + location.getLongitude() +
                "&destinations=" + "UCF garage " + garageName + "&mode=driving&language=fr-FR&avoid=tolls&key=" +
                getString(R.string.google_maps_key);
        //new GeoTask(RouteSelectionActivity.this).execute(url);

        return "";
    }

    private String getWalkingTime(String garageName) {
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" +
                garageName + "&destinations=" + "UCF garage " + dest.getName() + "&mode=walking&language=fr-FR&avoid=tolls&key=" +
                getString(R.string.google_maps_key);
        //new GeoTask(RouteSelectionActivity.this).execute(url);

        return "";
    }
}