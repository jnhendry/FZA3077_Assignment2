package com.hotmail.jamesnhendry.fza3077;

import java.util.ArrayList;

//This class Represents the data model structure of a clinician user of this platform.
public class Clinician {
    private String name,username,password;
    private ArrayList<Patient> patients;
    private ArrayList<Visit> pastVisits;


    public ArrayList<Visit> getPastVisits() {
        return pastVisits;
    }

    public void setPastVisits(ArrayList<Visit> pastVisits) {
        this.pastVisits = pastVisits;
    }


    public Clinician(String name, String username, String password, ArrayList<Patient> patients, ArrayList<Visit> pv) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.patients = patients;
        this.pastVisits = pv;
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

    public ArrayList<Patient> getPatients() {
        return patients;
    }

    public void setPatients(ArrayList<Patient> patients) {
        this.patients = patients;
    }
}
