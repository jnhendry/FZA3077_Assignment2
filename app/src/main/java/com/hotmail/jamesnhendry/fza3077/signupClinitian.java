package com.hotmail.jamesnhendry.fza3077;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link signupClinitian#newInstance} factory method to
 * create an instance of this fragment.
 */
public class signupClinitian extends Fragment {


    private EditText edtfirstName;
    private EditText edtsurname;
    private Spinner spncSpecialisation;
    private EditText edtemail;
    private EditText edtpassword;
    private EditText edtretypePassword;
    private Button btnsignUpButton;
    private TextView txtswitchToLogIn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public signupClinitian() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment signupClinitian.
     */
    // TODO: Rename and change types and number of parameters
    public static signupClinitian newInstance(String param1, String param2) {
        signupClinitian fragment = new signupClinitian();
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

        edtfirstName = view.findViewById(R.id.edtClinitianNameSU);
        edtsurname = view.findViewById(R.id.edtClinitianSurnamesu);
        spncSpecialisation = view.findViewById(R.id.spnClinitianSpeciality);
        edtemail = view.findViewById(R.id.edtEmailClinitianSU);
        edtpassword = view.findViewById(R.id.edtClinitianPasswordSU);
        edtretypePassword = view.findViewById(R.id.edtPasswordresu);
        btnsignUpButton = view.findViewById(R.id.btnSignUpClinitian);
        txtswitchToLogIn = view.findViewById((R.id.txtSignInClinitianSU));
        db = FirebaseFirestore.getInstance();

        String email,password;
        email = edtemail.getText().toString();
        password = edtpassword.getText().toString();
        signUp(email,password);

    }

    private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    String name,specialisation,email,userID;

                    name = edtfirstName.getText().toString()+ " " + edtsurname.getText().toString();
                    specialisation = spncSpecialisation.getSelectedItem().toString();
                    email = user.getEmail();
                    userID = user.getUid();

                    Map<String,Object> doc = new HashMap<>();
                    doc.put("name",name);
                    doc.put("specialisation",specialisation);
                    doc.put("email",email);

                    db.collection("Clinitian").document(userID).set(doc).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Yay doc", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Boo", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}