package com.hotmail.jamesnhendry.fza3077;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
    private final ArrayList<String> clintianID = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private long dateSelected = 0;

    public signupPatient() {
        // Required empty public constructor
    }


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
        mAuth = FirebaseAuth.getInstance();

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
        populateCliniciansSpinner();

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select date of Birth").setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR).build();
                datePicker.show(getFragmentManager(),"Date");

                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        dateSelected = (long) selection;
                        String dateString = new SimpleDateFormat("dd-mm-yyyy", Locale.ENGLISH).format(selection);
                        edtDate.setText(dateString);
                    }
                });
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //State Variables
                String firstName, surname, gender, suburb, clinician, email, password, retypePassword;
                long dateOfBirth;

                firstName = edtName.getText().toString();
                surname = edtSurname.getText().toString();
                gender = spnGender.getSelectedItem().toString();
                suburb = spnSuburb.getSelectedItem().toString();
                clinician = spnClinitian.getSelectedItem().toString();
                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();
                retypePassword = edtPass2.getText().toString();
                dateOfBirth = dateSelected;

                if(firstName.isEmpty() || surname.isEmpty() || gender.equals("Gender") || suburb.equals("Suburb") || clinician.equals("Clinician") || email.isEmpty() || password.isEmpty() || retypePassword.isEmpty() || dateOfBirth == 0 ){
                    Toast.makeText(getActivity().getApplicationContext(),"Please fill in all fields",Toast.LENGTH_SHORT).show();

                } else{
                    if(!password.equals(retypePassword)){
                        Toast.makeText(getActivity().getApplicationContext(),"Password & Re-type Password do not match",Toast.LENGTH_SHORT).show();
                    }else {
                        //getClinicianID
                        String clinicianId =clintianID.get(spnClinitian.getSelectedItemPosition());
                        signUpNewUser(firstName, surname, gender, suburb, clinicianId, email, password, dateOfBirth);
                    }
                }
            }
        });

    }

    public void populateCliniciansSpinner(){

        db.collection("clinician").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<String> theClinicians =  new ArrayList<String>();
                theClinicians.add("Clinician");
                clintianID.add("Clinician");
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        HashMap <String, Object> map = (HashMap) document.getData();
                        clintianID.add(document.getId());
                        theClinicians.add(map.get("name").toString());
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getContext(), android.R.layout.simple_spinner_item, theClinicians);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnClinitian.setAdapter(adapter);
            }
        });
    }


    public void signUpNewUser(final String firstName, final String surname,final String gender,final String suburb, final String clinician, final String email,final String password,final long dateOfBirth){

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        String documentId = user.getUid();
                        Map<String,Object> document = new HashMap<>();
                        document.put("name",firstName + " " + surname);
                        document.put("gender",gender);
                        document.put("email",email);
                        document.put("suburb",suburb);
                        document.put("clinicianId",clinician);
                        document.put("dateOfBirth",dateOfBirth);

                        db.collection("patient").document(documentId)
                                .set(document)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                       System.out.println("Success");
                                        Toast.makeText(getActivity().getApplicationContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getContext(),PatientHome.class);
                                        startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error Storing New User Info", e);
                                        Toast.makeText(getActivity().getApplicationContext(),"Error Storing New User Info",Toast.LENGTH_SHORT).show();
                                    }
                                });

                    } else{
                        Log.w(TAG, "signinwithemail:failure",task.getException() );
                        Toast.makeText(getActivity().getApplicationContext(),"Sign Up Error: Please Try Again",Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}