package com.hotmail.jamesnhendry.fza3077;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
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
    TextView edtPatientName;
    private MaterialToolbar topAppBar;

    private RecyclerView  recyclerPastVisit, recyclerFutureVisit;
    private visitAdapter visitPastAdapter, visitFutureAdapter;

    final ArrayList<Visit> visitPastArrayList = new ArrayList<>();
    final ArrayList<Visit> visitFutureArrayList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private String userID;
    private boolean isClinitian;
    private String clinitianintent;
    private Button btnSchedule;

    FirebaseFirestore db;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);


        Intent intent = getIntent();
        userID = intent.getStringExtra("UserID");
        isClinitian = intent.getBooleanExtra("isClinitian", false);
        clinitianintent = intent.getStringExtra("clinitianname");
        // View Components Binding
        patientId = findViewById(R.id.patientIdTxt);
        patientFullName = findViewById(R.id.fullNameTxt);
        patientGender = findViewById(R.id.genderTxt);
        patientAge = findViewById(R.id.ageTxt);
        patientDateOfBirth = findViewById(R.id.dateOfBirthTxt);
        patientLocation = findViewById(R.id.locationTxt);
        patientClinician = findViewById(R.id.clinicianNameTxt);
        btnSchedule = findViewById(R.id.btnSchedule);

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

        if(isClinitian) {
            btnSchedule.setVisibility(View.VISIBLE);
            btnSchedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(PatientHome.this);
                    dialog.setContentView(R.layout.popupvisit);
                    final CalendarView calendarView = dialog.findViewById(R.id.clvDate);
                    final Spinner edtTime = dialog.findViewById(R.id.spnTimes);
                    Button btnConfirm = dialog.findViewById(R.id.btnConfirm);
                    TextView txtAppointment = dialog.findViewById(R.id.txtAppointment);
                    //TODO: make calendar events for visits.
                    dialog.show();
                    txtAppointment.setText("Create a new visit with: " + patientFullName.getText().toString());
                    final String[] dateString = new String[1];

                    calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        @Override
                        public void onSelectedDayChange(CalendarView view, int year, int month,
                                                        int dayOfMonth) {
                            String curDate = String.valueOf(dayOfMonth);
                            String Year = String.valueOf(year);
                            String Month = String.valueOf(month + 1);
                            dateString[0] = curDate + "-" + Month + "-" + Year;
                        }
                    });

                    btnConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            final String time = edtTime.getSelectedItem().toString();
                            final FirebaseFirestore db = FirebaseFirestore.getInstance();
                            final String clinicianID = user.getUid();
                            final String patientID = patientId.getText().toString();

                            db.collection("clinician").document(clinicianID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                    if(task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if(document.exists()) {
                                            String cName = document.getData().get("name").toString();
                                            Map<String, Object> visit = new HashMap<>();
                                            visit.put("patientId", patientID);
                                            visit.put("patientName", patientFullName.getText().toString());
                                            visit.put("clinicianId", clinicianID);
                                            visit.put("clinicianName", cName);
                                            visit.put("scheduleStart", time);
                                            visit.put("date", dateString[0]);
                                            visit.put("visitCancelled", false);
                                            visit.put("visitCompleted", false);


                                            db.collection("visit").document().set(visit).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(getApplicationContext(), "boom", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });
                                        }
                                    }
                                }
                            });

                        }
                    });

                    String time;
                    long date;


                }
            });

        }
            //Get and Display Patient Details
            populatePatientDetails();
            populatePatientVisits();


    }

    private void populatePatientDetails(){
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        db.document("patient/"+userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
        if(isClinitian){
            edtPatientName.setText(clinitianintent);
            if(!patientClinician.getText().equals(clinitianintent)){
                ArrayList<Visit> afuture = new ArrayList<>();
                ArrayList<Visit> apast = new ArrayList<>();


                for(int i = 0;i<visitFutureArrayList.size();i++){
                    if(visitFutureArrayList.get(i).getClinitianID().equals(clinitianintent)){
                        afuture.add(visitFutureArrayList.get(i));
                    }
                }
                visitFutureAdapter = new visitAdapter(afuture,PatientHome.this);
                recyclerFutureVisit.setAdapter(visitFutureAdapter);


                for(int i = 0;i<visitPastArrayList.size();i++){
                    if(visitPastArrayList.get(i).getClinitianID().equals(clinitianintent)){
                        apast.add(visitPastArrayList.get(i));
                    }
                }
                visitPastAdapter = new visitAdapter(apast,PatientHome.this);
                recyclerPastVisit.setAdapter(visitPastAdapter);


            }
        }


    }


    private void populatePatientVisits() {

        db.collection("visit").whereEqualTo("patientId", userID).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                visitPastArrayList.clear();
                visitFutureArrayList.clear();
                for (final DocumentSnapshot documentSnapshot : value) {

                    String visitId = documentSnapshot.getId();

                    String date = documentSnapshot.get("date").toString();
                    String time = documentSnapshot.get("scheduleStart").toString();
                    String clinicianId = documentSnapshot.get("clinicianName").toString();
                    String patientID = documentSnapshot.get("patientName").toString();
                    boolean visitCompleted = (boolean) documentSnapshot.get("visitCompleted");


                    Visit visit = new Visit(clinicianId, patientID, date, time,documentSnapshot.getId(),visitCompleted);


                    if (visitCompleted) {
                        visitPastArrayList.add(visit);
                    } else {
                        visitFutureArrayList.add(visit);
                    }
                }

                recyclerFutureVisit.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                visitFutureAdapter = new visitAdapter(visitFutureArrayList, PatientHome.this);
                recyclerFutureVisit.setAdapter(visitFutureAdapter);


                if(isClinitian){
                    visitFutureAdapter.setonItemClicklistener(new visitAdapter.onItemClickListener() {
                        @Override
                        public void onItemClicked(int position) {
                            //TODO handle only clinitian ONCLICKS. ignore patient clicks here.
                            Intent futureVisit = new Intent(PatientHome.this,NewVisit.class);
                            futureVisit.putExtra("visitid",visitFutureArrayList.get(position).getVisitid());
                            futureVisit.putExtra("value",position);//if position>0 then do nothing;
                            futureVisit.putExtra("isvisitcompleted",visitFutureArrayList.get(position).isIscompleted());
                            futureVisit.putExtra("usertype","clinician");
                            startActivity(futureVisit);
                        }
                    });
                }

                recyclerPastVisit.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                visitPastAdapter = new visitAdapter(visitPastArrayList, PatientHome.this);
                recyclerPastVisit.setAdapter(visitPastAdapter);

                visitPastAdapter.setonItemClicklistener(new visitAdapter.onItemClickListener() {
                    @Override
                    public void onItemClicked(int position) {
                        //TODO:handle patient view of visits for patients. handle the IF STATEMENT that will distinguish clinitian or patient.
                        Intent futureVisit = new Intent(PatientHome.this,NewVisit.class);
                        futureVisit.putExtra("visitid",visitPastArrayList.get(position).getVisitid());
                        futureVisit.putExtra("value",position);//if position>0 then do nothing;
                        futureVisit.putExtra("isvisitcompleted",visitPastArrayList.get(position).isIscompleted());
                        futureVisit.putExtra("usertype","patient");
                        startActivity(futureVisit);
                    }
                });


            }
        });
    }
}