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