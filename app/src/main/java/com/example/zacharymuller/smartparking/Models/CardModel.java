package com.example.zacharymuller.smartparking.Models;


/**
 * Created by Zach on 1/13/2018.
 */

public class CardModel {

    private String garage;

    private int numSpotsLeft;

    private String timeToArrive;

    private String proximity;


    public CardModel(String garage, int numSpotsLeft, String timeToArrive, String proximity) {
        this.garage = garage;
        this.numSpotsLeft = numSpotsLeft;
        this.timeToArrive = timeToArrive;
        this.proximity = proximity;
    }

    // Getters
    public String getgarage() {
        return garage;
    }

    public int getNumSpotsLeft() {
        return numSpotsLeft;
    }

    public String getTimeToArrive() { return  timeToArrive; }

    public String getProximity() { return  proximity; }


    // Setters
    public void setgarage(String garage) {
        this.garage = garage;
    }

    public void setNumSpotsLeft(int numSpotsLeft) {
        this.numSpotsLeft = numSpotsLeft;
    }

    public  void  setTimeToArrive(String timeToArrive) { this.timeToArrive = timeToArrive; }

    public void setProximity(String proximity) { this.proximity = proximity; }
}
