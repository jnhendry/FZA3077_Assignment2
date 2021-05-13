package com.hotmail.jamesnhendry.fza3077;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SignUp extends AppCompatActivity {

    private FragmentTransaction ft;

    private final Fragment signUpClinitian = signupClinitian.newInstance("","");
    private final Fragment signUpPatients = signupPatient.newInstance("","");

    private Button btnPatient,btnCllinitian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        ft = getSupportFragmentManager().beginTransaction();
        openFragment(signUpPatients);

        btnPatient = findViewById(R.id.btntogglePatientSU);
        btnCllinitian = findViewById(R.id.btnToggleClinitianSU);

        btnPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(signUpPatients);
                btnCllinitian.setAlpha((float) 0.3);
            }
        });

        btnCllinitian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(signUpClinitian);
            }
        });


    }

    public void openFragment(Fragment fragment) {
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frmContain, fragment);
        ft.commit();
    }
}