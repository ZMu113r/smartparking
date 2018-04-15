package com.example.zacharymuller.smartparking.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zacharymuller.smartparking.APIClient.ParkingGarageView;
import com.example.zacharymuller.smartparking.APIClient.PollTask;
import com.example.zacharymuller.smartparking.APIClient.Spot;
import com.example.zacharymuller.smartparking.Entities.Garage;
import com.example.zacharymuller.smartparking.Entities.Garages;
import com.example.zacharymuller.smartparking.R;

public class SpotVisualizerActivity extends AppCompatActivity {
    /*
    * 290 Spots on one floor, store state of floor
    * */
    private Garage garage;
    private Button setParkedButton;
    private PollTask pollTask;

    private ParkingGarageView pgView;
    private TextView floorText;

    private int floor;
    private int floors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("SpotVisualiser", "Started");

        setContentView(R.layout.activity_spot_visualizer);

        Bundle bundle = getIntent().getExtras();

        garage = Garages.getGarage("A"); //Garages.getGarage(bundle.getString("chosengarage"));

        pgView = findViewById(R.id.parkingGarageView);
        pgView.setGarage(garage);
        floorText = findViewById(R.id.floorText);

        pollTask = new PollTask(garage, this);

        setParkedButton = findViewById(R.id.setParkedButton);
        setParkedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pollTask.setShouldQuit();
                Intent intent = new Intent(SpotVisualizerActivity.this, SplashScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
                finish();
            }
        });

        floor = 0;
        floors = this.garage.getFloors();

        pgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Garages.setFloor(++floor % floors);
                forceRedraw();
            }
        });

        pollTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void setSpot(int i, Spot spot) {
        pgView.setSpot(i, spot);
    }

    public void forceRedraw() {
        floorText.clearComposingText();
        floorText.setText(String.format("Floor %1d", Garages.getFloor() + 1));
        pgView.forceRedraw();
    }
}
