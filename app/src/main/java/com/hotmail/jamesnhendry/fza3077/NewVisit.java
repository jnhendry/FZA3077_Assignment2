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

    String visitID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_visit);
        boolean completed;
        int isMostRecent;
        String usertype,futureorpast;
        declareelements();
        Intent intent = getIntent();

        visitID = intent.getStringExtra("visitid");//use this to gather from the DB
        isMostRecent = intent.getIntExtra("value",1);
        completed = intent.getBooleanExtra("isvisitcompleted",false);
        usertype = intent.getStringExtra("usertype");
        populateemptyfields(visitID);
        isEditable(isMostRecent);
        whatUser(usertype);
        //TODO make medical record editable for lastest visit for patients
        //TODO make medical record, notes and recommendations editable of the latest visit for clinitians


        if(completed){
            //get the data from the database and populate notes and medical record as well as write out recommendations.
        }else{
            btnaddRecommendation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createnewRecommendation();
                }
            });

            btnaddNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createNewNote();
                }
            });
        }


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
    }

    private void whatUser(String usertype) {
        if(usertype!=null) {
            switch(usertype) {
                case "patient":
                    //make the buttons dissapear to add notes and recommendations
                    return;
                case "clinician":
                    //make buttons appear
                    return;
                default:
                    //throw out an error
            }
        }
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
            MedicalRecord medrec = new MedicalRecord(Double.parseDouble(sbloodp),Double.parseDouble(screac),Double.parseDouble(sapolproB),Double.parseDouble(sapolproA),Double.parseDouble(slipoProt),Double.parseDouble(shemoglo),smokerer,famhitoryry);
            rrs = medrec.calculateReynoldsRiskScore();
            txtreynoldsriskscore.setText(Math.round(rrs)+"%");

            updateVisitData(visitID, medrec,noteArrayList,recommendationArrayList);
            // newvisit.put("")
        }
    }

    private void updateVisitData(String visitId, MedicalRecord medrec, ArrayList<Note> noteArrayList, ArrayList<Recommendation> recommendationArrayList) {


        Map<String,Object> medicalRecordMap = new HashMap<>();
        medicalRecordMap.put("bloodPressure", medrec.getBloodpressure());
        medicalRecordMap.put("creactive", medrec.getcReactive());
        medicalRecordMap.put("apolprotA", medrec.getApolprotA());
        medicalRecordMap.put("apolprotB", medrec.getApolprotB());
        medicalRecordMap.put("hemoglobin", medrec.getHemoglobin());
        medicalRecordMap.put("lipoprotA", medrec.getLipProteinA());
        medicalRecordMap.put("smoker", medrec.isSmoker());
        medicalRecordMap.put("familyHistory", medrec.isFamhist());
        medicalRecordMap.put("reynoldsRiskScore", medrec.getReynoldsRiskScore());


        //Prepare Notes
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

        DocumentReference currentVisit = db.collection("visit").document(visitId);

        currentVisit.update("medicalRecord",medicalRecordMap, "notes", notesArrayToSave, "recommendations", RecommendationArrayToSave, "visitCompleted", true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(NewVisit.this, "Visit Updated", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewVisit.this, "Failed To Update Visit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void isEditable(int val){//set intent to grab a boolean of true if first element of recycler view is selected in PatientHome.class else, set nothing.
        if(val==0){
                //ismostrecent do something here
        }else{
                // is not most recent set edit texts to not editable and remove buttons for patients.
        }
    }
}