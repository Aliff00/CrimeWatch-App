package com.example.crimewatch;

public class ContactModel {
    //class to hold data of contact - name and phone number
    private String phoneNo;
    private String name;

    // constructor
    public ContactModel(){
    }
    public ContactModel(String name, String phoneNo) {
        this.phoneNo = phoneNo;
        this.name = name;
    }

    /* validate the phone number, and reformat is necessary
    private String validate(String phone) {

        // creating StringBuilder for both the cases
        StringBuilder case1 = new StringBuilder("+60");
        StringBuilder case2 = new StringBuilder("");

        // check if the string already has a "+"
        if (phone.charAt(0) != '+') {
            for (int i = 0; i < phone.length(); i++) {
                // remove any spaces or "-"
                if (phone.charAt(i) != '-' && phone.charAt(i) != ' ') {
                    case1.append(phone.charAt(i));
                }
            }
            return case1.toString();
        } else {
            for (int i = 0; i < phone.length(); i++) {
                // remove any spaces or "-"
                if (phone.charAt(i) != '-' || phone.charAt(i) != ' ') {
                    case2.append(phone.charAt(i));
                }
            }
            return case2.toString();
        }

    }*/

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}