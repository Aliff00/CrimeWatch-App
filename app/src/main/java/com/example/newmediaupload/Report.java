package com.example.newmediaupload;

public class Report {
    private String date;
    private String time;
    private String location;
    private String incident;

    public Report() {
        // Required empty constructor for Firestore
    }

    public Report(String date, String time, String location, String incident) {
        this.date = date;
        this.time = time;
        this.location = location;
        this.incident = incident;
    }

    // Getter and Setter methods for 'date'
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // Getter and Setter methods for 'time'
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    // Getter and Setter methods for 'location'
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // Getter and Setter methods for 'incident'
    public String getIncident() {
        return incident;
    }

    public void setIncident(String incident) {
        this.incident = incident;
    }
}