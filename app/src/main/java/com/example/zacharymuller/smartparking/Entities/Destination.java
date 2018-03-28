package com.example.zacharymuller.smartparking.Entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zach on 1/4/2018.
 */

public class Destination implements Parcelable {

    private String name;
    private double longitude;
    private double latitude;


    // Getters
    public String getName() { return name; }

    public double getLongitude() { return longitude; }

    public double getLatitude() { return latitude; }


    // Setters
    public void setName(String name) { this.name = name; }

    public void setLongitude(double longitude) { this.longitude = longitude; }

    public void setLatitude(double latitude) { this.latitude = latitude; }


    // Empty constructor to ensure normal object operation
    public Destination() {}


    // Parcelable implementation
    @Override
    public int describeContents() {
        return 0;
    }

    // A.K.A Flatten Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
    }

    public static final Parcelable.Creator<Destination> CREATOR = new Parcelable.Creator<Destination>() {
        public Destination createFromParcel(Parcel in) {
            return new Destination(in);
        }

        public Destination[] newArray(int size) {
            return new Destination[size];
        }
    };

    // Un-flatten parcel
    public Destination(Parcel in) {
        name = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
    }
}
