package com.example.zacharymuller.smartparking.APIClient;

import java.util.ArrayList;

public class Garage {
    private int size;
    private Spot[] spots;
    String garage;

    public Garage(Spot[] spots, String name) {
        this.garage = name;
        this.size = spots.length;
        this.spots = spots;
    }

    public Spot[] getOccupied() {
        ArrayList<Spot> occupied = new ArrayList<Spot>();
        for (int i = 0; i < this.size; i++) {
            if (spots[i].isOccupied()) {
                occupied.add(spots[i]);
            }
        }

        return occupied.toArray(new Spot[occupied.size()]);
    }

    public String getName() {
        return this.garage;
    }

    public int getNumOccupied() {
        return this.getOccupied().length;
    }

    public Spot get(int i) {
        return this.spots[i];
    }

    public boolean isFull() {
        return this.getNumOccupied() == this.size;
    }

    public String toString() {
        String s = "" + this.garage + "\n";
        for (int i = 0; i < spots.length; i++) {
            s += spots[i] + "\n";
        }

        return s;
    }

    public int getSize() {
        return this.size;
    }
}
