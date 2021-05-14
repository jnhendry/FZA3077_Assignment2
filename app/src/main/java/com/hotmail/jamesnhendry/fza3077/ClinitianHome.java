package com.hotmail.jamesnhendry.fza3077;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.List;
import java.util.Locale;

public class ClinitianHome extends AppCompatActivity {

    private Gson gson = new Gson();
    private RecyclerView recyclPatients, recycvisit;
    private patientAdapter patientAdapter;
    private visitAdapter visitAdapter;
    private TextView edtClinitianName;
    private Clinitian cl;
    private FirebaseAuth mAuth;
    private ArrayList<Patient> patients = new ArrayList<>();
    FirebaseFirestore db;
    FirebaseUser user;
    private long difference_In_Years;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinitian_home);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();


        edtClinitianName = findViewById(R.id.txtclinitianname);
        DocumentReference snap = db.collection("Clinitian").document(user.getUid());

        snap.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    edtClinitianName.setText(documentSnapshot.get("name").toString());
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


        db.collection("Patients").whereEqualTo("clinitianID", user.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null) {
                    return;
                }
                for(DocumentSnapshot documentSnapshot : value) {
                    long yearsOld = (long) documentSnapshot.get("dateofbirth");
                    String yearsOldString = new SimpleDateFormat("dd-mm-yyyy", Locale.ENGLISH).format(yearsOld);
                    SimpleDateFormat sdf
                            = new SimpleDateFormat(
                            "dd-MM-yyyy");

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-mm-yyyy");
                    LocalDateTime now = LocalDateTime.now();

                    String currentDate = dtf.format((now));

                    try {
                        Date d1 = sdf.parse(yearsOldString);
                        Date d2 = sdf.parse(currentDate);

                        long difference = d2.getTime() - d1.getTime();


                        difference_In_Years = (difference
                                / (1000l * 60 * 60 * 24 * 365));

                        System.out.println("Difference: " + difference_In_Years);

                    } catch(Exception e) {
                        System.out.println("Something went wrong with converting dates. ");
                    }


                    Patient pat = new Patient(documentSnapshot.get("name").toString(), difference_In_Years + "", documentSnapshot.get("gender").toString(), documentSnapshot.getId());
                    patients.add(pat);
                }
                recyclPatients = findViewById(R.id.recyclMedicalRecord);
                recyclPatients.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                patientAdapter = new patientAdapter(ClinitianHome.this, patients);
                recyclPatients.setAdapter(patientAdapter);

            }
        });


    }

    public void setRecycle() {
        final ArrayList<Visit> visitArrayList = new ArrayList<>();

        db.collection("Visits").whereEqualTo("clinitianID", user.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null) {
                    return;
                }
                for(DocumentSnapshot documentSnapshot : value) {
                    String date = documentSnapshot.get("date").toString();
                    String time = documentSnapshot.get("schedulestart").toString();

                    Visit visit = new Visit(clinitian[0], patient[0], date, time);
                    visitArrayList.add(visit);
                }


                recycvisit = findViewById(R.id.recycVisitclinitian);
                recycvisit.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                visitAdapter = new visitAdapter(visitArrayList, ClinitianHome.this);
                recycvisit.setAdapter(visitAdapter);
            }
        });
    }
}