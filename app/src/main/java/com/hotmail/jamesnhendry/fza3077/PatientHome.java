package com.hotmail.jamesnhendry.fza3077;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class PatientHome extends AppCompatActivity {

    private Gson gson = new Gson();
    private TextView txtPatientName;
    Visit temp ;
    ArrayList<MedicalRecord> medtemp = new ArrayList<>();
    MedicalRecord med;
    RecyclerView recyclFutureVisit,recyclVisit,recyclMedRec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);
        recyclFutureVisit = findViewById(R.id.recycFuturevisit);
        recyclFutureVisit.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recyclVisit = findViewById(R.id.recycVisitclinitian);
        recyclVisit.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recyclMedRec = findViewById(R.id.recyclMedicalRecord);
        recyclMedRec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        txtPatientName = findViewById(R.id.txtPatientName);
        Intent intent = getIntent();
        String patient = intent.getStringExtra("patient") ;
        if(patient!=null) {
            Patient pa = gson.fromJson(patient, Patient.class);
            txtPatientName.setText(pa.getName());
            visitAdapter pastVisitAdapter = new visitAdapter(pa.getPastVisits(),this);
            visitAdapter futureVisitAdapter = new visitAdapter(pa.getFutureVisits(),this);
            temp = pa.getPastVisits().get(pa.getPastVisits().size()-1);
            med = temp.getMedicalRecord();
            medtemp.clear();
            med.setReynoldsRiskScore(med.calculateReynoldsRiskScore());
            medtemp.add(med);

            MedicalRecordAdapter medicalRecordAdapter = new MedicalRecordAdapter(this,medtemp);
            recyclFutureVisit.setAdapter(futureVisitAdapter);
            recyclVisit.setAdapter(pastVisitAdapter);
            recyclMedRec.setAdapter(medicalRecordAdapter);

        }else{
            String clinitian = intent.getStringExtra("patientdetails");
            int pos = intent.getIntExtra("position",0);
            Clinitian cl = gson.fromJson(clinitian,Clinitian.class);
            if(cl!=null) {
                txtPatientName.setText(cl.getPatients().get(pos).getName());
                visitAdapter pastVisitAdapter = new visitAdapter(cl.getPatients().get(pos).getPastVisits(),this);
                visitAdapter futureVisitAdapter = new visitAdapter(cl.getPatients().get(pos).getFutureVisits(),this);
                temp = cl.getPatients().get(pos).getPastVisits().get(cl.getPatients().get(pos).getPastVisits().size()-1);
                med = temp.getMedicalRecord();
                medtemp.clear();
                med.setReynoldsRiskScore(med.calculateReynoldsRiskScore());
                medtemp.add(med);
                MedicalRecordAdapter medicalRecordAdapter = new MedicalRecordAdapter(this,medtemp);
                recyclFutureVisit.setAdapter(futureVisitAdapter);
                recyclVisit.setAdapter(pastVisitAdapter);
                recyclMedRec.setAdapter(medicalRecordAdapter);
            }
        }







    }
}