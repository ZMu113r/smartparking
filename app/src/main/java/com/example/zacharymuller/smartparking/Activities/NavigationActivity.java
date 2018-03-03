package com.example.zacharymuller.smartparking.Activities;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.example.zacharymuller.smartparking.Entities.GarageEntity;
import com.example.zacharymuller.smartparking.Entities.User;
import com.example.zacharymuller.smartparking.R;
import com.example.zacharymuller.smartparking.Remote.Common;
import com.example.zacharymuller.smartparking.Remote.IGoogleApi;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private ArrayList<GarageEntity> closestGarages;

    private String chosenGarage;

    private GarageEntity destinationGarage;

    private User currentUser;

    private SupportMapFragment mapFragment;

    private List<LatLng> polylineList;

    private Marker marker;

    private float v;

    private Handler handler;

    private double lat, lng;

    private LatLng startPosition, endPosition;

    private int index, next;

    private PolylineOptions polylineOptions, blackPolyLineOptions;

    private Polyline blackPolyLine, greyPolyLine;

    IGoogleApi myService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // unpack
        Gson gs = new Gson();

        String currentUserJSON = getIntent().getStringExtra("current user");
        String closestGaragesJSON = getIntent().getStringExtra("closest garages");
        String chosenGarageJSON = getIntent().getStringExtra("chosen garage");

        currentUser = gs.fromJson(currentUserJSON, User.class);
        closestGarages = gs.fromJson(closestGaragesJSON, new TypeToken<ArrayList<GarageEntity>>() {
        }.getType());
        chosenGarage = gs.fromJson(chosenGarageJSON, String.class);

        // Get  destination garage from list of closest garages
        for (GarageEntity g : closestGarages) {
            if(g.getName().compareTo(chosenGarage) == 0)
                destinationGarage = g;
        }

        polylineList = new ArrayList<>();

        // start map service
        mapFragment.getMapAsync(this);

        myService = Common.getGoogleApi();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setTrafficEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Add a marker for current location

        // Add a marker to destination and move the camera
        final LatLng destination = new LatLng(destinationGarage.getLatitude(), destinationGarage.getLongitude());
        mMap.addMarker(new MarkerOptions().position(destination).title(destinationGarage.getName()));

        // Zoom in
        float zoomLevel = 15.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, zoomLevel));

        String requestUrl = null;
        try {
            requestUrl = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "mode=driving&" +
                    "transit_routing_preferences=less_driving&" +
                    "origin=" + destination.latitude + ", " + destination.longitude + "&" +
                    "destination=" + destination + "&" +
                    "key=" + getResources().getString(R.string.google_maps_key);

            Log.d("URL", requestUrl);

            myService.getDataFromGoogleApi(requestUrl)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                JSONArray jsonArray = jsonObject.getJSONArray("routes");

                                for(int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject route = jsonArray.getJSONObject(i);
                                    JSONObject poly = route.getJSONObject("overview_polyline");
                                    String polyLine = poly.getString("points");
                                    polylineList = decodePoly(polyLine);
                                }

                                // Adjusting bounds
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                for(LatLng latLng : polylineList) {
                                    builder.include(latLng);
                                }
                                LatLngBounds bounds = builder.build();

                                // Animate camera
                                CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
                                mMap.animateCamera(mCameraUpdate);

                                // Draw polylines
                                polylineOptions = new PolylineOptions();
                                polylineOptions.color(Color.GRAY);
                                polylineOptions.width(5);
                                polylineOptions.startCap(new SquareCap());
                                polylineOptions.jointType(JointType.ROUND);
                                polylineOptions.addAll(polylineList);
                                greyPolyLine = mMap.addPolyline(polylineOptions);

                                blackPolyLineOptions = new PolylineOptions();
                                blackPolyLineOptions.color(Color.BLACK);
                                blackPolyLineOptions.width(5);
                                blackPolyLineOptions.startCap(new SquareCap());
                                blackPolyLineOptions.jointType(JointType.ROUND);
                                blackPolyLineOptions.addAll(polylineList);
                                blackPolyLine = mMap.addPolyline(blackPolyLineOptions);

                                mMap.addMarker(new MarkerOptions().position(polylineList.get(polylineList.size() - 1)));

                                // Animator
                                final ValueAnimator polylineAnimator = ValueAnimator.ofInt(0, 100);
                                polylineAnimator.setDuration(2000);
                                polylineAnimator.setInterpolator(new LinearInterpolator());
                                polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        List<LatLng> points = greyPolyLine.getPoints();
                                        int percentValue = (int)animation.getAnimatedValue();
                                        int size = points.size();
                                        int newPoints = (int)(size * (percentValue / 100.0f));
                                        List<LatLng> p = points.subList(0, newPoints);
                                        blackPolyLine.setPoints(p);
                                    }
                                });
                                polylineAnimator.start();

                                /**
                                 // Add car marker
                                 marker = mMap.addMarker(new MarkerOptions().position(dest))
                                 .isFlat(true)
                                 .icon(BitmapDescriptorFactory.fromResource(R.drawable.car));
                                 **/

                                // Car moving
                                handler = new Handler();
                                index = -1;
                                next = 1;

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(index < polylineList.size()-1) {
                                            index++;
                                            next = index + 1;
                                        }
                                        if(index < polylineList.size() - 1) {
                                            startPosition = polylineList.get(index);
                                            endPosition = polylineList.get(next);
                                        }

                                        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
                                        valueAnimator.setDuration(3000);
                                        valueAnimator.setInterpolator(new LinearInterpolator());
                                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                            @Override
                                            public void onAnimationUpdate(ValueAnimator animation) {
                                                v = valueAnimator.getAnimatedFraction();
                                                lng = v*endPosition.longitude+(1-v)
                                                        *startPosition.longitude;
                                                lat = v*endPosition.latitude+(1-v)
                                                        *startPosition.latitude;

                                                LatLng newPosition = new LatLng(lat, lng);
                                                marker.setPosition(newPosition);
                                                marker.setAnchor(0.5f, 0.5f);
                                                marker.setRotation(getBearing(startPosition, newPosition));
                                                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                                                        .target(newPosition)
                                                        .zoom(15.5f)
                                                        .build()));
                                            }
                                        });
                                        valueAnimator.start();
                                        handler.postDelayed(this, 3000);
                                    }
                                }, 3000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private float getBearing(LatLng startPosition, LatLng newPosition) {
        double lat = Math.abs(startPosition.latitude - newPosition.latitude);
        double lng = Math.abs(startPosition.longitude - newPosition.longitude);

        if(startPosition.latitude < newPosition.latitude && startPosition.longitude < newPosition.longitude) {
            return (float)(Math.toDegrees(Math.atan(lng/lat)));
        }
        else if(startPosition.latitude >= newPosition.latitude && startPosition.longitude < newPosition.longitude) {
            return (float)(Math.toDegrees(Math.atan(lng/lat))+90);
        }
        else if(startPosition.latitude >= newPosition.latitude && startPosition.longitude >= newPosition.longitude) {
            return (float)(Math.toDegrees(Math.atan(lng/lat))+180);
        }
        else if(startPosition.latitude < newPosition.latitude && startPosition.longitude >= newPosition.longitude) {
            return (float)(Math.toDegrees(Math.atan(lng/lat))+270);
        }

        return -1;
    }

    private List<LatLng> decodePoly(String encoded) {
        List poly = new ArrayList();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while(index < len) {
            int b, shift = 0, result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while(b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while(b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));

            poly.add(p);
        }
        return poly;
    }
}
