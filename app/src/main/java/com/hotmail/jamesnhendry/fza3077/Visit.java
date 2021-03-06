package com.hotmail.jamesnhendry.fza3077;

public class Visit {
    private String clinicianID, patientId, visitId;
    private MedicalRecord medicalRecord;
    private String date,time;
    private boolean isCompleted;

    /*
    Every visit will have basic requirements. date, time doc username and patient username.
    Every previous visit will hold just the medical record.
    every patient will have an arraylist of visits. every visit has its own medical record and thus upon completion of a visit a patient/ doctor will be able to compare notes and details.
    the home page of a patient and patient page for doctors will display the latest medical record as well as all old visits with their respective medical record.
     */

    public String getTime() {
        return time;
    }

    public Visit(String clinicianID, String date, String patientID, MedicalRecord medicalRecord) {
        this.clinicianID = clinicianID;
        this.patientId = patientID;
        this.medicalRecord = medicalRecord;
        this.date = date;

    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public boolean isIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Visit(String clinicianID, String patientID, String date, String time, String visitId, boolean iscompleted) {
        this.clinicianID = clinicianID;
        this.patientId = patientID;
        this.date = date;
        this.time = time;
        this.visitId = visitId;
        this.isCompleted = iscompleted;

    }

    public String getClinicianID() {
        return clinicianID;
    }

    public String getPatientID() {
        return patientId;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public String getDate() {
        return date;
    }
}