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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "";

    //Ui Components
    private Button btnLogin;
    private EditText edtUsername,edtPassword;
    private TextView goToSignUpPageText;

    private FirebaseAuth mAuth;
    private DocumentReference userpatient;
    private DocumentReference userClinitian;
    FirebaseFirestore db;
    FirebaseUser user;
    int counter =0;
    @Override
    public void onStart() {
        super.onStart();
        //TODO
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Connecting View Components
        edtUsername = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        goToSignUpPageText = findViewById(R.id.txtSignUp);

        goToSignUpPageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushToNewActivity("Sign Up");

            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });

        //Firebase FireStore & Authentication Initialization
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }

    public void logIn(){
        Toast.makeText(this, "Loading...", Toast.LENGTH_LONG).show();
        String email,password;
        email = edtUsername.getText().toString();
        password = edtPassword.getText().toString();
        if(!email.isEmpty() && !password.isEmpty()){
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.d(TAG, "signinwithemail:success");
                        user = mAuth.getCurrentUser();
                        userpatient = db.document("patient/"+user.getUid());
                        userClinitian = db.document("clinician/"+user.getUid());

                        userClinitian.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()) {
                                    pushToNewActivity("Clinician Home");
                                    finish();
                                }
                            }
                        });

                        userpatient.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()) {
                                    pushToNewActivity("Patient Home");
                                    finish();
                                }
                            }
                        });

                    }else{
                        Log.w(TAG, "signinwithemail:failure",task.getException() );
                        Toast.makeText(getApplicationContext(),"Invalid email and/or password ",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else{
            Toast.makeText(getApplicationContext(),"Username and/or Password Empty",Toast.LENGTH_SHORT).show();
        }

    }

    public void pushToNewActivity(String activityName){

        Intent intent;
        switch (activityName){
            case "Clinician Home":
                intent = new Intent(MainActivity.this, ClinicianHome.class);
                break;

            case "Patient Home":
                intent = new Intent(MainActivity.this, PatientHome.class);
                break;
            case "Sign Up":
                intent = new Intent(getApplicationContext(),SignUp.class);
                break;
            default:
                intent = new Intent(getApplicationContext(),MainActivity.class);
        }
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        counter+=1;
        if(counter>1){
            super.onBackPressed();
        }else{
            Toast.makeText(this, "press back again to exit", Toast.LENGTH_SHORT).show();
        }
    }
}