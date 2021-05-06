package com.hotmail.jamesnhendry.fza3077;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

public class ClinitianHome extends AppCompatActivity {

    private Gson gson = new Gson();
    private RecyclerView recyclPatients,recycvisit;
    private patientAdapter patientAdapter;
    private visitAdapter visitAdapter;
    private TextView edtClinitianName;
    private  Clinitian cl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinitian_home);
        final Intent intent = getIntent();
        String clinitian = intent.getStringExtra("clinitian") ;
        cl = gson.fromJson(clinitian,Clinitian.class);

        edtClinitianName = findViewById(R.id.txtclinitianname);
        edtClinitianName.setText(cl.getName());


        patientAdapter.setonItemClicklistener(new patientAdapter.onItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                Intent intent1 = new Intent(getApplicationContext(),PatientHome.class);
                String patientDetails = gson.toJson(cl);//put something here
                int pos = position;
                intent1.putExtra("patientdetails",patientDetails);
                intent1.putExtra("position",pos);
                startActivity(intent1);
            }
        });




    }

    public void setRecycle(){
        recyclPatients = findViewById(R.id.recyclMedicalRecord);
        recyclPatients.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        patientAdapter = new patientAdapter(getApplicationContext(), cl.getPatients());
        recyclPatients.setAdapter(patientAdapter);


        recycvisit = findViewById(R.id.recycVisitclinitian);
        recycvisit.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        visitAdapter = new visitAdapter(cl.getFutureVisits(),getApplicationContext());
        recycvisit.setAdapter(visitAdapter);
    }
}