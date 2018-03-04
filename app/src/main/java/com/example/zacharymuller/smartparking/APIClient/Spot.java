package com.example.zacharymuller.smartparking.APIClient;

import org.json.JSONException;
import org.json.JSONObject;

public class Spot implements java.io.Serializable {
    private boolean occupied;
    private int id;

    public Spot(JSONObject obj) {
        try {
            this.occupied = obj.getBoolean("occupied");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String name = null;
        try {
            name = obj.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.id = Integer.parseInt(name.substring(7, 11));
    }

    public boolean isOccupied() {
        return this.occupied;
    }

    public int getId() {
        return this.id;
    }

    public String toString() {
        String s = "";
        s += "\tSpotId: " + String.format("%04d", this.id) + "\n";
        s += "\tOccupied: " + this.occupied;

        return s;
    }
}
