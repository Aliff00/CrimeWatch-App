package com.example.map;

public class Crime {
    private double latitude;
    private double longitude;
    private String crimeType;

    public Crime(double latitude, double longitude, String CrimeType) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.crimeType = crimeType;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCrimeType() {
        return crimeType;
    }
}