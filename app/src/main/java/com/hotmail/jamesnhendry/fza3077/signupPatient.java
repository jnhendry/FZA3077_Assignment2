package com.hotmail.jamesnhendry.fza3077;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link signupPatient#newInstance} factory method to
 * create an instance of this fragment.
 */
public class signupPatient extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "SignUp";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText edtName,edtSurname,edtEmail,edtPassword,edtPass2;
    private Button edtDate;
    private TextView txtLogin;
    private Spinner spnGender,spnSuburb,spnClinitian;
    private Button btnSignUp;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private long dateselected;
    public signupPatient() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment signupPatient.
     */
    // TODO: Rename and change types and number of parameters
    public static signupPatient newInstance(String param1, String param2) {
        signupPatient fragment = new signupPatient();
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
        return inflater.inflate(R.layout.fragment_signup_patient, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();

        edtName = view.findViewById(R.id.edtPatientNameSU);
        edtSurname = view.findViewById(R.id.edtPatientSurnameSU);
        edtDate = view.findViewById(R.id.edtDOB);
        edtEmail = view.findViewById(R.id.edtEmailPatientSU);
        edtPassword = view.findViewById(R.id.edtPasswordSU);
        edtPass2 = view.findViewById(R.id.edtPasswordReSU);
        btnSignUp = view.findViewById(R.id.btnSignUp);
        txtLogin = view.findViewById(R.id.txtSignIn);
        spnGender = view.findViewById(R.id.spnGender);
        spnSuburb = view.findViewById(R.id.spnSuburb);
        spnClinitian = view.findViewById(R.id.spnClinitian);
        mAuth = FirebaseAuth.getInstance();


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtPassword.getText().toString().equals(edtPass2.getText().toString())){
                    String email,password;
                    email = edtEmail.getText().toString();
                    password = edtPassword.getText().toString();
                    SignUpemail(email,password);
                }
            }
        });



        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select date of Birth").setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR).build();
                datePicker.show(getFragmentManager(),"Date");

                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        dateselected = (long) selection;
                        String dateString = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(selection);
                        edtDate.setText(dateString);



                    }
                });
            }
        });








    }

    public void SignUpemail(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String docname = user.getUid();
                            String name = edtName.getText().toString() + " " + edtSurname.getText().toString();
                            String gender = spnGender.getSelectedItem().toString();
                            String suburb = spnSuburb.getSelectedItem().toString();
                            String clinitian = spnClinitian.getSelectedItem().toString();


                            Intent intent = new Intent(getContext(),PatientHome.class);
                            startActivity(intent);
                        } else{
                            Log.w(TAG, "signinwithemail:failure",task.getException() );
                            Toast.makeText(getActivity().getApplicationContext(),"authentication failed, check email and/or password or create a new Account",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}