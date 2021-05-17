package com.hotmail.jamesnhendry.fza3077;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Date;
import java.util.Map;

public class PatientHome extends AppCompatActivity {

    //View Components
    TextView patientId;
    TextView patientFullName;
    TextView patientGender;
    TextView patientAge;
    TextView patientDateOfBirth;
    TextView patientLocation;
    TextView patientClinician;



    private Gson gson = new Gson();

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

        // View Components Binding
        patientId = findViewById(R.id.patientIdTxt);
        patientFullName  = findViewById(R.id.fullNameTxt);
        patientGender = findViewById(R.id.genderTxt);
        patientAge = findViewById(R.id.ageTxt);
        patientDateOfBirth = findViewById(R.id.dateOfBirthTxt);
        patientLocation = findViewById(R.id.locationTxt);
        patientClinician = findViewById(R.id.clinicianNameTxt);

        //Get and Display Patient Details
        populatePatientDetails();

//        recyclFutureVisit = findViewById(R.id.patientFuturerecyclerView);
//        recyclFutureVisit.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//
//        recyclVisit = findViewById(R.id.patientRecyclePast);
//        recyclVisit.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//
//        recyclMedRec = findViewById(R.id.recyclMedicalRecord);
//        recyclMedRec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));




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
                recyclFutureVisit.setAdapter(futureVisitAdapter);
                recyclVisit.setAdapter(pastVisitAdapter);
                recyclMedRec.setAdapter(medicalRecordAdapter);
            }
        }

*/

    }

    private void populatePatientDetails(){
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        db.document("patient/"+mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){



                    final String id = documentSnapshot.getId();
                    final String fullName = documentSnapshot.get("name").toString();
                    final String gender = documentSnapshot.get("gender").toString();

                    DateAge dateAge = new DateAge((long) documentSnapshot.get("dateOfBirth"));
                    final int age = dateAge.getAge();
                    final String datOfBirth = dateAge.getDateOfBirth();
                    final String location = documentSnapshot.get("suburb").toString();

                    db.document("clinician/"+documentSnapshot.get("clinicianId")).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){

                                displayPatientDetails(id, fullName,gender, age + " ", datOfBirth, location,documentSnapshot.get("name").toString());
                            }
                        }
                    });

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PatientHome.this, "Error: Something went Wrong getting data.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void displayPatientDetails(String id, String fullName, String gender, String age, String dateOfBirth, String location, String clinicianName ){
        patientId.setText(id);
        patientFullName.setText(fullName);
        patientGender.setText(gender);
        patientAge.setText(age);
        patientDateOfBirth.setText(dateOfBirth);
        patientLocation.setText(location);
        patientClinician.setText(clinicianName);
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

                recyclFutureVisit = findViewById(R.id.patientFuturerecyclerView);
                recyclFutureVisit.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                visitAdapter visitAdapter = new visitAdapter(visits,getApplicationContext());
                recyclFutureVisit.setAdapter(visitAdapter);

//                recyclMedRec = findViewById(R.id.recyclMedicalRecord);
//                recyclMedRec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                recyclMedRec.setAdapter(medicalRecordAdapter);

               // System.out.println(medtemp.get(0).getBloodpressure());

            }
        });






    }
}