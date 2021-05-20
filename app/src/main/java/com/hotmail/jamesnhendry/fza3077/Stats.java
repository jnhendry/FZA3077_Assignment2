package com.hotmail.jamesnhendry.fza3077;

public class Stats {
    private String name,gender,location;
    private double rrs,bloodpressure;
    private int age;
    private boolean smokes,famhist;

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

    public double getBloodpressure() {
        return bloodpressure;
    }

    public void setBloodpressure(double bloodpressure) {
        this.bloodpressure = bloodpressure;
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

    public boolean isFamhist() {
        return famhist;
    }

    public void setFamhist(boolean famhist) {
        this.famhist = famhist;
    }

    public Stats(String name, String gender, String location, double rrs, double bloodpressure, int age, boolean smokes, boolean famhist) {
        this.name = name;
        this.gender = gender;
        this.location = location;
        this.rrs = rrs;
        this.bloodpressure = bloodpressure;
        this.age = age;
        this.smokes = smokes;
        this.famhist = famhist;
    }
}
