package com.hotmail.jamesnhendry.fza3077;

import java.util.ArrayList;

public class Patient {
    private String clinitianUsername,name,username,password,phoneNumber,sex,patientID;
    private ArrayList<Visit> pastVisits;
    private ArrayList<Visit> futureVisits;


    public Patient() {
    }

    public Patient(String name, String phoneNumber, String sex,String patientid,String clinitianUsername) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.sex = sex;
        this.patientID = patientid;
        this.clinitianUsername = clinitianUsername;
    }



    public String getPatientID() {
        return patientID;
    }

    public String getClinitianUsername() {
        return clinitianUsername;
    }

    public ArrayList<Visit> getPastVisits() {
        return pastVisits;
    }

    public void setPastVisits(ArrayList<Visit> pastVisits) {
        this.pastVisits = pastVisits;
    }

    public ArrayList<Visit> getFutureVisits() {
        return futureVisits;
    }

    public void setFutureVisits(ArrayList<Visit> futureVisits) {
        this.futureVisits = futureVisits;
    }

    public void setClinitianUsername(String clinitianUsername) {
        this.clinitianUsername = clinitianUsername;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
