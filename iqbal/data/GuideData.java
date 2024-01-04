package com.example.iqbal.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GuideData extends Data implements Serializable {
    private String title;
    private List<String> details;
    private List<String> p1List;

    public GuideData() {
        details = new ArrayList<>();
        p1List = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getDetails() {
        return details;
    }
    public List<String> getP1() {
        return p1List;
    }
    public void addP1(String p1) {
        p1List.add(p1);
    }
    public void addDetail(String detail) {
        details.add(detail);
    }


}