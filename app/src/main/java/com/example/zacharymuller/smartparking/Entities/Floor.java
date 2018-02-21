package com.example.zacharymuller.smartparking.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Zach on 1/4/2018.
 */

public class Floor implements Parcelable{

    private ArrayList<Spot> spots;
    private int capacity;
    private int number;

    // Getters
    public ArrayList<Spot> getSpots() { return spots; }

    public int getCapacity() { return capacity; }

    public int getNumber() { return number; }


    // Setters
    public void setSpots(ArrayList<Spot> spots) { spots = this.spots; }

    public void setCapacity(int capacity) { capacity = this.capacity; }

    public void setNumber(int number) { number = this.number; }

    // Parcelable implementation methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel desc, int flags) {
        desc.writeTypedList(spots);
        desc.writeInt(capacity);
        desc.writeInt(number);
    }

    public static final Parcelable.Creator<Floor> CREATOR = new Parcelable.Creator<Floor>() {
        public Floor createFromParcel(Parcel in) {
            return new Floor(in);
        }

        public Floor[] newArray(int size) {
            return new Floor[size];
        }
    };

    // Un-flatten parcel
    public Floor(Parcel in) {
        capacity = in.readInt();
        number = in.readInt();
        in.readTypedList(spots, Spot.CREATOR);
    }
}
