package com.example.crimewatch;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

public class Report {

    private Timestamp timestamp;
    private GeoPoint location;
    private String desc;
    private String userId;
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }




}