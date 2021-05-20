package com.hotmail.jamesnhendry.fza3077;

public class Stats {
    private String name,gender,location;
    private double rrs, bloodPressure;
    private int age;
    private boolean smokes, familyHistory;

    public Stats() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getRrs() {
        return rrs;
    }

    public void setRrs(double rrs) {
        this.rrs = rrs;
    }

    public double getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(double bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSmokes() {
        return smokes;
    }

    public void setSmokes(boolean smokes) {
        this.smokes = smokes;
    }

    public boolean isFamilyHistory() {
        return familyHistory;
    }

    public void setFamilyHistory(boolean familyHistory) {
        this.familyHistory = familyHistory;
    }

    public Stats(String name, String gender, String location, double rrs, double bloodPressure, int age, boolean smokes, boolean famhist) {
        this.name = name;
        this.gender = gender;
        this.location = location;
        this.rrs = rrs;
        this.bloodPressure = bloodPressure;
        this.age = age;
        this.smokes = smokes;
        this.familyHistory = famhist;
    }
}
