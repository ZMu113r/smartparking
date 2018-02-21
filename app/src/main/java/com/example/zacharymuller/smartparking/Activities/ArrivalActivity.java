package com.example.zacharymuller.smartparking.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.zacharymuller.smartparking.R;

public class ArrivalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrival);

        // Button in case user wants to route to a new destination
        Button newRouteButton = findViewById(R.id.new_route_button);
        newRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArrivalActivity.this, DestinationSelectionActivity.class);
                startActivity(intent);
            }
        });
    }
}