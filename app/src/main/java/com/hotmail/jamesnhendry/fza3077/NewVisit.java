package com.hotmail.jamesnhendry.fza3077;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class NewVisit extends AppCompatActivity {

    private EditText bloodpressure,creactive,apolprotA,apolprotB,lipoprotA,hemoA;
    private Spinner smoker,famhist;
    private TextView txtclinititanname,txtpatientname,txtpatientGender,txtpatientage,txtpatientlocation,txtreynoldsriskscore;
    private RecyclerView recyclnotes,recyclRecommendation;
    private Button btnaddNote,btnaddRecommendation,btnSaveVisit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_visit);
        boolean value;
        String usertype;
        Intent intent = getIntent();
        value = intent.getBooleanExtra("value",false);
        usertype = intent.getStringExtra("usertype");
        isEditable(value);
        whatUser(usertype);

        declareelements();
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
        switch(usertype){
            case "patient":
                    //make the buttons dissapear to add notes and recommendations
                return;
            case  "clinician":
                //make buttons appear
                return;
            default:
                //throw out an error
        }
    }


    public void createNewNote(){

    }

    public void createnewRecommendation(){

    }

    public void saveVisit(){

    }

    public void isEditable(boolean val){//set intent to grab a boolean of true if first element of recycler view is selected in PatientHome.class else, set nothing.
        if(val){

        }else{

        }
    }
}