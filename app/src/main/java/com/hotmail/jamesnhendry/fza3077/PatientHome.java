package com.hotmail.jamesnhendry.fza3077;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

public class PatientHome extends AppCompatActivity {

    private Gson gson = new Gson();
    private TextView txtPatientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);
        txtPatientName = findViewById(R.id.txtPatientName);
        Intent intent = getIntent();
        String patient = intent.getStringExtra("patient") ;
        if(patient!=null) {
            Patient pa = gson.fromJson(patient, Patient.class);
            txtPatientName.setText(pa.getName());
        }else{
            String clinitian = intent.getStringExtra("patientdetails");
            int pos = intent.getIntExtra("position",0);
            Clinitian cl = gson.fromJson(clinitian,Clinitian.class);
            if(cl!=null) {
                txtPatientName.setText(cl.getPatients().get(pos).getName());
            }
        }



    }
}