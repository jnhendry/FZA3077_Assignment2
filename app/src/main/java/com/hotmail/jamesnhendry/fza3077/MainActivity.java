package com.hotmail.jamesnhendry.fza3077;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button btnLogin;
    private EditText edtUsername,edtPassword;
    private Gson gson = new Gson();
    private ArrayList<Patient> patients = new ArrayList<>();
    private ArrayList<Clinitian> clinitians = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setPC();

        btnLogin = findViewById(R.id.btnLogin);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Patient pat:patients){
                    if((pat.getUsername().equalsIgnoreCase(edtUsername.getText().toString()))&&(pat.getPassword().equalsIgnoreCase(edtPassword.getText().toString()))){
                        Intent intent = new Intent(getApplicationContext(),PatientHome.class);
                        String patientinfo = gson.toJson(pat);
                        intent.putExtra("patient",patientinfo);
                        startActivity(intent);
                    }
                }
                for(Clinitian cl:clinitians){
                    if((cl.getUsername().equalsIgnoreCase(edtUsername.getText().toString()))&&(cl.getPassword().equalsIgnoreCase(edtPassword.getText().toString()))){
                        Intent intent = new Intent(getApplicationContext(),ClinitianHome.class);
                        String clinitianinfo = gson.toJson(cl);
                        intent.putExtra("clinitian",clinitianinfo);
                        startActivity(intent);
                    }
                }



            }

        });
    }


    public void setPC(){
        patients.clear();
        patients.add(new Patient("fgib1","James Hendry","jhen01","jhen01"));
        patients.add(new Patient("fgib1","Matthew Mcdaniel","mmcd01","mmcd01"));
        patients.add(new Patient("fgib1","Jason Henrick","jhen02","jhen02"));
        patients.add(new Patient("fgib1","Feddy Knight","fkni01","fkni01"));
        patients.add(new Patient("fgib1","Jack Worthall","jwor01","jwor01"));


        patients.add(new Patient("char01","Bolívar Adelardi","bade01","bade01"));
        patients.add(new Patient("char01","Vihaan Mondy","vmon01","vmon01"));
        patients.add(new Patient("char01","Heidemarie Wendell","hwen01","hwen01"));
        patients.add(new Patient("char01","Wiley Farkas","wfar01","wfar01"));
        patients.add(new Patient("char01","Lelio McKinley","lmck01","lmck01"));


        patients.add(new Patient("dmat01","Viljo Ek","vek001","vek001"));
        patients.add(new Patient("dmat01","Agneza Pereira","aper01","aper01"));
        patients.add(new Patient("dmat01","Munyaradzi Nigel","mnig01","mnig01"));
        patients.add(new Patient("dmat01","Ruslan Clement","rcle01","rcle01"));
        patients.add(new Patient("dmat01","Elvio Abrahams","eabr01","eabr01"));


        clinitians.clear();
        clinitians.add(new Clinitian("Fred Gibbs","fgib1","fgib1",patients));
        clinitians.add(new Clinitian("Charlie Harrison","char01","char01",patients));
        clinitians.add(new Clinitian("Daniel Matthews","dmat01","dmat01",patients));
    }


}