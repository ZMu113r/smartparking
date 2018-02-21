package com.example.zacharymuller.smartparking.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.zacharymuller.smartparking.Util.GarageENUM;
/**
 * Created by Nate Houlihan and Zach Muller on 1/4/2018.
 */

public class Spot implements Parcelable{

    private int spotId;
    private GarageENUM garageENUM;
    private String garageString;
    private boolean occupied;

    public Spot(String sensorString, boolean occupied) {
        this.occupied = occupied;
        this.spotId = Integer.parseInt(sensorString.substring(7, 11));
        this.garageString = (char)(sensorString.charAt(18) - 32) + "";
        if(sensorString.length() > 18) this.garageString += sensorString.substring(19);

        switch(garageString.toLowerCase().charAt(0)) {
            case 'a':
                this.garageENUM = GarageENUM.A;
                break;
            case 'b':
                this.garageENUM = GarageENUM.B;
                break;
            case 'c':
                this.garageENUM = GarageENUM.C;
                break;
            case 'd':
                this.garageENUM = GarageENUM.D;
                break;
            case 'h':
                this.garageENUM = GarageENUM.H;
                break;
            case 'i':
                this.garageENUM = GarageENUM.I;
                break;
            case 'l':
                this.garageENUM = GarageENUM.LIBRA;
                break;
        }
    }

    public int getSpotId() {
        return this.spotId;
    }

    public GarageENUM getGarageENUM() {
        return this.garageENUM;
    }

    public String getGarageString() {
        return this.garageString;
    }

    public boolean isOccupied() {
        return this.occupied;
    }

    // Parcelable implementation methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel desc, int flags) {
        desc.writeInt(spotId);
        desc.writeString(this.garageENUM.name());
        desc.writeString(garageString);
        desc.writeByte((byte) (occupied ? 1 : 0)); // if status == true, byte == 1
    }

    public static final Parcelable.Creator<Spot> CREATOR = new Parcelable.Creator<Spot>() {
        public Spot createFromParcel(Parcel in) {
            return new Spot(in);
        }

        public Spot[] newArray(int size) {
            return new Spot[size];
        }
    };

    // Un-flatten parcel
    public Spot(Parcel in) {
        spotId = in.readInt();
        garageENUM = GarageENUM.valueOf(in.readString());
        garageString = in.readString();
        occupied = in.readByte() != 0; // status == true if byte != 0
    }
}
