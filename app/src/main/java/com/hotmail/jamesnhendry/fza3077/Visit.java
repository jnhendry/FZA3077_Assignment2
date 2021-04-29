package com.hotmail.jamesnhendry.fza3077;

public class Visit {
    private String date,time,clinitianUsername,patientUsername,clinitiannotes;
    private double bloodpressure,cholestorol,cReactive,apolprotB,apolprotA,lipProteinA;
    private boolean smoker;

    public double getcReactive() {
        return cReactive;
    }

    public void setcReactive(double cReactive) {
        this.cReactive = cReactive;
    }

    public double getApolprotB() {
        return apolprotB;
    }

    public void setApolprotB(double apolprotB) {
        this.apolprotB = apolprotB;
    }

    public double getApolprotA() {
        return apolprotA;
    }

    public void setApolprotA(double apolprotA) {
        this.apolprotA = apolprotA;
    }

    public double getLipProteinA() {
        return lipProteinA;
    }

    public void setLipProteinA(double lipProteinA) {
        this.lipProteinA = lipProteinA;
    }

    public boolean isFamhist() {
        return famhist;
    }

    public void setFamhist(boolean famhist) {
        this.famhist = famhist;
    }

    private boolean famhist;
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Visit(String date, String time, String clinitianUsername, String patientUsername) {
        this.date = date;
        this.time = time;
        this.clinitianUsername = clinitianUsername;
        this.patientUsername = patientUsername;
    }

    public Visit(String date, String time, String clinitianUsername, String patientUsername, String clinitiannotes, double bloodpressure,
                    double cholestorol, double cReactive, double apolprotB, double apolprotA,
                            double lipProteinA, boolean smoker, boolean famhist, int age) {
        this.date = date;
        this.time = time;
        this.clinitianUsername = clinitianUsername;
        this.patientUsername = patientUsername;
        this.clinitiannotes = clinitiannotes;
        this.bloodpressure = bloodpressure;
        this.cholestorol = cholestorol;
        this.cReactive = cReactive;
        this.apolprotB = apolprotB;
        this.apolprotA = apolprotA;
        this.lipProteinA = lipProteinA;
        this.smoker = smoker;
        this.famhist = famhist;
        this.age = age;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getClinitianUsername() {
        return clinitianUsername;
    }

    public void setClinitianUsername(String clinitianUsername) {
        this.clinitianUsername = clinitianUsername;
    }

    public String getPatientUsername() {
        return patientUsername;
    }

    public void setPatientUsername(String patientUsername) {
        this.patientUsername = patientUsername;
    }

    public String getClinitiannotes() {
        return clinitiannotes;
    }

    public void setClinitiannotes(String clinitiannotes) {
        this.clinitiannotes = clinitiannotes;
    }

    public double getBloodpressure() {
        return bloodpressure;
    }

    public void setBloodpressure(double bloodpressure) {
        this.bloodpressure = bloodpressure;
    }

    public double getCholestorol() {
        return cholestorol;
    }

    public void setCholestorol(double cholestorol) {
        this.cholestorol = cholestorol;
    }

    public boolean isSmoker() {
        return smoker;
    }

    public void setSmoker(boolean smoker) {
        this.smoker = smoker;
    }
}
