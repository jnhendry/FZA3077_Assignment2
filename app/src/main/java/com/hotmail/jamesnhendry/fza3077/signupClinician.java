package com.hotmail.jamesnhendry.fza3077;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signupClinician extends Fragment {

    private EditText edtFirstName;
    private EditText edtSurname;
    private Spinner spncSpecialisation;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtRetypePassword;
    private Button btnSignUpButton;
    private TextView txtSwitchToLogIn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public signupClinician() {
        // Required empty public constructor
    }

    public static signupClinician newInstance(String param1, String param2) {
        signupClinician fragment = new signupClinician();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup_clinitian, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtFirstName = view.findViewById(R.id.edtClinitianNameSU);
        edtSurname = view.findViewById(R.id.edtClinitianSurnamesu);
        spncSpecialisation = view.findViewById(R.id.spnClinitianSpeciality);
        edtEmail = view.findViewById(R.id.edtEmailClinitianSU);
        edtPassword = view.findViewById(R.id.edtClinitianPasswordSU);
        edtRetypePassword = view.findViewById(R.id.edtPasswordresu);
        btnSignUpButton = view.findViewById(R.id.btnSignUpClinitian);
        txtSwitchToLogIn = view.findViewById((R.id.txtSignInClinitianSU));
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        txtSwitchToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        btnSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //State Variables
            String firstName, surname, specialisation, email, password, retypePassword;

            firstName = edtFirstName.getText().toString();
            surname = edtSurname.getText().toString();
            specialisation = spncSpecialisation.getSelectedItem().toString();
            email = edtEmail.getText().toString();
            password = edtPassword.getText().toString();
            retypePassword = edtRetypePassword.getText().toString();

            if(firstName.isEmpty() || surname.isEmpty() || specialisation.equals("Specialisation") || email.isEmpty() || password.isEmpty() || retypePassword.isEmpty()){
                Toast.makeText(getActivity().getApplicationContext(),"Please fill in all fields",Toast.LENGTH_SHORT).show();
            }
            else{
                if(!password.equals(retypePassword)){
                    Toast.makeText(getActivity().getApplicationContext(),"Password & Re-type Password do not match",Toast.LENGTH_SHORT).show();
                }else{
                    signUpNewClinician(firstName, surname, specialisation, email,password);
                }
            }
            }
        });
    }

    // This method performs the actual process of signing up clinicians as users of this platform.
    // This also includes storing their user data in the firebase firestore database.
    private void signUpNewClinician(final String firstName, final String surname, final String specialisation, final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                FirebaseUser user = mAuth.getCurrentUser();
                String documentId = user.getUid();

                Map<String,Object> doc = new HashMap<>();
                doc.put("name", firstName + " " + surname);
                doc.put("specialisation",specialisation);
                doc.put("email",email);

                db.collection("clinician").document(documentId).set(doc).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity().getApplicationContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), ClinicianHome.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(),"Error Storing New User Info",Toast.LENGTH_SHORT).show();
                    }
                });

            }else{
                Toast.makeText(getActivity().getApplicationContext(),"Sign Up Error: Please Try Again",Toast.LENGTH_SHORT).show();
            }
            }
        });
    }
}