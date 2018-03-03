package com.example.zacharymuller.smartparking.APIClient;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.zacharymuller.smartparking.Activities.HomeActivity;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Nathaniel on 2/28/2018.
 */

public class RequestTask extends AsyncTask<String, Void, ArrayList<Garage>> {
    private Context context;
    private HomeActivity activity;

    public RequestTask(Context context, HomeActivity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected ArrayList<Garage> doInBackground(String... params) {
        Garage g = null;
        ArrayList<Garage> garageList = new ArrayList<Garage>();
        try {
            for (String garage : params) {
                g = APIClient.getAllSpots(garage);
                garageList.add(g);
                g = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return garageList;
    }

    @Override
    protected void onPostExecute(ArrayList<Garage> garageList) {
        super.onPostExecute(garageList);
        // Figure out toast stuff

        for (Garage g : garageList) {
            Toast.makeText(context, "Garage" + g.getName() + " spots: " + g.getSize(), Toast.LENGTH_SHORT).show();
            this.activity.setGarage(g);
        }
    }
}
