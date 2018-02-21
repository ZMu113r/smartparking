package com.example.zacharymuller.smartparking.Entities;


import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by Zach on 1/6/2018.
 */

public class User implements Parcelable {
    private String parking_status;

    private double longitude;

    private double latitude;


    public User(String parking_status, double longitude, double latitude) {
        this.parking_status = parking_status;

        this.longitude = longitude;

        this.latitude = latitude;
    }


    public String getParking_status() { return parking_status; }

    public double getLongitude() { return longitude; }

    public double getLatitude() { return latitude; }


    public void setParking_status(String parking_status) { this.parking_status = parking_status; }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }



    // Parcelable implementation methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel desc, int flags) {
        desc.writeString(parking_status);
        desc.writeDouble(longitude);
        desc.writeDouble(latitude);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    // Un-flatten parcel
    private User(Parcel in) {
        parking_status = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
    }
}