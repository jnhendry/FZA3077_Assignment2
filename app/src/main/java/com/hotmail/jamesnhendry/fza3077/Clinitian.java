package com.hotmail.jamesnhendry.fza3077;

import java.util.ArrayList;

public class Clinitian {
    private String name,username,password;
    private ArrayList<Patient> patients;


    public Clinitian(String name, String username, String password, ArrayList<Patient> patients) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.patients = patients;
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
