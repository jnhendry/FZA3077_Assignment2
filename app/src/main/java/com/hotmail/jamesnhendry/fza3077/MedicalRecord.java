package com.hotmail.jamesnhendry.fza3077;

public class MedicalRecord {
    private String dateUpdated,time,clinitianUsername,patientUsername,clinitiannotes;
    private int age;
    private double bloodpressure,reynoldsRiskScore,cReactive,apolprotB,apolprotA,lipProteinA;
    private boolean smoker;
    private boolean famhist;


    public MedicalRecord(String dateUpdated, String time, String clinitianUsername, String patientUsername, String clinitiannotes,
                         int age, double bloodpressure, double reynoldsRiskScore, double cReactive, double apolprotB,
                         double apolprotA, double lipProteinA, boolean smoker, boolean famhist) {
        this.dateUpdated = dateUpdated;
        this.time = time;
        this.clinitianUsername = clinitianUsername;
        this.patientUsername = patientUsername;
        this.clinitiannotes = clinitiannotes;
        this.age = age;
        this.bloodpressure = bloodpressure;
        this.reynoldsRiskScore = reynoldsRiskScore;
        this.cReactive = cReactive;
        this.apolprotB = apolprotB;
        this.apolprotA = apolprotA;
        this.lipProteinA = lipProteinA;
        this.smoker = smoker;
        this.famhist = famhist;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getBloodpressure() {
        return bloodpressure;
    }

    public void setBloodpressure(double bloodpressure) {
        this.bloodpressure = bloodpressure;
    }

    public double getReynoldsRiskScore() {
        return reynoldsRiskScore;
    }

    public void setReynoldsRiskScore(double reynoldsRiskScore) {
        this.reynoldsRiskScore = reynoldsRiskScore;
    }

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

    public boolean isSmoker() {
        return smoker;
    }

    public void setSmoker(boolean smoker) {
        this.smoker = smoker;
    }

    public boolean isFamhist() {
        return famhist;
    }

    public void setFamhist(boolean famhist) {
        this.famhist = famhist;
    }

    public double calculateReynoldsRiskScore(){
        double A = (((0.0785)*(age)+(3.271)*Math.log(bloodpressure)+(0.202)*Math.log(cReactive)+(0.00820)*(apolprotB)));
        A=A-(0.00769)*(apolprotA+0.134);
        if(smoker){
            A=A+0.825;
        }
        if(famhist){
            A=A+0.427;
        }
        if(lipProteinA>10&&apolprotB>=100){
            A=A+0.00742*lipProteinA;
        }
        reynoldsRiskScore = (1-Math.pow(0.98756,A-19.848))*100;
        return reynoldsRiskScore;
    }
}
