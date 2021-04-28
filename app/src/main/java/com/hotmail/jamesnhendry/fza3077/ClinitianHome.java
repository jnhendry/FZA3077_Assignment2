package com.hotmail.jamesnhendry.fza3077;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

public class ClinitianHome extends AppCompatActivity {

    private Gson gson = new Gson();

    private TextView edtClinitianName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinitian_home);
        Intent intent = getIntent();
        String clinitian = intent.getStringExtra("clinitian") ;
        Clinitian cl = gson.fromJson(clinitian,Clinitian.class);

        edtClinitianName = findViewById(R.id.txtclinitianname);

        edtClinitianName.setText(cl.getName());



    }
}