package com.example.zacharymuller.smartparking.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.FragmentActivity;

import com.example.zacharymuller.smartparking.R;
import com.mapbox.geojson.Point;
import com.mapbox.services.android.navigation.ui.v5.NavigationView;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions;
import com.mapbox.services.android.navigation.ui.v5.OnNavigationReadyCallback;
import com.mapbox.services.android.navigation.ui.v5.listeners.NavigationListener;

/**
 * Created by Nathaniel on 3/4/2018.
 */

public class NavigationActivity extends FragmentActivity implements NavigationListener, OnNavigationReadyCallback{
    private NavigationView navigationView;

    Point origin;
    Point destination;
    String awsPoolId;
    boolean simulateRoute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_NoActionBar);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation);
        navigationView = findViewById(R.id.navigationView);
        navigationView.onCreate(savedInstanceState);
        navigationView.getNavigationAsync(this);

        Bundle bundle = getIntent().getExtras();

        origin = (Point)bundle.get("origin");
        destination = (Point)bundle.get("destination");
        awsPoolId = bundle.getString("awsPoolId");
        simulateRoute = bundle.getBoolean("origin");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        navigationView.onLowMemory();
    }

    @Override
    public void onBackPressed() {
        // If the navigation view didn't need to do anything, call super
        if (!navigationView.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        navigationView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        navigationView.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        navigationView.onDestroy();
    }

    @Override
    public void onNavigationReady() {
        NavigationViewOptions options = NavigationViewOptions.builder()
                .navigationListener(this)
                .origin(origin)
                .destination(destination)
                .awsPoolId(awsPoolId)
                .shouldSimulateRoute(simulateRoute)
                .build();

        navigationView.startNavigation(options);
    }

    @Override
    public void onCancelNavigation() {

    }

    @Override
    public void onNavigationFinished() {
        finish();
    }

    @Override
    public void onNavigationRunning() {

    }
}
