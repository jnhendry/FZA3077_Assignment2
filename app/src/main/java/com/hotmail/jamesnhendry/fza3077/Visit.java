package com.hotmail.jamesnhendry.fza3077;

import java.util.Date;

public class Visit {
    private String clinitianID, patientID,visitid;
    private MedicalRecord medicalRecord;
    private String date,time;
    private Patient patty;
    private boolean iscompleted;

    /*
    Every visit will have basic requirements. date, time doc username and patient username.
    Every previous visit will hold just the medical record.
    every patient will have an arraylist of visits. every visit has its own medical record and thus upon completion of a visit a patient/ doctor will be able to compare notes and details.
    the home page of a patient and patient page for doctors will display the latest medical record as well as all old visits with their respective medical record.
     */

    public String getTime() {
        return time;
    }

    public Visit(String clinitianID, String date, String patientID, MedicalRecord medicalRecord) {
        this.clinitianID = clinitianID;
        this.patientID = patientID;
        this.medicalRecord = medicalRecord;
        this.date = date;

    }

    public String getVisitid() {
        return visitid;
    }

    public void setVisitid(String visitid) {
        this.visitid = visitid;
    }

    public boolean isIscompleted() {
        return iscompleted;
    }

    public void setIscompleted(boolean iscompleted) {
        this.iscompleted = iscompleted;
    }

    public Visit(String clinitianID, String patientID, String date, String time, String visitId, boolean iscompleted) {
        this.clinitianID = clinitianID;
        this.patientID = patientID;
        this.date = date;
        this.time = time;
        this.visitid = visitId;
        this.iscompleted = iscompleted;

    }

    public String getClinitianID() {
        return clinitianID;
    }

    public String getPatientID() {
        return patientID;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public String getDate() {
        return date;
    }
}