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
import android.widget.Toast;

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
import java.util.Map;

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
    int counter = 0;


    //TODO: allow clintians to search all patients...
    //TODO clinitians must see statistics and print the info

    @Override
    public void onBackPressed() {

        counter+=1;
        if(counter>1){
            super.onBackPressed();
        }else{
            Toast.makeText(this, "press back again to exit", Toast.LENGTH_SHORT).show();
        }
    }

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

    public void searchPatients(String patient, String search){
        patients.clear();
        String query;
        switch(search){
            case "Name":
                query = "name";
                return;
            case "Age":
                query = "age";
                return;
            case "Location":
                query = "suburb";
                return;
            default:
                query = "";

        }

        db.collection("patients").whereEqualTo(query,patient).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    for(DocumentSnapshot snap:queryDocumentSnapshots){
                        Map<String,Object> map = snap.getData();
                        Patient patient1 = new Patient(map.get("name").toString(),map.get("age").toString(),map.get("gender").toString(),snap.getId(),user.getUid());
                        patients.add(patient1);
                    }
                }
            }
        });


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

                    DateAge dateAge = new DateAge((long) documentSnapshot.get("dateOfBirth"));
                    int age = dateAge.getAge();

//

                    Patient pat = new Patient(documentSnapshot.get("name").toString(), age + "", documentSnapshot.get("gender").toString(), documentSnapshot.getId(),name);
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

                visitFutureAdapter.setonItemClicklistener(new visitAdapter.onItemClickListener() {
                    @Override
                    public void onItemClicked(int position) {
                        //TODO: handle the visit onclick
                    }
                });

                recyclerPastVisit = findViewById(R.id.recycPastVisitclinitian);
                recyclerPastVisit.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                visitPastAdapter = new visitAdapter(visitPastArrayList, ClinicianHome.this);
                recyclerPastVisit.setAdapter(visitPastAdapter);

                visitPastAdapter.setonItemClicklistener(new visitAdapter.onItemClickListener() {
                    @Override
                    public void onItemClicked(int position) {
                        //TODO: handle the data transfer
                    }
                });

            }
        });
    }

}