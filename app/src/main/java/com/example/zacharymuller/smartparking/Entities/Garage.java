package com.example.zacharymuller.smartparking.Entities;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by Zach on 1/4/2018.
 */

public class Garage implements Parcelable {

    private String name;
    private String status;
    private int capacity;
    private double latitude;
    private double longitude;
    private double destinationDistance;
    private double userDistance;
    private ArrayList<Floor> floors;


    public Garage(String name, String status, int capacity, double latitude, double longitude, ArrayList<Floor> floors) {
        this.name = name;
        this.status = status;
        this.capacity = capacity;
        this.latitude = latitude;
        this.longitude = longitude;
        this.floors = floors;
    }


    // Getters
    public String getName() { return name; }

    public String getStatus() { return  status; }

    public int getCapacity() { return  capacity; }

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }

    public double getDestinationDistance() {
        return destinationDistance;
    }

    public double getUserDistance() {
        return userDistance;
    }

    public ArrayList<Floor> getFloors() { return  floors; }


    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setFloors(ArrayList<Floor> floors) {
        this.floors = floors;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setDestinationDistance(double destinationDistance) {
        this.destinationDistance = destinationDistance;
    }

    public void setUserDistance(double userDistance) {
        this.userDistance = userDistance;
    }



    // Parcelable implementation methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel desc, int flags) {
        desc.writeString(name);
        desc.writeString(status);
        desc.writeInt(capacity);
        desc.writeDouble(latitude);
        desc.writeDouble(longitude);
        desc.writeDouble(destinationDistance);
        desc.writeDouble(userDistance);
        desc.writeTypedList(floors);
    }

    public static final Parcelable.Creator<Garage> CREATOR = new Parcelable.Creator<Garage>() {
        public Garage createFromParcel(Parcel in) {
            return new Garage(in);
        }

        public Garage[] newArray(int size) {
            return new Garage[size];
        }
    };

    // Un-flatten parcel
    public Garage(Parcel in) {
        name = in.readString();
        status = in.readString();
        capacity = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        destinationDistance = in.readDouble();
        userDistance = in.readDouble();
        in.readTypedList(floors, Floor.CREATOR);
    }
}
