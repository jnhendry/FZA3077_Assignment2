package com.hotmail.jamesnhendry.fza3077;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    private FragmentTransaction ft;

    private final Fragment signUpClinician = signupClinician.newInstance("","");
    private final Fragment signUpPatients = signupPatient.newInstance("","");

    private Button btnPatient, btnClinician;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        ft = getSupportFragmentManager().beginTransaction();

        btnPatient = findViewById(R.id.btntogglePatientSU);
        btnClinician = findViewById(R.id.btnToggleClinitianSU);


        btnPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(signUpPatients);
                btnPatient.setAlpha(1);
                btnClinician.setAlpha((float) 0.3);
            }
        });

        btnClinician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(signUpClinician);
                btnPatient.setAlpha((float) 0.3);
                btnClinician.setAlpha(1);
            }
        });

        btnPatient.callOnClick();
    }

    public void openFragment(Fragment fragment) {
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frmcontain, fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {

        counter+=1;
        if(counter>1){
            super.onBackPressed();
        }else{
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
    }
}