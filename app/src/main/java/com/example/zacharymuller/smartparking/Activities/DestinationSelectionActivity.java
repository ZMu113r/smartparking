package com.example.zacharymuller.smartparking.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.example.zacharymuller.smartparking.Tasks.CoordinateFetchTask;
import com.example.zacharymuller.smartparking.R;
import com.google.gson.Gson;
import java.util.ArrayList;

public class DestinationSelectionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView lvData;

    private Destination dest = new Destination();

    private ArrayList<Garage> garages = new ArrayList<>();
    private ArrayList<Garage> closestGarages = new ArrayList<>();

    public void addClosestGarage(Garage g) {
        closestGarages.add(g);
        Log.i("ADDCLOSEST", "Called: " + closestGarages.size());
    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_destination_selection);
        // Get current user from intent
        Gson gs = new Gson();

        Bundle bundle = getIntent().getExtras();

        String currentUserJSON = bundle.getString("curruser");
        ArrayList<Garage> garages = new ArrayList<>();
        for(String name : indices) {
            garages.add(Garages.getGarage(name));
        }

        currentUser = gs.fromJson(currentUserJSON, User.class);


        // Create list view access
        lvData = findViewById(R.id.lvData);
        // Listen for a user to choose their destination
        lvData.setOnItemClickListener(this);
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
        new CoordinateFetchTask(this).execute(selectedDestination.replace(" ", "+"));

        // Create a new activity intent
    }
}
