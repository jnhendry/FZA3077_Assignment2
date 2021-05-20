package com.hotmail.jamesnhendry.fza3077;

import java.util.ArrayList;

//This class Represents the data model structure of a patient user of this platform.
public class Patient {
    private String clinicianUsername,name,username,password,sex,patientID,location;
    private int phoneNumber;
    private ArrayList<Visit> pastVisits;
    private ArrayList<Visit> futureVisits;


    public Patient() {
        
    }

    public Patient(String name, int phoneNumber, String sex,String patientid,String clinitianUsername) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.sex = sex;
        this.patientID = patientid;
        this.clinicianUsername = clinitianUsername;
    }

    public Patient(String name, int phoneNumber, String sex, String patientID, String clinitianUsername,String location) {
        this.clinicianUsername = clinitianUsername;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.sex = sex;
        this.patientID = patientID;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getClinicianUsername() {
        return clinicianUsername;
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

    public void setClinicianUsername(String clinicianUsername) {
        this.clinicianUsername = clinicianUsername;
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

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
