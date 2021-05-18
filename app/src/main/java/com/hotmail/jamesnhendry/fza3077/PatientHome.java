package com.hotmail.jamesnhendry.fza3077;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PatientHome extends AppCompatActivity {

    //View Components
    TextView patientId;
    TextView patientFullName;
    TextView patientGender;
    TextView patientAge;
    TextView patientDateOfBirth;
    TextView patientLocation;
    TextView patientClinician;
    TextView edtPatientName;
    private MaterialToolbar topAppBar;

    private RecyclerView  recyclerPastVisit, recyclerFutureVisit;
    private visitAdapter visitPastAdapter, visitFutureAdapter;

    final ArrayList<Visit> visitPastArrayList = new ArrayList<>();
    final ArrayList<Visit> visitFutureArrayList = new ArrayList<>();
    private FirebaseAuth mAuth;

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

        edtPatientName = findViewById(R.id.user_name_banner);

        recyclerFutureVisit = findViewById(R.id.patientFuturerecyclerView);
        recyclerPastVisit = findViewById(R.id.patientRecyclePast);


        topAppBar = findViewById(R.id.topAppBarPatientHome);

        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                System.out.println("You Clicked Log Out");
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                return true;
            }
        });



        //Get and Display Patient Details
        populatePatientDetails();

        populatePatientVisits();

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
                                edtPatientName.setText(fullName);
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


    private void populatePatientVisits() {

        db.collection("visit").whereEqualTo("patientId", user.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                visitPastArrayList.clear();
                visitFutureArrayList.clear();
                for (final DocumentSnapshot documentSnapshot : value) {

                    String date = documentSnapshot.get("date").toString();
                    String time = documentSnapshot.get("scheduleStart").toString();
                    String clinicianId = documentSnapshot.get("clinicianName").toString();
                    String patientID = documentSnapshot.get("patientName").toString();
                    boolean visitCompleted = (boolean) documentSnapshot.get("visitCompleted");

                    Visit visit = new Visit(clinicianId, patientID, date, time);

                    if (visitCompleted) {
                        visitPastArrayList.add(visit);
                    } else {
                        visitFutureArrayList.add(visit);
                    }
                }

                recyclerFutureVisit.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                visitFutureAdapter = new visitAdapter(visitFutureArrayList, PatientHome.this);
                recyclerFutureVisit.setAdapter(visitFutureAdapter);

                visitFutureAdapter.setonItemClicklistener(new visitAdapter.onItemClickListener() {
                    @Override
                    public void onItemClicked(int position) {
                        //TODO handle only clinitian ONCLICKS. ignore patient clicks here.
                    }
                });

                recyclerPastVisit.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                visitPastAdapter = new visitAdapter(visitPastArrayList, PatientHome.this);
                recyclerPastVisit.setAdapter(visitPastAdapter);

                visitPastAdapter.setonItemClicklistener(new visitAdapter.onItemClickListener() {
                    @Override
                    public void onItemClicked(int position) {
                        //TODO:handle patient view of visits for patients. handle the IF STATEMENT that will distinguish clinitian or patient.
                    }
                });
            }
        });
    }
}