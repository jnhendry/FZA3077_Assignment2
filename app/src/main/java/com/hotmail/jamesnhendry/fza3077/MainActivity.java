package com.hotmail.jamesnhendry.fza3077;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "";

    private Button btnLogin;
    private EditText edtUsername,edtPassword;
    private Gson gson = new Gson();
    private ArrayList<Patient> patients = new ArrayList<>();
    private ArrayList<Clinitian> clinitians = new ArrayList<>();
    private ArrayList<Visit> pastVisits = new ArrayList<>();
    private ArrayList<Visit> futureVisits = new ArrayList<>();
    private ArrayList<MedicalRecord> medicalRecords = new ArrayList<>();
    private FirebaseAuth mAuth;


    @Override
    public void onStart() {
        super.onStart();
        //TODO
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        btnLogin = findViewById(R.id.btnLogin);



        mAuth = FirebaseAuth.getInstance();


        setPC();



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signin();
            }
        });
    }


    public void signin(){
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        String email,password;
        email = edtUsername.getText().toString();
        password = edtPassword.getText().toString();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "signinwithemail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(getApplicationContext(),"Authentication Successful",Toast.LENGTH_SHORT).show();

                }else{
                    Log.w(TAG, "signinwithemail:failure",task.getException() );
                    Toast.makeText(getApplicationContext(),"authentication failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void setPC(){
        patients.clear();
        patients.add(new Patient("fgib1","James Hendry","jhen01","jhen01","0833003093","Male",pastVisits,futureVisits));
        patients.add(new Patient("fgib1","Matthew Mcdaniel","mmcd01","mmcd01","0833002793","Male",pastVisits,futureVisits));
        patients.add(new Patient("fgib1","Jason Henrick","jhen02","jhen02","0833235493","Male",pastVisits,futureVisits));
        patients.add(new Patient("fgib1","Feddy Knight","fkni01","fkni01","0833065763","Male",pastVisits,futureVisits));
        patients.add(new Patient("fgib1","Jack Worthall","jwor01","jwor01","0833087693","Male",pastVisits,futureVisits));


        patients.add(new Patient("char01","Bolívar Adelardi","bade01","bade01","0765003093","Female",pastVisits,futureVisits));
        patients.add(new Patient("char01","Vihaan Mondy","vmon01","vmon01","0833034593","Female",pastVisits,futureVisits));
        patients.add(new Patient("char01","Heidemarie Wendell","hwen01","hwen01","0612003093","Female",pastVisits,futureVisits));
        patients.add(new Patient("char01","Wiley Farkas","wfar01","wfar01","0833004567","Male",pastVisits,futureVisits));
        patients.add(new Patient("char01","Lelio McKinley","lmck01","lmck01","0833985493","Male",pastVisits,futureVisits));


        patients.add(new Patient("dmat01","Viljo Ek","vek001","vek001","0833098793","Female",pastVisits,futureVisits));
        patients.add(new Patient("dmat01","Agneza Pereira","aper01","aper01","0723003093","Female",pastVisits,futureVisits));
        patients.add(new Patient("dmat01","Munyaradzi Nigel","mnig01","mnig01","0633003093","Male",pastVisits,futureVisits));
        patients.add(new Patient("dmat01","Ruslan Clement","rcle01","rcle01","0453003093","Female",pastVisits,futureVisits));
        patients.add(new Patient("dmat01","Elvio Abrahams","eabr01","eabr01","0736003093","Male",pastVisits,futureVisits));


        clinitians.clear();
        clinitians.add(new Clinitian("Fred Gibbs","fgib1","fgib1",patients,pastVisits));
        clinitians.add(new Clinitian("Charlie Harrison","char01","char01",patients,pastVisits));
        clinitians.add(new Clinitian("Daniel Matthews","dmat01","dmat01",patients,pastVisits));


        //want to implement either a MONGODB or just use firebase NOSQL because fok this is getting annoying.
        medicalRecords.clear();
        medicalRecords.add(new MedicalRecord("24/07","15:30","Fred Gibbs","James Hendry","Should stop smoking, keep in mind family history and reduce stress."
                ,22,100,2,100,125,25,true,true));
        medicalRecords.add(new MedicalRecord("25/07","15:30","Fred Gibbs","James Hendry",
                "Should stop smoking, keep in mind family history and reduce stress.",22,125,2.3,110,115,20,false,true));
        medicalRecords.add(new MedicalRecord("26/07","15:30","Fred Gibbs","James Hendry",
                "Should stop smoking, keep in mind family history and reduce stress.",22,100,2,100,125,25,false,false));


        pastVisits.clear();
        pastVisits.add(new Visit("24/07","15:30","Fred Gibbs","James Hendry",medicalRecords.get(0)));
        pastVisits.add(new Visit("25/07","15:30","Fred Gibbs","James Hendry",medicalRecords.get(1)));
        pastVisits.add(new Visit("26/07","15:30","Fred Gibbs","James Hendry",medicalRecords.get(2)));
        pastVisits.add(new Visit("29/08","15:30","Fred Gibbs","James Hendry"));
        pastVisits.add(new Visit("01/09","15:30","Fred Gibbs","James Hendry"));
        pastVisits.add(new Visit("27/09","15:30","Fred Gibbs","James Hendry"));




    }


}