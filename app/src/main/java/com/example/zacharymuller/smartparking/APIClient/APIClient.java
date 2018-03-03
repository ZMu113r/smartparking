package com.example.zacharymuller.smartparking.APIClient;

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
        String res = req(garage.substring(0, 1).toUpperCase() + "" + garage.substring(1).toLowerCase());

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


        return new Garage(spots, garage);//new Garage(objs, garage.substring(0, 1).toUpperCase() + "" + garage.substring(1).toLowerCase());
    }

    public static Spot getSpot(String garage, int id) throws IOException {
        String res = req(garage.substring(0, 1).toUpperCase() + "" + garage.substring(1).toLowerCase() + "/" + String.format("%04d", id));

        JSONObject obj = null;

        try {
            obj = new JSONObject(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new Spot(obj);
    }

    public static String req(String params) throws IOException {
        URL url = new URL("http://" + REMOTEADDR + "/spots/" + params);
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
