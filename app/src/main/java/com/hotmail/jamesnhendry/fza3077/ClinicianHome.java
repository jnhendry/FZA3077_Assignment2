package com.hotmail.jamesnhendry.fza3077;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ClinicianHome extends AppCompatActivity {

    private Gson gson = new Gson();
    private RecyclerView recyclPatients, recyclerPastVisit, recyclerFutureVisit;
    private patientAdapter patientAdapter;
    private visitAdapter visitPastAdapter, visitFutureAdapter;
    private TextView edtClinitianName;
    private Clinitian cl;
    private String name;
    private FirebaseAuth mAuth;
    private ArrayList<Patient> patients = new ArrayList<>();
    FirebaseFirestore db;
    FirebaseUser user;
    private long difference_In_Years;
    final ArrayList<Visit> visitPastArrayList = new ArrayList<>();
    final ArrayList<Visit> visitFutureArrayList = new ArrayList<>();
    private MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinitian_home);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        edtClinitianName = findViewById(R.id.user_name_banner);
        DocumentReference snap = db.collection("clinician").document(user.getUid());

        topAppBar = findViewById(R.id.topAppBar);

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



        snap.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    edtClinitianName.setText(documentSnapshot.get("name").toString());
                    name = documentSnapshot.get("name").toString();
                }
            }
        });

        populateArray();
        setRecycle();

    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();


    }

    private void populateArray() {

        db.collection("patient").whereEqualTo("clinicianId", user.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null) {
                    return;
                }
                for(DocumentSnapshot documentSnapshot : value) {
                    long yearsOld = (long) documentSnapshot.get("dateOfBirth");
                    String yearsOldString = new SimpleDateFormat("dd-mm-yyyy", Locale.ENGLISH).format(yearsOld);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-mm-yyyy");
                    LocalDateTime now = LocalDateTime.now();

                    String currentDate = dtf.format((now));

                    try {
                        Date d1 = sdf.parse(yearsOldString);
                        Date d2 = sdf.parse(currentDate);

                        long difference = d2.getTime() - d1.getTime();

                        difference_In_Years = (difference / (1000L * 60 * 60 * 24 * 365));

                        System.out.println("Difference: " + difference_In_Years);

                    } catch(Exception e) {
                        System.out.println("Something went wrong with converting dates. ");
                    }

                    Patient pat = new Patient(documentSnapshot.get("name").toString(), difference_In_Years + "", documentSnapshot.get("gender").toString(), documentSnapshot.getId(),name);
                    patients.add(pat);
                }

                recyclPatients = findViewById(R.id.recyclMedicalRecord);
                recyclPatients.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                patientAdapter = new patientAdapter(ClinicianHome.this, patients);
                recyclPatients.setAdapter(patientAdapter);
            }
        });


    }



    public void setRecycle() {

        db.collection("visit").whereEqualTo("clinicianId", user.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null) {
                    return;
                }
                visitPastArrayList.clear();
                visitFutureArrayList.clear();
                for( final DocumentSnapshot documentSnapshot : value) {

                    String date = documentSnapshot.get("date").toString();
                    String time = documentSnapshot.get("scheduleStart").toString();
                    String clinicianId = documentSnapshot.get("clinicianName").toString();
                    String patientID = documentSnapshot.get("patientName").toString();
                    boolean visitCompleted = (boolean) documentSnapshot.get("visitCompleted");

                    Visit visit = new Visit(clinicianId,patientID , date, time);

                    if (visitCompleted){
                        visitPastArrayList.add(visit);
                    }
                    else{
                        visitFutureArrayList.add(visit);
                    }
                }
                recyclerFutureVisit = findViewById(R.id.recycFutureVisitclinitian);
                recyclerFutureVisit.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                visitFutureAdapter = new visitAdapter(visitFutureArrayList, ClinicianHome.this);
                recyclerFutureVisit.setAdapter(visitFutureAdapter);

                recyclerPastVisit = findViewById(R.id.recycPastVisitclinitian);
                recyclerPastVisit.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                visitPastAdapter = new visitAdapter(visitPastArrayList, ClinicianHome.this);
                recyclerPastVisit.setAdapter(visitPastAdapter);

            }
        });
    }

}