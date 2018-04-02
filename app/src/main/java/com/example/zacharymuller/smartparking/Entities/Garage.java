package com.example.zacharymuller.smartparking.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zach on 1/4/2018.
 */

public class Garage implements Parcelable, Serializable {
    private String name;
    //private String status;
    private int size;
    private double latitude;
    private double longitude;
    private double destinationDistance;
    private double userDistance;
    private int floors;
    //private ArrayList<Floor> floors;
    private com.example.zacharymuller.smartparking.APIClient.Spot[] spots;

    public Garage(com.example.zacharymuller.smartparking.APIClient.Spot[] spots, String name, double latitude, double longitude) {
        this.name = name;
        //this.status = status;
        this.size = spots.length;
        this.floors = (int)Math.ceil((double)size / 290.0);
        this.spots = spots;
        this.latitude = latitude;
        this.longitude = longitude;
        //this.floors = floors;
    }


    // Getters
    //    public String getStatus() { return  status; }
//
//    public int getCapacity() { return  capacity; }
//
    public int getFloors() { return this.floors; }

    public double getLatitude() {
        return latitude;
    }

    //
    public double getLongitude() {
        return longitude;
    }

    public com.example.zacharymuller.smartparking.APIClient.Spot getSpot(int i) {
        return this.spots[i];
    }

    public void setSpot(int i, com.example.zacharymuller.smartparking.APIClient.Spot s) {
        this.spots[i] = s;
    }

    //
    public double getDestinationDistance() {
        return destinationDistance;
    }

    public double getUserDistance() {
        return userDistance;
    }

    public com.example.zacharymuller.smartparking.APIClient.Spot[] diff(Garage other) {
        if (!this.name.equals(other.getName())) throw new IllegalArgumentException();

        ArrayList<com.example.zacharymuller.smartparking.APIClient.Spot> differences = new ArrayList<com.example.zacharymuller.smartparking.APIClient.Spot>();
        for (int i = 0; i < this.size; i++) {
            if (this.spots[i].isOccupied() != other.getSpot(i).isOccupied())
                differences.add(this.spots[i]);
        }

        return differences.toArray(new com.example.zacharymuller.smartparking.APIClient.Spot[differences.size()]);
    }

    //
//    public ArrayList<Floor> getFloors() { return  floors; }
//
//
//    // Setters
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public void setCapacity(int capacity) {
//        this.capacity = capacity;
//    }
//
//    public void setFloors(ArrayList<Floor> floors) {
//        this.floors = floors;
//    }
//
//    public void setLatitude(double latitude) {
//        this.latitude = latitude;
//    }
//
//    public void setLongitude(double longitude) {
//        this.longitude = longitude;
//    }
//
    public void setDestinationDistance(double destinationDistance) {
        this.destinationDistance = destinationDistance;
    }

    //
    public void setUserDistance(double userDistance) {
        this.userDistance = userDistance;
    }

    public String getName() {
        return this.name;
    }

    public int getSize() {
        return this.size;
    }

    public com.example.zacharymuller.smartparking.APIClient.Spot[] getOccupiedSpots() {
        ArrayList<com.example.zacharymuller.smartparking.APIClient.Spot> occupied = new ArrayList<com.example.zacharymuller.smartparking.APIClient.Spot>();
        for (int i = 0; i < this.size; i++) {
            if (spots[i].isOccupied()) occupied.add(spots[i]);
        }

        return occupied.toArray(new com.example.zacharymuller.smartparking.APIClient.Spot[occupied.size()]);
    }

    public int getOccupied() {
        return this.getOccupiedSpots().length;
    }

    public com.example.zacharymuller.smartparking.APIClient.Spot get(int i) {
        return this.spots[i];
    }

    public boolean isFull() {
        return this.getOccupied() == this.size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeArray(this.spots);
        dest.writeString(this.name);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    public Garage(Parcel in) {
        this.spots = (com.example.zacharymuller.smartparking.APIClient.Spot[])in.readArray(this.getClass().getClassLoader());
        this.name = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Parcelable.Creator<Garage> CREATOR = new Parcelable.Creator<Garage>() {

        @Override
        public Garage createFromParcel(Parcel source) {
            return new Garage(source);
        }

        @Override
        public Garage[] newArray(int size) {
            return new Garage[size];
        }
    };

    // Parcelable implementation methods
}