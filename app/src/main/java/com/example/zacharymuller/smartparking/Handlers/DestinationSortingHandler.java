package com.example.zacharymuller.smartparking.Handlers;

import com.example.zacharymuller.smartparking.Entities.GarageEntity;

import java.util.Comparator;

/**
 * Created by Zach on 1/25/2018.
 */

public class DestinationSortingHandler implements Comparator<GarageEntity> {
    @Override
    public int compare(GarageEntity g1, GarageEntity g2) {
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