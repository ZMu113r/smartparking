package com.example.zacharymuller.smartparking.Activities;

import android.Manifest;
import android.app.Activity;
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
import android.view.Window;
import android.widget.Button;

import com.example.zacharymuller.smartparking.APIClient.RequestTask;
import com.example.zacharymuller.smartparking.Entities.Garages;
import com.example.zacharymuller.smartparking.Entities.User;
import com.example.zacharymuller.smartparking.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

public class HomeActivity extends Activity {
    private int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;

    private FusedLocationProviderClient mFusedLocationClient;

    private Button destinationSelectionButton;

    private User currentUser;


 //   private int cnt = 0;

    public void enableEnterButton() {
        this.destinationSelectionButton.setEnabled(true);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_home);


      //  new RequestTask(this.getApplicationContext(), this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "A", "B", "C", "D", "H", "I", "Libra", "Test");
        // Faster, call initialization on 5 garages at once, and execute the remaining as the other threads are freed up
        //new InitializationCallerTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        //final PollTask pollTask = new PollTask(g[7], HomeActivity.this);
        //pollTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        // Create an instance of the fused location provider client
        // in order to get users current location

        // Home Screen -> Destination Selection
        destinationSelectionButton = findViewById(R.id.destinationSelectionButton);

        destinationSelectionButton.setEnabled(true);
        destinationSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get last known location of currentUsers device

                Intent intent = new Intent(HomeActivity.this, DestinationSelectionActivity.class);
                intent.putExtra("curruser", getIntent().getStringExtra("curruser"));
                startActivity(intent);

            }
        });
    }
}
