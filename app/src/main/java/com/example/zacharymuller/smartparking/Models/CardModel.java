package com.example.zacharymuller.smartparking.Models;


/**
 * Created by Zach on 1/13/2018.
 */

public class CardModel {

    private String garage;

    private int numSpotsLeft;

    private String timeToArrive;

    private String walkingTime;


    public CardModel(String garage, int numSpotsLeft, String timeToArrive, String walkingTime) {
        this.garage = garage;
        this.numSpotsLeft = numSpotsLeft;
        this.timeToArrive = timeToArrive;
        this.walkingTime = walkingTime;
    }

    // Getters
    public String getgarage() {
        return garage;
    }

    public int getNumSpotsLeft() {
        return numSpotsLeft;
    }

    public String getTimeToArrive() { return  timeToArrive; }

    public String getWalkingTime() { return  walkingTime; }


    // Setters
    public void setgarage(String garage) {
        this.garage = garage;
    }

    public void setNumSpotsLeft(int numSpotsLeft) {
        this.numSpotsLeft = numSpotsLeft;
    }

    public  void  setTimeToArrive(String timeToArrive) { this.timeToArrive = timeToArrive; }

    public void setWalkingTime(String walkingTime) { this.walkingTime = walkingTime; }
}
