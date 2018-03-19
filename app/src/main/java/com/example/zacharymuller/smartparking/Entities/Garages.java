package com.example.zacharymuller.smartparking.Entities;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nathaniel on 3/3/2018.
 */

public class Garages {
    private static Garage[] garages;
    private static ArrayList<Garage> closestGarages;
    private static HashMap<String, Integer> indices;

    public static void initGarages(String[] keys) {
        garages = new Garage[keys.length];
        indices = new HashMap<>();

        int count = 0;
        for(String key: keys) {
            indices.put(key, count++);
        }

        closestGarages = new ArrayList<>();
    }

    public static ArrayList<Garage> getClosestGarages() {
        return closestGarages;
    }

    public static Garage getGarage(String name) {
        return garages[indices.get(name)];
    }

    public static void setGarage(String name, Garage g) {
        garages[indices.get(name)] = g;
    }

    public static Garage[] getGarages() {
        return garages;
    }

    public static void addClosestGarage(Garage g) {
        closestGarages.add(g);
    }

    public static void resetClosestGarages() {
        closestGarages = null;
        closestGarages = new ArrayList<>();
    }

    //For Debugging Purposes
    public static void isThisNull ()
    {
        for (Garage g : garages) {
            if (g == null) Log.i("debug", "isThisNull: yes");
            else Log.i("debug", "isThisNull: no");
        }
    }
}
