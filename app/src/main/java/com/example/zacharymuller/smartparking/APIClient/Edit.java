package com.example.zacharymuller.smartparking.APIClient;

public class Edit {
    private String garage;
    private Spot s;

    public Edit(String garage, Spot s) {
        this.garage = garage;
        this.s = s;
    }

    public Spot getSpot() {
        return this.s;
    }
}
