package com.hotmail.jamesnhendry.fza3077;

import java.util.ArrayList;

public class Clinitian {
    private String name,username,password;
    private ArrayList<Patient> patients;
    private ArrayList<Visit> pastVisits;
    private ArrayList<Visit> futureVisits;


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

    public Clinitian(String name, String username, String password, ArrayList<Patient> patients, ArrayList<Visit> pv, ArrayList<Visit> fv) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.patients = patients;
        this.pastVisits = pv;
        this.futureVisits = fv;
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
