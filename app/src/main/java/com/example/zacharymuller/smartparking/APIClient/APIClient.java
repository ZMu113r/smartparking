package com.example.zacharymuller.smartparking.APIClient;

import android.util.Log;

import com.example.zacharymuller.smartparking.Entities.Garage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIClient {
    private static final String REMOTEADDR = "18.218.108.116:3000";

    public static Garage getAllSpots(String garage) throws IOException {
        String res = req("spots", garage.substring(0, 1).toUpperCase() + "" + garage.substring(1).toLowerCase());
        String locRes = req("locations", garage.substring(0, 1).toUpperCase() + "" + garage.substring(1).toLowerCase());

        JSONArray json = null;
        try {
            json = new JSONArray(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Spot[] spots = new Spot[json.length()];

        for (int i = 0; i < json.length(); i++) {
            JSONObject obj = null;
            try {
                obj = json.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            spots[i] = new Spot(obj);
        }

        JSONArray arr = null;
        JSONObject obj = null;
        try {
            arr = new JSONArray(locRes);
            obj = arr.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        double lat = 0;
        double lng = 0;

        if (obj == null)
            Log.i("APICLIENT", "request failed for location data");
        else {
            try {
                lat = obj.getDouble("latitude");
                lng = obj.getDouble("longitude");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return new Garage(spots, garage, lat, lng);//new Garage(objs, garage.substring(0, 1).toUpperCase() + "" + garage.substring(1).toLowerCase());
    }

    public static Spot getSpot(String garage, int id) throws IOException {
        String res = req("spots", garage.substring(0, 1).toUpperCase() + "" + garage.substring(1).toLowerCase() + "/" + String.format("%04d", id));

        JSONObject obj = null;

        try {
            obj = new JSONObject(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new Spot(obj);
    }

    public static String req(String route, String params) throws IOException {
        URL url = new URL("http://" + REMOTEADDR + "/" + route + "/" + params);
        Log.i("APICLIENT", url.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        String output;
        output = br.readLine();

        conn.disconnect();

        return output;
    }
}
