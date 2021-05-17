package com.hotmail.jamesnhendry.fza3077;

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

import java.util.ArrayList;

public class NewVisit extends AppCompatActivity {

    private EditText bloodpressure,creactive,apolprotA,apolprotB,lipoprotA,hemoA;
    private Spinner smoker,famhist;
    private TextView txtclinititanname,txtpatientname,txtpatientGender,txtpatientage,txtpatientlocation,txtreynoldsriskscore;
    private RecyclerView recyclnotes,recyclRecommendation;
    private Button btnaddNote,btnaddRecommendation,btnSaveVisit;
    private notes_recommendationadapter adapter;
    private ArrayList<Note> noteArrayList = new ArrayList<>();
    private ArrayList<Recommendation> recommendationArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_visit);
        boolean value,completed;
        String usertype;
        declareelements();
        Intent intent = getIntent();
        String visitID;
        visitID = intent.getStringExtra("visitid");//use this to gather from the DB
        value = intent.getBooleanExtra("value",false);
        completed = intent.getBooleanExtra("isvisitcompleted",false);
        usertype = intent.getStringExtra("usertype");
        populateemptyfields(visitID);
        isEditable(value);
        whatUser(usertype);

        iscompleted(completed);

        btnaddRecommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createnewRecommendation();
            }
        });




        setuprecyclers();
    }

    private void populateemptyfields(String visitID) {

    }

    private void iscompleted(boolean completed) {
        if(completed){
            //get the data from the database and populate notes and medical record as well as write out recommendations.
        }else{
            //everything is empty its a visit in progress
        }
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
        txtpatientname = findViewById(R.id.txtPatientName);
        txtpatientGender = findViewById(R.id.patientGenderMedRec);
        txtpatientage = findViewById(R.id.txtAgeClPatients);
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
            }}
        });

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
            //add all items to the database and update visit statuses.
    }

    public void isEditable(boolean val){//set intent to grab a boolean of true if first element of recycler view is selected in PatientHome.class else, set nothing.
        if(val){

        }else{

        }
    }
}