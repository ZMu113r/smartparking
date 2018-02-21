package com.example.zacharymuller.smartparking.Handlers;

import android.support.annotation.NonNull;

import com.example.zacharymuller.smartparking.Entities.Garage;

import java.util.Comparator;

/**
 * Created by Zach on 1/25/2018.
 */

public class DestinationSortingHandler implements Comparator<Garage> {
    @Override
    public int compare(Garage g1, Garage g2) {
        if(g1.getDestinationDistance() > g2.getDestinationDistance()) {
            return 1;
        }
        else if(g1.getDestinationDistance() < g2.getDestinationDistance()) {
            return -1;
        }
        else
            return 0;
    }
}