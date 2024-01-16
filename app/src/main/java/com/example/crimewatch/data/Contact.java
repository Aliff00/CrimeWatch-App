package com.example.crimewatch.data;

import java.io.Serializable;

public class Contact implements Serializable {
    //class to hold data of contact - name and phone number
    private String phoneNo;
    private String name;
    private String desc;

    private int imageResourceId;

    // constructor
    public Contact(){

    }
    public interface OnItemClickListener {
        void onItemClick(Contact contact);
    }
    public Contact(String name, String phoneNo, String desc, int imageResourceId) {
        this.phoneNo = phoneNo;
        this.name = name;
        this.desc= desc;
        this.imageResourceId=imageResourceId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getImageResourceId(){
        return imageResourceId;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public void setImageResourceId(int id){ this.imageResourceId=id;};
}