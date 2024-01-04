package com.example.map;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

public class Report {
    private String desc;
    private GeoPoint location;
    private Timestamp timestamp;
    private String user;

    // Constructor, getters, and setters

    public Report() {
        // Default constructor required for Firestore
    }

    public String getDesc() {
        return desc;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getUser() {
        return user;
    }
}
