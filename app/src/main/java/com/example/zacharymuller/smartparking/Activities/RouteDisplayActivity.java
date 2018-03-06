package com.example.zacharymuller.smartparking.Activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.zacharymuller.smartparking.Entities.Garage;
import com.example.zacharymuller.smartparking.Entities.Garages;
import com.example.zacharymuller.smartparking.Entities.User;
import com.example.zacharymuller.smartparking.R;

import com.google.gson.Gson;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;

import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;
import com.mapbox.services.android.telemetry.location.LostLocationEngine;
import com.mapbox.services.android.telemetry.permissions.PermissionsListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;

import android.view.View;
import android.widget.Button;

import java.util.List;

public class RouteDisplayActivity extends FragmentActivity implements LocationEngineListener, PermissionsListener, OnMapReadyCallback {
    private MapView mapView;
    private User currentUser;
    private Garage destination;

    private MapboxMap mapboxMap;

    private PermissionsManager permissionsManager;
    private LocationLayerPlugin locationPlugin;
    private LocationEngine locationEngine;
    private Location originLocation;

    private Point originPosition;
    private Point destinationPosition;
    private DirectionsRoute currentRoute;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;

    private Button button;

    public static boolean navigationCalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            Mapbox.getInstance(this, "pk.eyJ1Ijoibjhob3VsIiwiYSI6ImNqZWQ1c2g4MzFmdHMycGxuZW03aW44ZnoifQ.tctQ1Hd69Bw3zWdqn56How");
            setContentView(R.layout.activity_route_display);
            mapView = (MapView) findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);

            button = findViewById(R.id.startNavButton);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Point origin = originPosition;
                    Point destination = destinationPosition;

                    // Pass in your Amazon Polly pool id for speech synthesis using Amazon Polly
                    // Set to null to use the default Android speech synthesizer
                    String awsPoolId = null;

                    boolean simulateRoute = true;

                    Intent intent = new Intent(RouteDisplayActivity.this, NavigationActivity.class);
                    intent.putExtra("origin", origin);
                    intent.putExtra("destination", destination);
                    intent.putExtra("awsPoolId", awsPoolId);
                    intent.putExtra("simulateRoute", simulateRoute);

                    startActivity(intent);
                }
            });

            Gson gs = new Gson();

            String currentUserJSON = getIntent().getStringExtra("current user");
            String closestGaragesJSON = getIntent().getStringExtra("closest garages");
            String chosenGarageJSON = getIntent().getStringExtra("chosen garage");

            currentUser = gs.fromJson(currentUserJSON, User.class);
            String chosenGarage = gs.fromJson(chosenGarageJSON, String.class);

            for (Garage g : Garages.getClosestGarages()) {
                if (g.getName().compareTo(chosenGarage) == 0)
                    destination = g;
            }

            mapView.getMapAsync(this);
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationPlugin() {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Create an instance of LOST location engine
            initializeLocationEngine();

            locationPlugin = new LocationLayerPlugin(mapView, mapboxMap, locationEngine);
            locationPlugin.setLocationLayerEnabled(LocationLayerMode.TRACKING);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressWarnings( {"MissingPermission"})
    private void initializeLocationEngine() {
        locationEngine = new LostLocationEngine(RouteDisplayActivity.this);
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null) {
            originLocation = lastLocation;
            setCameraPosition(lastLocation);
        } else {
            locationEngine.addLocationEngineListener(this);
        }
    }

    private void setCameraPosition(Location location) {
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 13));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationPlugin();
        } else {
            finish();
        }
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            originLocation = location;
            locationEngine.removeLocationEngineListener(this);
        }
    }




    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        enableLocationPlugin();

        final LatLng dest = new LatLng(destination.getLatitude(), destination.getLongitude());
        final LatLng source = new LatLng(currentUser.getLatitude(), currentUser.getLongitude());
        //mapboxMap.addMarker(new MarkerOptions().position(source).title("You are here"));
        mapboxMap.addMarker(new MarkerOptions().position(dest).title(destination.getName()));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(source);
        builder.include(dest);

        LatLngBounds bounds = builder.build();

//                mapboxMap.addMarker(new MarkerOptions()
//                .position(new com.mapbox.mapboxsdk.geometry.LatLng(destination.getLatitude(), destination.getLongitude())).title(destination.getName()).snippet(""));
//                mapboxMap.addMarker(new MarkerOptions()
//                        .position(new com.mapbox.mapboxsdk.geometry.LatLng(currentUser.getLatitude(), currentUser.getLongitude())).title("You are here").snippet(""));

        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, (int)(getResources().getDisplayMetrics().widthPixels * .2)));

        destinationPosition = Point.fromLngLat(dest.getLongitude(), dest.getLatitude());
        originPosition = Point.fromLngLat(source.getLongitude(), source.getLatitude());
        getRoute(originPosition, destinationPosition);
    }

    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder()
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        // You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        Log.i(TAG, response.toString());
                        currentRoute = response.body().routes().get(0);

                        // Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);

                        button.setEnabled(true);
                        button.setBackgroundResource(R.color.mapboxBlue);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        if (locationEngine != null) {
            locationEngine.requestLocationUpdates();
        }
        if (locationPlugin != null) {
            locationPlugin.onStart();
        }
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates();
        }
        if (locationPlugin != null) {
            locationPlugin.onStop();
        }
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (locationEngine != null) {
            locationEngine.deactivate();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(navigationCalled) {
            Intent intent = new Intent(this, SpotVisualizerActivity.class);
            intent.putExtra("chosengarage", destination.getName());

            navigationCalled = false;
            startActivity(intent);
            finish();
        } else {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}