package com.hotmail.jamesnhendry.fza3077;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
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

    private Button btnSearch,btnStatistics;
    private long difference_In_Years;
    final ArrayList<Visit> visitPastArrayList = new ArrayList<>();
    final ArrayList<Visit> visitFutureArrayList = new ArrayList<>();
    private MaterialToolbar topAppBar;
    int counter = 0;
    private SearchView svsview ;
    private Spinner spnsearch;
    private String searchstuff,queryforsearch;
    private int thisisthebreaker;





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
        btnSearch = findViewById(R.id.btnsearchpatients);
        spnsearch = findViewById(R.id.spnsearchpatient);
        svsview = findViewById(R.id.svsearchpatient);
        btnStatistics = findViewById(R.id.btnStatistics);
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

        btnStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO set up printing and showing statistics based on people gathered in search. WITH PDF
            }
        });



        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStatistics.setVisibility(View.VISIBLE);

                switch(spnsearch.getSelectedItem().toString()) {
                    case "Name":
                        searchPatients(svsview.getQuery().toString(),"name");
                        return;
                    case "Age":
                        searchPatients(svsview.getQuery().toString(),"age");
                        return;
                    case "Location":
                        searchPatients(svsview.getQuery().toString(),"suburb");
                        return;
                    case "My Patients":
                        searchPatients(svsview.getQuery().toString(),"patients");
                        return;
                    default:

                }

            }
        });

        setRecycle();






    }

    public void searchPatients(String patient, String search){
        patients.clear();
        patientAdapter.notifyDataSetChanged();
        searchstuff = patient;
        queryforsearch = search;
        thisisthebreaker = 0;





        //Toast.makeText(getApplicationContext(), search + " ======= " + patient, Toast.LENGTH_SHORT).show();

        db.collection("patient").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    for(DocumentSnapshot snap:queryDocumentSnapshots){

                        Map<String,Object> map = snap.getData();
                        DateAge test = new DateAge((long)map.get("dateOfBirth"));
                        String age = test.getAge() + "";
                        Patient patient1 = new Patient(map.get("name").toString(),age,map.get("gender").toString(),snap.getId(),user.getUid(),map.get("suburb").toString());

                        switch(queryforsearch){
                            case "name":
                                if(patient1.getName().toLowerCase().startsWith(searchstuff.toLowerCase())) {
                                    patients.add(patient1);
                                    System.out.println( patient1.getName() + searchstuff);
                                    patientAdapter.notifyDataSetChanged();
                                }
                                break;
                            case "age":
                                if(patient1.getPhoneNumber().equals(searchstuff)) {
                                    patients.add(patient1);
                                    System.out.println( patient1.getName() + searchstuff);
                                    patientAdapter.notifyDataSetChanged();
                                }
                                break;
                            case "suburb":
                                if(patient1.getLocation().toLowerCase().startsWith(searchstuff.toLowerCase())) {
                                    patients.add(patient1);
                                    System.out.println( patient1.getName() + searchstuff);
                                    patientAdapter.notifyDataSetChanged();
                                }
                                break;
                            case "patients":
                                while(thisisthebreaker == 0) {
                                    populateArray();
                                    patientAdapter.notifyDataSetChanged();
                                    thisisthebreaker+=1;
                                }

                                break;
                            default:


                        }
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
                    Patient pat = new Patient(documentSnapshot.get("name").toString(), age + "", documentSnapshot.get("gender").toString(), documentSnapshot.getId(),name);

                    patients.add(pat);
                }

                recyclPatients = findViewById(R.id.recyclMedicalRecord);
                recyclPatients.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                patientAdapter = new patientAdapter(ClinicianHome.this, patients);
                recyclPatients.setAdapter(patientAdapter);

                patientAdapter.setonItemClicklistener(new patientAdapter.onItemClickListener() {
                    @Override
                    public void onItemClicked(int position) {
                        Intent intent = new Intent(ClinicianHome.this,PatientHome.class);
                        intent.putExtra("UserID",patients.get(position).getPatientID());
                        intent.putExtra("isClinitian",true);
                        intent.putExtra("clinitianname",edtClinitianName.getText().toString());
                        startActivity(intent);
                    }
                });


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

                    Visit visit = new Visit(clinicianId,patientID , date, time,documentSnapshot.getId(),visitCompleted);

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
                        Intent futurevis = new Intent(ClinicianHome.this,NewVisit.class);
                        futurevis.putExtra("visitid",visitFutureArrayList.get(position).getVisitid());
                        futurevis.putExtra("value",position);//if position>0 then do nothing;
                        futurevis.putExtra("isvisitcompleted",visitFutureArrayList.get(position).isIscompleted());
                        futurevis.putExtra("usertype","clinitian");
                        startActivity(futurevis);
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
                        Intent pastvis = new Intent(ClinicianHome.this,NewVisit.class);
                        pastvis.putExtra("visitid",visitPastArrayList.get(position).getVisitid());
                        pastvis.putExtra("value",position);//if position>0 then do nothing;
                        pastvis.putExtra("isvisitcompleted",visitPastArrayList.get(position).isIscompleted());
                        pastvis.putExtra("usertype","clinician");
                        startActivity(pastvis);
                    }
                });

            }
        });
    }

}