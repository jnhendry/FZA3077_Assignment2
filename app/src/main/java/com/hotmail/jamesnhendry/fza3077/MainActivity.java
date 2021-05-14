package com.hotmail.jamesnhendry.fza3077;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private DocumentReference userpatient;
    private DocumentReference userClinitian;
    FirebaseFirestore db;
    FirebaseUser user;
    private TextView txtSignup;


    @Override
    public void onStart() {
        super.onStart();
        //TODO
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        }
        //updateUI(currentUser);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       db = FirebaseFirestore.getInstance();


        btnLogin = findViewById(R.id.btnLogin);
        txtSignup = findViewById(R.id.txtSignUp);

        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SignUp.class);
                startActivity(intent);
            }
        });



        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signin();
            }
        });
    }


    public void signin(){
        edtUsername = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        String email,password;
        email = edtUsername.getText().toString();
        password = edtPassword.getText().toString();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "signinwithemail:success");
                     user = mAuth.getCurrentUser();
                      userpatient = db.document("Patients/"+user.getUid());
                      userClinitian = db.document("Clinitian/"+user.getUid());


                    userClinitian.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()) {
                                Toast.makeText(MainActivity.this, "DOC!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, ClinitianHome.class);
                                startActivity(intent);
                            }
                        }
                    });
                    userpatient.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()) {
                                Toast.makeText(MainActivity.this, "MARTY!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(MainActivity.this, PatientHome.class);
                                startActivity(intent);
                            }
                        }
                    });

                }else{
                    Log.w(TAG, "signinwithemail:failure",task.getException() );
                    Toast.makeText(getApplicationContext(),"authentication failed, check email and/or password or create a new Account",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }






}