package com.hotmail.jamesnhendry.fza3077;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewVisit extends AppCompatActivity {

    private EditText bloodpressure,creactive,apolprotA,apolprotB,lipoprotA,hemoA;
    private Spinner smoker,famhist;
    private TextView txtclinititanname,txtpatientname,txtpatientGender,txtpatientage,txtpatientlocation,txtreynoldsriskscore;
    private RecyclerView recyclnotes,recyclRecommendation;
    private Button btnaddNote,btnaddRecommendation,btnSaveVisit;
    private notes_recommendationadapter adapter;
    private ArrayList<Note> noteArrayList = new ArrayList<>();
    private ArrayList<Recommendation> recommendationArrayList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button btnUpdate,btnSimulate;
    String usertype,futureorpast;
    String visitID;
    private MedicalRecord medrec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_visit);
        boolean completed;
        int isMostRecent;

        declareelements();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatenotesandrecommendations(noteArrayList,recommendationArrayList);
            }
        });

        btnaddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewNote();
            }
        });

        btnaddRecommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createnewRecommendation();
            }
        });
        btnSimulate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sbloodp,screac,sapolproA,sapolproB,shemoglo,slipoProt;
                double rrs;
                boolean smokerer,famhitoryry;
                String smokes,famhistory;
                sbloodp =bloodpressure.getText().toString();
                screac = creactive.getText().toString();
                sapolproA = (apolprotA.getText().toString());
                sapolproB=(apolprotB.getText().toString());
                shemoglo = (hemoA.getText().toString());
                slipoProt = (lipoprotA.getText().toString());
                smokes = smoker.getSelectedItem().toString();
                famhistory = famhist.getSelectedItem().toString();
                if((sbloodp.equals(""))||(screac.equals(""))||(sapolproA.equals(""))||(sapolproB.equals(""))||(shemoglo.equals(""))||(slipoProt.equals(""))||(smokes.equals("--Select one--"))||famhistory.equals("--Select one--")){
                    Toast.makeText(NewVisit.this, "Fill in all fields", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(NewVisit.this, "works", Toast.LENGTH_SHORT).show();

                    if(smokes.equals("Yes")){
                        smokerer = true;
                    }else{
                        smokerer = false;
                    }
                    if(famhistory.equals("Yes")){
                        famhitoryry = true;
                    }else {
                        famhitoryry = false;
                    }
                    MedicalRecord medrec = new MedicalRecord(Double.parseDouble(sbloodp),Double.parseDouble(screac),Double.parseDouble(sapolproB),Double.parseDouble(sapolproA),Double.parseDouble(slipoProt),Double.parseDouble(shemoglo),smokerer,famhitoryry);
                    rrs = medrec.calculateReynoldsRiskScore();
                    txtreynoldsriskscore.setText(Math.round(rrs)+"%");
                }
            }});
        Intent intent = getIntent();

        visitID = intent.getStringExtra("visitid");//use this to gather from the DB
        isMostRecent = intent.getIntExtra("value",1);
        completed = intent.getBooleanExtra("isvisitcompleted",false);
        usertype = intent.getStringExtra("usertype");
        Toast.makeText(this, usertype, Toast.LENGTH_SHORT).show();
        populateemptyfields(visitID);

        //isEditable(isMostRecent);

        //TODO make medical record editable for lastest visit for patients
        //TODO make medical record, notes and recommendations editable of the latest visit for clinitians

        if(usertype!=null) {
            switch(usertype) {
                case "patient":
                    btnSimulate.setVisibility(View.VISIBLE);
                    btnUpdate.setVisibility(View.GONE);
                    btnSaveVisit.setVisibility(View.GONE);
                    btnaddNote.setVisibility(View.GONE);
                    btnaddRecommendation.setVisibility(View.GONE);
                    updateviewforcompleted();
                    return;
                case "clinician":
                    if(completed) {
                       btnSaveVisit.setVisibility(View.GONE);
                       btnUpdate.setVisibility(View.VISIBLE);
                       btnSimulate.setVisibility(View.GONE);
                       updateviewforcompleted();
                       makeViewUneditable();
                       return;
                    }else {
                        btnSaveVisit.setVisibility(View.VISIBLE);
                        btnUpdate.setVisibility(View.GONE);
                        btnSimulate.setVisibility(View.GONE);
                    }
                    return;
                default:
                    //throw out an error
            }
        }




            //get the data from the database and populate notes and medical record as well as write out recommendations.




        btnSaveVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:import the pdf viewer and handle the data to the database. PDFs will be local.
                //TODO: handle notifications for clinitians and patients.
               saveVisit();
            }
        });




        setuprecyclers();
    }



    private void updateviewforcompleted() {
        System.out.println(visitID);
        db.collection("visit").document(visitID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()) {
                    double bp, cr, apa, apb, lipa, hem,rrs;
                    boolean sm, fh;
                    Map<String,Object> map = documentSnapshot.getData();
                    Map<String,Object> mr = (Map<String, Object>) map.get("medicalRecord");
                    ArrayList<Map> notes = (ArrayList<Map>) map.get("notes");
                    ArrayList<Map> recos =(ArrayList<Map>) map.get("recommendations");

                     bp = (double) mr.get("bloodPressure");
                    cr = (double) mr.get("creactive");
                    apa = (double) mr.get("apolprotA");
                    apb = (double) mr.get("apolprotB");
                    lipa = (double) mr.get("lipoprotA");
                    hem = (double) mr.get("hemoglobin");
                    sm = (boolean) mr.get("smoker");
                    fh = (boolean) mr.get("familyHistory");
                    rrs = (double) mr.get("reynoldsRiskScore");
                    MedicalRecord medicalRecord = new MedicalRecord(bp, cr, apb, apa, lipa, hem, sm, fh);//put shit here


                    for(Map maps : notes) {
                        String body,description;
                        body = maps.get("body").toString();
                        description=maps.get("subject").toString();
                        Note note = new Note(body, description);
                        noteArrayList.add(note);
                    }
                    for(Map maps : recos) {
                        Recommendation recommendation = new Recommendation(maps.get("body").toString(), maps.get("subject").toString());
                        recommendationArrayList.add(recommendation);
                    }

                    setuprecyclers();
                    bloodpressure.setText(bp+"");
                    creactive.setText( cr+"");
                    apolprotA.setText( apa+"");
                    apolprotB.setText(apb+"");
                    lipoprotA.setText( lipa+"");
                    hemoA.setText(hem+"");
                    txtreynoldsriskscore.setText(Math.round(rrs)+"%");

                    if(medicalRecord.isSmoker()) {
                        smoker.setSelection(1);
                    } else {
                        smoker.setSelection(2);
                    }
                    if(medicalRecord.isFamhist()) {
                        famhist.setSelection(1);
                    } else {
                        famhist.setSelection(2);

                    }


                }
                }

        });

    }



    private void populateemptyfields(String visitID) {
        db.collection("visit").document(visitID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                txtclinititanname.setText(documentSnapshot.get("clinicianName").toString());
                txtpatientname.setText(documentSnapshot.get("patientName").toString());

                db.collection("patient").document(documentSnapshot.get("patientId").toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        txtpatientGender.setText(documentSnapshot.get("gender").toString());
                        txtpatientlocation.setText(documentSnapshot.get("suburb").toString());
                        DateAge age = new DateAge((long)documentSnapshot.get("dateOfBirth"));

                        txtpatientage.setText(age.getAge()+"");

                    }
                });
            }
        });
    }



    private void declareelements() {
        //please do this
        bloodpressure = findViewById(R.id.bloodpressureMedRec);
        creactive = findViewById(R.id.creactiveMedRec);
        apolprotA = findViewById(R.id.apolipoproteinAMedRec);
        apolprotB = findViewById(R.id.apolipoproteinBMedRec);
        lipoprotA = findViewById(R.id.lipoproteinAMedRec);
        hemoA = findViewById(R.id.HemoglobinA1MedRec);
        smoker = findViewById(R.id.spnSmokerMedRec);
        famhist = findViewById(R.id.spnFamilyHistory);
        txtclinititanname = findViewById(R.id.txtClintiannameMedRec);
        txtpatientname = findViewById(R.id.patientnameMedRec);
        txtpatientGender = findViewById(R.id.patientGenderMedRec);
        txtpatientage = findViewById(R.id.txtPatientAgeMedical);
        txtpatientlocation = findViewById(R.id.patientLocationMedRec);
        txtreynoldsriskscore = findViewById(R.id.reynoldsriskscoreMedRec);
        recyclnotes = findViewById(R.id.recyclNotes);
        recyclRecommendation = findViewById(R.id.recyclRecommendations);
        btnaddNote = findViewById(R.id.addNote);
        btnaddRecommendation = findViewById(R.id.addRecom);
        btnSaveVisit = findViewById(R.id.btnSaveVisit);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnSimulate = findViewById(R.id.btnSimulate);
    }



    public void createNewNote(){
        //add a new note to the arraylist and update the recycler view. notifydatsetchanged
        final EditText nBody, nDescription;
        Button saveNote;
        final Dialog popup = new Dialog(this);
        popup.setContentView(R.layout.notes_popup);

        nBody = popup.findViewById(R.id.noteBody);
        nDescription = popup.findViewById(R.id.noteSubject);
        saveNote = popup.findViewById(R.id.newNote);
        popup.show();

        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nbody, nsubject;
                nbody = nBody.getText().toString();
                nsubject = nDescription.getText().toString();
                if (nbody.equals("")||nsubject.equals("")){
                    Toast.makeText(NewVisit.this, "Do not leave fields empty", Toast.LENGTH_SHORT).show();
                }else {
                    Note note = new Note(nbody,nsubject);
                    noteArrayList.add(note);
                }

                popup.hide();
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void createnewRecommendation(){
        //add a new recommendation to the arraylist and update the recycler view. notifydatsetchanged
        final Dialog dialog = new Dialog( NewVisit.this);
        dialog.setContentView(R.layout.notes_popup);
        final EditText body,description;
        Button save;
        body = dialog.findViewById(R.id.noteBody);
        description = dialog.findViewById(R.id.noteSubject);
        save = dialog.findViewById(R.id.newNote);

        dialog.show();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String notebody,notesubject;
                notebody = body.getText().toString();
                notesubject = description.getText().toString();
                if(notebody.equals("")||notesubject.equals("")){
                    Toast.makeText(NewVisit.this, "Do not leave fields empty", Toast.LENGTH_SHORT).show();
                }else{
                Recommendation recommendation = new Recommendation(notebody,notesubject);
                recommendationArrayList.add(recommendation);
                    dialog.hide();
            }}
        });

        adapter.notifyDataSetChanged();
    }

    public void setuprecyclers(){
        recyclnotes.setLayoutManager(new LinearLayoutManager(NewVisit.this));
        adapter = new notes_recommendationadapter(NewVisit.this,noteArrayList);
        recyclnotes.setAdapter(adapter);

        recyclRecommendation.setLayoutManager(new LinearLayoutManager(NewVisit.this));
        adapter = new notes_recommendationadapter(recommendationArrayList,NewVisit.this);
        recyclRecommendation.setAdapter(adapter);
    }

    public void saveVisit(){
        String sbloodp,screac,sapolproA,sapolproB,shemoglo,slipoProt;
        double rrs;
        boolean smokerer,famhitoryry;
        String smokes,famhistory;
        sbloodp =bloodpressure.getText().toString();
        screac = creactive.getText().toString();
        sapolproA = (apolprotA.getText().toString());
        sapolproB=(apolprotB.getText().toString());
        shemoglo = (hemoA.getText().toString());
        slipoProt = (lipoprotA.getText().toString());
        smokes = smoker.getSelectedItem().toString();
        famhistory = famhist.getSelectedItem().toString();
        if((sbloodp.equals(""))||(screac.equals(""))||(sapolproA.equals(""))||(sapolproB.equals(""))||(shemoglo.equals(""))||(slipoProt.equals(""))||(smokes.equals("--Select one--"))||famhistory.equals("--Select one--")){
            Toast.makeText(NewVisit.this, "Fill in all fields", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(NewVisit.this, "works", Toast.LENGTH_SHORT).show();

            if(smokes.equals("Yes")){
                smokerer = true;
            }else{
                smokerer = false;
            }
            if(famhistory.equals("Yes")){
                famhitoryry = true;
            }else {
                famhitoryry = false;
            }
             medrec = new MedicalRecord(Double.parseDouble(sbloodp),Double.parseDouble(screac),Double.parseDouble(sapolproB),Double.parseDouble(sapolproA),Double.parseDouble(slipoProt),Double.parseDouble(shemoglo),smokerer,famhitoryry);
            rrs = medrec.calculateReynoldsRiskScore();
            txtreynoldsriskscore.setText(Math.round(rrs)+"%");

            updateVisitMedicalRecord(visitID, medrec);
            updatenotesandrecommendations(noteArrayList,recommendationArrayList);
            // newvisit.put("")
        }
    }
    private void updatenotesandrecommendations(ArrayList<Note> noteArrayList, ArrayList<Recommendation> recommendationArrayList) {
        ArrayList<Map> notesArrayToSave = new ArrayList<>();


        for (int i = 0; i < noteArrayList.size(); i++){
            Map<String,Object> singleNote= new HashMap<>();
            singleNote.put("subject", noteArrayList.get(i).getDescription());
            singleNote.put("body",  noteArrayList.get(i).getBody());
            notesArrayToSave.add(singleNote);
        }

        // Prepare Recommendations

        ArrayList<Map> RecommendationArrayToSave = new ArrayList<>();

        for (int i = 0; i < recommendationArrayList.size(); i++){
            Map<String,Object> singleRecommendation= new HashMap<>();
            singleRecommendation.put("subject", recommendationArrayList.get(i).getDescription());
            singleRecommendation.put("body",  recommendationArrayList.get(i).getBody());
            RecommendationArrayToSave.add(singleRecommendation);
        }

        //Updating The Visit

        DocumentReference currentVisit = db.collection("visit").document(visitID);

        currentVisit.update( "notes", notesArrayToSave, "recommendations", RecommendationArrayToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                btnSaveVisit.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.VISIBLE);
                makeViewUneditable();
                Toast.makeText(NewVisit.this, "Visit Updated", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewVisit.this, "Failed To Update Visit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateVisitMedicalRecord(String visitId, MedicalRecord medrec) {


        Map<String,Object> medicalRecordMap = new HashMap<>();
        medicalRecordMap.put("bloodPressure",medrec.getBloodpressure());
        medicalRecordMap.put("creactive", medrec.getcReactive());
        medicalRecordMap.put("apolprotA", medrec.getApolprotA());
        medicalRecordMap.put("apolprotB", medrec.getApolprotB());
        medicalRecordMap.put("hemoglobin", medrec.getHemoglobin());
        medicalRecordMap.put("lipoprotA", medrec.getLipProteinA());
        medicalRecordMap.put("smoker", medrec.isSmoker());
        medicalRecordMap.put("familyHistory", medrec.isFamhist());
        medicalRecordMap.put("reynoldsRiskScore", medrec.getReynoldsRiskScore());




        DocumentReference currentVisit = db.collection("visit").document(visitId);

        currentVisit.update("medicalRecord",medicalRecordMap, "visitCompleted", true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                btnSaveVisit.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.VISIBLE);
                makeViewUneditable();
                Toast.makeText(NewVisit.this, "Visit Updated", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewVisit.this, "Failed To Update Visit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeViewUneditable() {
        bloodpressure.setKeyListener(null);
        creactive.setKeyListener(null);
        apolprotA.setKeyListener(null);
        apolprotB.setKeyListener(null);
        hemoA.setKeyListener(null);
        lipoprotA.setKeyListener(null);
        smoker.setEnabled(false);
        famhist.setEnabled(false);
    }

    public void isEditable(int val){//set intent to grab a boolean of true if first element of recycler view is selected in PatientHome.class else, set nothing.
        if(val==0&&usertype.equals("patient")){
                btnSaveVisit.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.GONE);
                btnSimulate.setVisibility(View.VISIBLE);
        }else{
               makeViewUneditable();
        }
    }
}