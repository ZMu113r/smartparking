package com.example.zacharymuller.smartparking.APIClient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.zacharymuller.smartparking.Entities.Garage;

/**
 * Created by Nathaniel on 3/5/2018.
 */

public class ParkingGarageView extends View {
    private Paint paintRed;
    private Paint paintGreen;
    Garage garage;

    public ParkingGarageView(Context context) {
        super(context);

        paintRed = new Paint();
        paintRed.setColor(Color.RED);
        paintRed.setStyle(Paint.Style.FILL);

        paintGreen = new Paint();
        paintGreen.setColor(Color.GREEN);
        paintGreen.setStyle(Paint.Style.FILL);
    }

    public ParkingGarageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintRed = new Paint();
        paintRed.setColor(Color.RED);
        paintRed.setStyle(Paint.Style.FILL);

        paintGreen = new Paint();
        paintGreen.setColor(Color.GREEN);
        paintGreen.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(garage != null) {
            int numDraw = Math.min(garage.getSize(), 10);
            canvas.translate(getMeasuredWidth() / 2 - (50 * numDraw + 35 * numDraw) / 2, getMeasuredHeight() / 2);
            for (int i = 0; i < numDraw;  i++) {
                float left = 50 * i + 35 * i;
                canvas.drawRect(left, 0, left + 50, 100, garage.getSpot(i).isOccupied() ? paintRed : paintGreen);
            }
        } else {
            Log.i("ParkingGarageView", "Garage is null");
        }
    }

    public void setGarage(Garage g) {
        this.garage = g;
    }

    public void setSpot(int id, Spot s) {
        garage.setSpot(id, s);
        Log.i("ParkingGarageView", "Spot " + id + " changed");
        invalidate();
    }
}
