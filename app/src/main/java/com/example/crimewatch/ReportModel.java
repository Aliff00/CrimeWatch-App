package com.example.crimewatch;

public class ReportModel {
    //class to hold report data
    private String date;
    private String time;
    private String location;
    private String title;
    private String description;
    private String status;

    private String type;

    public ReportModel(){}

    public ReportModel(String date, String time, String title, String location, String status, String type) {
        this.date = date;
        this.time = time;
        this.title = title;
        this.location = location;
        this.status = status;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
