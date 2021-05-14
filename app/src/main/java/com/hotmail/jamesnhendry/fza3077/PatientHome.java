package com.hotmail.jamesnhendry.fza3077;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class PatientHome extends AppCompatActivity {

    private Gson gson = new Gson();
    private TextView txtPatientName;
    Visit temp ;
    ArrayList<MedicalRecord> medtemp = new ArrayList<>();
    MedicalRecord med;
    RecyclerView recyclFutureVisit,recyclVisit,recyclMedRec;
    private FirebaseAuth mAuth;
    private ArrayList<Visit> visits = new ArrayList<>();

    FirebaseFirestore db;
    FirebaseUser user;

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


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        db.document("Patients/"+mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                txtPatientName.setText(documentSnapshot.get("name").toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PatientHome.this, "Boo", Toast.LENGTH_SHORT).show();
            }
        });
        populateArray();



/*

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


*/




    }


    private void populateArray() {


        db.collection("Visits").whereEqualTo("patientID",user.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    return;
                }

                for(DocumentSnapshot documentSnapshot:value) {
                    Map<String,Object> map = documentSnapshot.getData();
                    String clinitianname = map.get("clinitianname").toString();
                    String patientname = map.get("patientname").toString();
                    String date = map.get("date").toString();
                    String time = map.get("schedulestart").toString();
                    Boolean set = (Boolean) map.get("visitCompleted");
                    //if(!set) {
                        ///Visit pat = new Visit(clinitianname,patientname,date,time);//add medical record shit
                        //visits.add(pat);

                  //  }else{
                        Visit pat = new Visit(clinitianname,patientname,date,time);
                        visits.add(pat);
                  //  }

                   // medtemp.add(pat.getMedicalRecord());
                }

                recyclFutureVisit = findViewById(R.id.recycFuturevisit);
                recyclFutureVisit.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                visitAdapter visitAdapter = new visitAdapter(visits,getApplicationContext());
                recyclFutureVisit.setAdapter(visitAdapter);

//                recyclMedRec = findViewById(R.id.recyclMedicalRecord);
//                recyclMedRec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                MedicalRecordAdapter medicalRecordAdapter = new MedicalRecordAdapter(getApplicationContext(), medtemp);
//                recyclMedRec.setAdapter(medicalRecordAdapter);

               // System.out.println(medtemp.get(0).getBloodpressure());

            }
        });






    }
}