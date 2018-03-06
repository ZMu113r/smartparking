package com.example.zacharymuller.smartparking.APIClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.zacharymuller.smartparking.Activities.SpotVisualizerActivity;
import com.example.zacharymuller.smartparking.Entities.Garage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Nathaniel on 3/3/2018.
 */

public class PollTask extends AsyncTask<Void, Void, Void> {
    private SpotVisualizerActivity a;
    private boolean quittingTime = false;

    private ArrayList<Edit> q;
    Garage garage;

    public PollTask(Garage g, SpotVisualizerActivity a) {
        this.a = a;
        this.garage = g;

        this.q = new ArrayList<Edit>();

        Log.i("GARAGELISTENER", "Task created");
    }

    public synchronized void setShouldQuit() {
        this.quittingTime = true;
    }

    @Override
    protected Void doInBackground(Void... params) {

        while (!quittingTime) {
            try {
                Garage g = APIClient.getAllSpots(garage.getName());
                com.example.zacharymuller.smartparking.APIClient.Spot[] changes = g.diff(garage);
                for (Spot s : changes) {
                    q.add(new Edit(g.getName(), s));
                    garage.setSpot(s.getId(), s);

                    publishProgress(params);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... progress) {
        super.onProgressUpdate(progress);
        Spot s = q.remove(0).getSpot();
        if (s != null) {
            a.setSpot(s.getId(), s);
            Toast.makeText(this.a.getApplicationContext(), String.format("sensor_%04d_garage%s changed from ", s.getId(), garage.getName().toLowerCase()) + occupiedStatus(!s.isOccupied()) + " to " + occupiedStatus(s.isOccupied()), Toast.LENGTH_SHORT).show();
            Log.i("GARAGELISTENER", String.format("[UPDATE] sensor_%04d_garage%s changed from ", s.getId(), garage.getName().toLowerCase()) + occupiedStatus(!s.isOccupied()) + " to " + occupiedStatus(s.isOccupied()));
        } else {
            Log.i("GARAGELISTENER", "s is null");
        }
    }

    private String occupiedStatus(boolean status) {
        if(status) return "Occupied";
        else return "Unoccupied";
    }
}
