package com.hotmail.jamesnhendry.fza3077;

//This class Represents the data model structure of a Medical Record of this platform.
public class MedicalRecord {
    private String dateUpdated,time, clinicianName,patientName;
    private int age;
    private double bloodPressure,reynoldsRiskScore,cReactive,apolprotB,apolprotA,lipProteinA,hemoglobin;
    private boolean smoker;
    private boolean familyHistory;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public MedicalRecord() {
    }

    public MedicalRecord( double bloodPressure, double cReactive, double apolprotB, double apolprotA, double lipProteinA,double hemoglobin, boolean smoker, boolean familyHistory) {

        this.bloodPressure = bloodPressure;
        this.cReactive = cReactive;
        this.apolprotB = apolprotB;
        this.apolprotA = apolprotA;
        this.lipProteinA = lipProteinA;
        this.smoker = smoker;
        this.familyHistory = familyHistory;
        this.hemoglobin = hemoglobin;
    }

    public double getHemoglobin() {
        return hemoglobin;
    }

    public void setHemoglobin(double hemoglobin) {
        this.hemoglobin = hemoglobin;
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

    public String getClinicianName() {
        return clinicianName;
    }

    public void setClinicianName(String clinicianName) {
        this.clinicianName = clinicianName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public double getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(double bloodpressure) {
        this.bloodPressure = bloodpressure;
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

    public boolean isFamilyHistory() {
        return familyHistory;
    }

    public void setFamilyHistory(boolean familyHistory) {
        this.familyHistory = familyHistory;
    }

    public double calculateReynoldsRiskScore(){

        if(age<40) {
            age += (65 - age);
        }
        double A = (((0.0785)*(age)+(3.271)*Math.log(bloodPressure)+(0.202)*Math.log(cReactive)+(0.00820)*(apolprotB)));
        A=A-(0.00769)*(apolprotA+0.134);

        if(smoker){
            A=A+0.825;
        }
        if(familyHistory){
            A=A+0.427;
        }
        if(lipProteinA>10&&apolprotB>=100){
            A=A+0.00742*lipProteinA;
        }
        reynoldsRiskScore = (1-Math.pow(0.98756,A-19.848))*100;
        return reynoldsRiskScore+10;
    }
}
