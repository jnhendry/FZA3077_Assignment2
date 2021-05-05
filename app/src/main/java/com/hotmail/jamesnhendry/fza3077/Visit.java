package com.hotmail.jamesnhendry.fza3077;

public class Visit {
    private String date, time, clinitianUsername, patientUsername;
    private MedicalRecord medicalRecord;

    /*
    Every visit will have basic requirements. date, time doc username and patient username.
    Every previous visit will hold just the medical record.
    every patient will have an arraylist of visits. every visit has its own medical record and thus upon completion of a visit a patient/ doctor will be able to compare notes and details.
    the home page of a patient and patient page for doctors will display the latest medical record as well as all old visits with their respective medical record.
     */

    public Visit(String date, String time, String clinitianUsername, String patientUsername) {
        this.date = date;
        this.time = time;
        this.clinitianUsername = clinitianUsername;
        this.patientUsername = patientUsername;
    }

    public Visit(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
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

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }
}