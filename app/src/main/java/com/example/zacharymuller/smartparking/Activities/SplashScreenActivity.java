package com.example.zacharymuller.smartparking.Activities;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.example.zacharymuller.smartparking.APIClient.RequestTask;
import com.example.zacharymuller.smartparking.Entities.Garages;
import com.example.zacharymuller.smartparking.Entities.User;
import com.example.zacharymuller.smartparking.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

public class SplashScreenActivity extends Activity {

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        String[] keys = {"A", "B", "C", "D", "H", "I", "Libra", "Test"};
        Garages.initGarages(keys);

        new RequestTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "A", "B", "C", "D", "H", "I", "Libra", "Test");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }
}
