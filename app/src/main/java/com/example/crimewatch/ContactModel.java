package com.example.crimewatch;

public class ContactModel {
    //class to hold data of contact - name and phone number
    private String phoneNo;
    private String name;
    private String desc;

    // constructor
    public ContactModel(){
    }
    public ContactModel(String name, String phoneNo, String desc) {
        this.phoneNo = phoneNo;
        this.name = name;
        this.desc= desc;
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

    public void setName(String name) {
        this.name = name;
    }
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
}