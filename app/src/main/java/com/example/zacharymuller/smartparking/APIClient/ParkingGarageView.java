package com.example.zacharymuller.smartparking.APIClient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.zacharymuller.smartparking.Entities.Garage;
import com.example.zacharymuller.smartparking.Entities.Garages;

/**
 * Created by Nathaniel on 3/5/2018.
 */

public class ParkingGarageView extends View {
    private Paint paintRed;
    private Paint paintGreen;
    private Paint paintWhite;
    Garage garage;

    public ParkingGarageView(Context context) {
        super(context);

        paintRed = new Paint();
        paintRed.setColor(Color.RED);
        paintRed.setStyle(Paint.Style.FILL);

        paintGreen = new Paint();
        paintGreen.setColor(Color.GREEN);
        paintGreen.setStyle(Paint.Style.FILL);

        paintWhite = new Paint();
        paintWhite.setColor(Color.GRAY);
        paintWhite.setStyle(Paint.Style.FILL);
    }

    public ParkingGarageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintRed = new Paint();
        paintRed.setColor(Color.RED);
        paintRed.setStyle(Paint.Style.FILL);

        paintGreen = new Paint();
        paintGreen.setColor(Color.GREEN);
        paintGreen.setStyle(Paint.Style.FILL);

        paintWhite = new Paint();
        paintWhite.setColor(Color.WHITE);
        paintWhite.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas canvas) {
        int startSpot = Garages.getFloor() * 290;
        int endSpot = Garages.getFloor() * 290 + 290;
        endSpot = Math.min(endSpot, this.garage.getSize());
        /*
        *  x -> 10px, dev_wid
        *  y -> dev_height - 100px, 10px
        *  space -> 10px
        * */

        //Log.i("PGVIEW", "Garage " + garage.getName() + " size " + garage.getSize());

        int[] columns = {
                32, 31, 29, 29, 30, 31, 31, 30, 30, 28
        };

        int[] rows = {
                15, 4, 5, 25
        };

        if(garage != null) {
           int spot = 0 + startSpot;
           Paint selectedPaint = paintWhite;

           canvas.save();
           // Draw Top Row
           canvas.translate(getMeasuredWidth() / 2 - (20 * 15 + 10 * 15) / 2, getMeasuredHeight() / 2 - 430);
           for(int i = 0; i < 15; i++) {
               float left = 20 * i + 10 * i;

               if(spot < garage.getSize()) {
                   if(garage.getSpot(spot).isOccupied()) {
                       selectedPaint = paintRed;
                   } else {
                       selectedPaint = paintGreen;
                   }
               } else {
                   selectedPaint = paintWhite;
               }

               canvas.drawRect(left, 0, left + 20, 40, selectedPaint);
               spot ++;
           }

           // Draw second row
           canvas.translate((20 * 15 + 10 * 15) / 2 - (20 * 4 + 10 * 4) / 2, 50);
           for(int i = 0; i < 4; i++) {
               float left = 20 * i + 10 * i;

               selectedPaint = paintWhite;

               if(spot < garage.getSize()) {
                   if(garage.getSpot(spot).isOccupied()) {
                       selectedPaint = paintRed;
                   } else {
                       selectedPaint = paintGreen;
                   }
               } else {
                   selectedPaint = paintWhite;
               }

               canvas.drawRect(left, 0, left + 20, 40, selectedPaint);
               spot ++;
           }
           // Draw all columns

           canvas.restore();
           canvas.translate(getMeasuredWidth() / 2 - (40 * columns.length + 50 * columns.length) / 2, getMeasuredHeight() / 2 - (40 * 15 + 10 * 15) / 2 + (40 + 10));

           for(int i = 0; i < columns.length; i++) {
               for(int j = 0; j < columns[i]; j++) {
                   float left = i * 40 + i * 50;
                   float top = j * 20 + j * 10;

                   if(spot < garage.getSize()) {
                       if(garage.getSpot(spot).isOccupied()) {
                           selectedPaint = paintRed;
                       } else {
                           selectedPaint = paintGreen;
                       }
                   } else {
                       selectedPaint = paintWhite;
                   }

                   canvas.drawRect(left, top, left + 40, top + 20, selectedPaint);
                   spot ++;
               }
           }

           // Draw second to last row
           canvas.translate((40 * columns.length + 50 * columns.length) / 2 - (20 * 5 + 5 * 10) / 2, ((40 * 15 + 10 * 15) / 2 + (40 + 10)) * 2 + 80);

           for(int i = 0; i < 5; i++) {
               float left = 20 * i + 10 * i;

               selectedPaint = paintWhite;

               if(spot < garage.getSize()) {
                   if(garage.getSpot(spot).isOccupied()) {
                       selectedPaint = paintRed;
                   } else {
                       selectedPaint = paintGreen;
                   }
               } else {
                   selectedPaint = paintWhite;
               }

               canvas.drawRect(left, 0, left + 20, 40, selectedPaint);
               spot ++;
           }
           // Draw Bottom Row
           canvas.translate((20 * 5 + 5 * 10) / 2 - (20 * 25 + 10 * 25) / 2, 40 + 10);
           for(int i = 0; i < 25; i++) {
               float left = 20 * i + 10 * i;
               selectedPaint = paintWhite;

               if(spot < garage.getSize()) {
                   if(garage.getSpot(spot).isOccupied()) {
                       selectedPaint = paintRed;
                   } else {
                       selectedPaint = paintGreen;
                   }
               } else {
                   selectedPaint = paintWhite;
               }

               canvas.drawRect(left, 0, left + 20, 40, selectedPaint);
               spot ++;
           }
        }
        /*
        if(garage != null) {
            canvas.translate(getMeasuredWidth() / 2 - (20 * 29 + 10 * 29) / 2, getMeasuredHeight() / 2 - (40 * 10 + 10 * 10) / 2);
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 29; j++) {
                    float left = 20 * j + 10 * j;
                    float top = 40 * i + 10 * i;
                    Paint selectedPaint = paintWhite;

                    int spotSelector = startSpot + i * 29 + j;

                    if(spotSelector < this.garage.getSize()) {
                        if(garage.getSpot(spotSelector).isOccupied())
                            selectedPaint = paintRed;
                        else
                            selectedPaint = paintGreen;
                        Log.i("PGVIEW", "Spot: " + spotSelector);
                    } else {
                        Log.i("PGVIEW", "Spot: " + spotSelector + " doesnt exist, painting white");
                    }

                    canvas.drawRect(left, top, left + 20, top + 40, selectedPaint);
                }
            }
        }*/ else {
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

    public void forceRedraw() {
        invalidate();
    }
}
