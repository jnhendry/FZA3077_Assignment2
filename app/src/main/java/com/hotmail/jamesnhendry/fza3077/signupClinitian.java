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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link signupClinitian#newInstance} factory method to
 * create an instance of this fragment.
 */
public class signupClinitian extends Fragment {


    private EditText firstName;
    private EditText surname;
    private Spinner cSpecialisation;
    private EditText email;
    private EditText password;
    private EditText retypePassword;
    private Button signUpButton;
    private TextView switchToLogIn;

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

        firstName = view.findViewById(R.id.edtClinitianNameSU);
        surname = view.findViewById(R.id.edtClinitianSurnamesu);
        cSpecialisation = view.findViewById(R.id.spnClinitianSpeciality);
        email = view.findViewById(R.id.edtEmailClinitianSU);
        password = view.findViewById(R.id.edtClinitianPasswordSU);
        retypePassword = view.findViewById(R.id.edtPasswordresu);
        signUpButton = view.findViewById(R.id.btnSignUpClinitian);
        switchToLogIn = view.findViewById((R.id.txtSignInClinitianSU));
    }
}