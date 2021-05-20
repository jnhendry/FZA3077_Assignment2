package com.hotmail.jamesnhendry.fza3077;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class VisitScreen extends AppCompatActivity {

    private EditText bloodPressure, cReactive,apolprotA,apolprotB,lipoprotA,hemoA;
    private Spinner smoker,famhist;
    private TextView txtClinicianName, txtPatientName, txtPatientGender, txtPatientAge, txtPatientLocation, txtReynoldsRiskScore, bannerName;
    private RecyclerView recyclerNotes, recyclerRecommendation;
    private Button btnAddNote, btnAddRecommendation,btnSaveVisit;
    private NotesRecommendationAdapter adapter;
    private ArrayList<Note> noteArrayList = new ArrayList<>();
    private ArrayList<Recommendation> recommendationArrayList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Button btnUpdate,btnSimulate,btnGeneratePDF;
    private MaterialToolbar topAppBar;
    String userType;
    String visitID;
    private MedicalRecord medrec;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_visit);
        boolean completed;

        declareElements();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNotesAndRecommendations(noteArrayList,recommendationArrayList);
            }
        });

        DocumentReference patient = db.document("patient/"+mAuth.getCurrentUser().getUid());
        DocumentReference clinician = db.document("clinician/"+mAuth.getCurrentUser().getUid());

        patient.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    bannerName = findViewById(R.id.user_name_banner);
                    bannerName.setText(documentSnapshot.get("name").toString());
                }
            }
        });

        clinician.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    bannerName = findViewById(R.id.user_name_banner);
                    bannerName.setText(documentSnapshot.get("name").toString());
                }
            }
        });

        topAppBar = findViewById(R.id.topAppBar);

        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

                return true;
            }
        });

        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewNote();
            }
        });

        btnAddRecommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewRecommendation();
            }
        });
        btnGeneratePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generatePDF();
            }
        });
        btnSimulate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sBloodP,SCReac,sApolproA,sApolproB,sHemoglobin,sLipoProt;
                double rrs;
                boolean smokerEr,famhistoryRy;
                String smokes,famhistory;
                sBloodP = bloodPressure.getText().toString();
                SCReac = cReactive.getText().toString();
                sApolproA = (apolprotA.getText().toString());
                sApolproB=(apolprotB.getText().toString());
                sHemoglobin = (hemoA.getText().toString());
                sLipoProt = (lipoprotA.getText().toString());
                smokes = smoker.getSelectedItem().toString();
                famhistory = famhist.getSelectedItem().toString();
                if((sBloodP.equals(""))||(SCReac.equals(""))||(sApolproA.equals(""))||(sApolproB.equals(""))||(sHemoglobin.equals(""))||(sLipoProt.equals(""))||(smokes.equals("--Select one--"))||famhistory.equals("--Select one--")){
                    Toast.makeText(VisitScreen.this, "Fill in all fields", Toast.LENGTH_SHORT).show();
                }else{

                    if(smokes.equals("Yes")){
                        smokerEr = true;
                    }else{
                        smokerEr = false;
                    }
                    if(famhistory.equals("Yes")){
                        famhistoryRy = true;
                    }else {
                        famhistoryRy = false;
                    }
                    MedicalRecord medrec = new MedicalRecord(Double.parseDouble(sBloodP),Double.parseDouble(SCReac),Double.parseDouble(sApolproB),Double.parseDouble(sApolproA),Double.parseDouble(sLipoProt),Double.parseDouble(sHemoglobin),smokerEr,famhistoryRy);
                    rrs = medrec.calculateReynoldsRiskScore();
                    txtReynoldsRiskScore.setText(Math.round(rrs)+"%");
                }
            }});
        Intent intent = getIntent();

        visitID = intent.getStringExtra("visitid");//use this to gather from the DB
        completed = intent.getBooleanExtra("isvisitcompleted",false);
        userType = intent.getStringExtra("usertype");
        populateEmptyFields(visitID);

        if(userType !=null) {
            switch(userType) {
                case "patient":
                    btnSimulate.setVisibility(View.VISIBLE);
                    btnUpdate.setVisibility(View.GONE);
                    btnSaveVisit.setVisibility(View.GONE);
                    btnAddNote.setVisibility(View.GONE);
                    btnAddRecommendation.setVisibility(View.GONE);
                    btnGeneratePDF.setVisibility(View.VISIBLE);
                    updateViewForCompleted();
                    return;
                case "clinician":
                    if(completed) {
                       btnSaveVisit.setVisibility(View.GONE);
                       btnUpdate.setVisibility(View.VISIBLE);
                       btnSimulate.setVisibility(View.GONE);
                       updateViewForCompleted();
                       makeViewUneditable();
                       btnGeneratePDF.setVisibility(View.VISIBLE);
                       return;
                    }else {
                        btnSaveVisit.setVisibility(View.VISIBLE);
                        btnUpdate.setVisibility(View.GONE);
                        btnSimulate.setVisibility(View.GONE);
                    }
                    return;
                default:

            }
        }

        btnSaveVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               saveVisit();
            }
        });

        setupRecyclerViews();
    }

    private void generatePDF() {
        int pageHeight = 1120;
        int pageWidth = 792;
        Bitmap bmp, scaledbmp;
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.gfgimage);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 280, 280, false);
        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint title = new Paint();

        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(myPageInfo);
        Canvas canvas = myPage.getCanvas();
        canvas.drawBitmap(scaledbmp, 56, 40, paint);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, R.color.black));
        canvas.drawText("Document ID :  "+visitID,500,140,title);

        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.black));
        title.setTextSize(15);

        title.setTextAlign(Paint.Align.LEFT);

        canvas.drawText("Clinician : "+ txtClinicianName.getText().toString(),100,350,title);

        title.setTextSize(21);

        canvas.drawText("Patient Details : ",100,380,title);
        title.setTextSize(15);
        canvas.drawText("Name :        "+ txtPatientName.getText().toString(),120,420,title);
        canvas.drawText("Age :            "+ txtPatientAge.getText().toString(),120,440,title);
        canvas.drawText("Gender :      "+ txtPatientGender.getText().toString(),120,460,title);
        canvas.drawText("Location :    "+ txtPatientLocation.getText().toString(),120,480,title);

        title.setTextSize(21);


        canvas.drawText("Lab Measurements : ",100,520,title);

        title.setTextSize(15);

        canvas.drawText("Blood pressure :                                        "+ bloodPressure.getText().toString(),120,550,title);
        canvas.drawText("C Reactive protein :                                   "+ cReactive.getText().toString(),120,570,title);
        canvas.drawText("Apolipo protein A :                                    "+apolprotA.getText().toString(),120,590,title);
        canvas.drawText("Apolipo protein B :                                    "+apolprotB.getText().toString(),120,610,title);
        canvas.drawText("Lipoprotein A :                                           "+lipoprotA.getText().toString(),120,630,title);
        canvas.drawText("Hemoglobin A1 :                                       "+hemoA.getText().toString(),120,650,title);
        canvas.drawText("Is a smoker :                                              "+smoker.getSelectedItem().toString(),120,670,title);
        canvas.drawText("Family History of heart disease :              "+famhist.getSelectedItem().toString(),120,690,title);
        canvas.drawText("Reynolds risk score :                                  "+ txtReynoldsRiskScore.getText().toString(),120,710,title);

        title.setTextSize(21);

        canvas.drawText("Notes  :  ",100,760,title);
        int counter = 760;
        title.setTextSize(10);
        for(int i = 0;i<noteArrayList.size();i++){
            counter+=15;
            canvas.drawText("Description : "+(noteArrayList.get(i).getDescription()),120,counter,title);
            counter+=15;
            canvas.drawText("Body :            "+(noteArrayList.get(i).getBody()),120,counter,title);
            counter+=15;
        }
        title.setTextSize(21);
        canvas.drawText("Recommendations  :  ",450,760,title);
        int counter2 = 760;
        title.setTextSize(10);
        for(int i = 0;i<recommendationArrayList.size();i++){
            counter2+=15;
            canvas.drawText("Description : "+(recommendationArrayList.get(i).getDescription()),470,counter2,title);
            counter2+=15;
            canvas.drawText("Body :            "+(recommendationArrayList.get(i).getBody()),470,counter2,title);
            counter2+=15;
        }


        pdfDocument.finishPage(myPage);
        File[] files = getExternalFilesDirs(null);
        String dir= "";

        if(files == null) {
        } else {
            if(files.length > 0) {
                for(File file : files) {
                    dir = file.getPath();
                }
            }
        }
            File parentDir = new File(dir + File.separator + getString(R.string.app_name) + File.separator + "PDF's");
            if(!parentDir.exists()) {
                parentDir.mkdirs();
            }
            String filename =  txtPatientName.getText().toString().trim().replace(" ", "") + LocalDate.now().toString().replace(":", "").replace("-", "").replace(".", "").replace("/", "") + ".pdf";

            File file = new File(parentDir , filename);

            try {
                // after creating a file name we will
                // write our PDF file to that location.
                pdfDocument.writeTo(new FileOutputStream(file));

                // below line is to print toast message
                // on completion of PDF generation.
                Toast.makeText(VisitScreen.this, "PDF file generated Successfully.", Toast.LENGTH_SHORT).show();
            } catch(IOException e) {
                // below line is used
                // to handle error
                e.printStackTrace();
            }
            // after storing our pdf to that
            // location we are closing our PDF file.
            pdfDocument.close();

        }


    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    private void updateViewForCompleted() {
        System.out.println(visitID);
        db.collection("visit").document(visitID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

            if(documentSnapshot.exists()) {
                double bp, cr, apa, apb, lipa, hem,rrs;
                boolean sm, fh;
                Map<String,Object> map = documentSnapshot.getData();
                Map<String,Object> mr = (Map<String, Object>) map.get("medicalRecord");
                ArrayList<Map> notes = (ArrayList<Map>) map.get("notes");
                ArrayList<Map> recos =(ArrayList<Map>) map.get("recommendations");

                bp = (double) mr.get("bloodPressure");
                cr = (double) mr.get("creactive");
                apa = (double) mr.get("apolprotA");
                apb = (double) mr.get("apolprotB");
                lipa = (double) mr.get("lipoprotA");
                hem = (double) mr.get("hemoglobin");
                sm = (boolean) mr.get("smoker");
                fh = (boolean) mr.get("familyHistory");
                rrs = (double) mr.get("reynoldsRiskScore");
                MedicalRecord medicalRecord = new MedicalRecord(bp, cr, apb, apa, lipa, hem, sm, fh);//put shit here

                for(Map maps : notes) {
                    String body,description;
                    body = maps.get("body").toString();
                    description=maps.get("subject").toString();
                    Note note = new Note(body, description);
                    noteArrayList.add(note);
                }
                for(Map maps : recos) {
                    Recommendation recommendation = new Recommendation(maps.get("body").toString(), maps.get("subject").toString());
                    recommendationArrayList.add(recommendation);
                }

                setupRecyclerViews();
                bloodPressure.setText(bp+"");
                cReactive.setText( cr+"");
                apolprotA.setText( apa+"");
                apolprotB.setText(apb+"");
                lipoprotA.setText( lipa+"");
                hemoA.setText(hem+"");
                txtReynoldsRiskScore.setText(Math.round(rrs)+"%");

                if(medicalRecord.isSmoker()) {
                    smoker.setSelection(1);
                } else {
                    smoker.setSelection(2);
                }
                if(medicalRecord.isFamilyHistory()) {
                    famhist.setSelection(1);
                } else {
                    famhist.setSelection(2);

                }
            }
                }

        });

    }


    private void populateEmptyFields(String visitID) {
        db.collection("visit").document(visitID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
            txtClinicianName.setText(documentSnapshot.get("clinicianName").toString());
            txtPatientName.setText(documentSnapshot.get("patientName").toString());

            db.collection("patient").document(documentSnapshot.get("patientId").toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                txtPatientGender.setText(documentSnapshot.get("gender").toString());
                txtPatientLocation.setText(documentSnapshot.get("suburb").toString());
                DateAge age = new DateAge((long)documentSnapshot.get("dateOfBirth"));

                txtPatientAge.setText(age.getAge()+"");
                }
            });
            }
        });
    }

    private void declareElements() {
        bloodPressure = findViewById(R.id.bloodpressureMedRec);
        cReactive = findViewById(R.id.creactiveMedRec);
        apolprotA = findViewById(R.id.apolipoproteinAMedRec);
        apolprotB = findViewById(R.id.apolipoproteinBMedRec);
        lipoprotA = findViewById(R.id.lipoproteinAMedRec);
        hemoA = findViewById(R.id.HemoglobinA1MedRec);
        smoker = findViewById(R.id.spnSmokerMedRec);
        famhist = findViewById(R.id.spnFamilyHistory);
        txtClinicianName = findViewById(R.id.txtClintiannameMedRec);
        txtPatientName = findViewById(R.id.patientnameMedRec);
        txtPatientGender = findViewById(R.id.patientGenderMedRec);
        txtPatientAge = findViewById(R.id.txtPatientAgeMedical);
        txtPatientLocation = findViewById(R.id.patientLocationMedRec);
        txtReynoldsRiskScore = findViewById(R.id.reynoldsriskscoreMedRec);
        recyclerNotes = findViewById(R.id.recyclNotes);
        recyclerRecommendation = findViewById(R.id.recyclRecommendations);
        btnAddNote = findViewById(R.id.addNote);
        btnAddRecommendation = findViewById(R.id.addRecom);
        btnSaveVisit = findViewById(R.id.btnSaveVisit);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnSimulate = findViewById(R.id.btnSimulate);
        btnGeneratePDF = findViewById(R.id.btnGeneratePdf);
    }


    //Add a new note to the arraylist and update the recycler view to show the changes.
    public void createNewNote(){

        final EditText edtBody, edtDescription;
        Button saveNote;
        final Dialog popup = new Dialog(this);
        popup.setContentView(R.layout.notes_popup);

        edtBody = popup.findViewById(R.id.noteBody);
        edtDescription = popup.findViewById(R.id.noteSubject);
        saveNote = popup.findViewById(R.id.newNote);
        popup.show();

        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String noteBody, noteSubject;
            noteBody = edtBody.getText().toString();
            noteSubject = edtDescription.getText().toString();
            if (noteBody.equals("")||noteSubject.equals("")){
                Toast.makeText(VisitScreen.this, "Do not leave fields empty", Toast.LENGTH_SHORT).show();
            }else {
                Note note = new Note(noteBody,noteSubject);
                noteArrayList.add(note);
            }
            popup.hide();
            adapter.notifyDataSetChanged();
            }
        });
    }

    //Add a new recommendation to the arraylist and update the recycler view to show the changes.
    public void createNewRecommendation(){
        final Dialog dialog = new Dialog( VisitScreen.this);
        dialog.setContentView(R.layout.notes_popup);
        final EditText body,description;
        Button save;
        body = dialog.findViewById(R.id.noteBody);
        description = dialog.findViewById(R.id.noteSubject);
        save = dialog.findViewById(R.id.newNote);

        dialog.show();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noteBody,noteSubject;
                noteBody = body.getText().toString();
                noteSubject = description.getText().toString();
                if(noteBody.equals("")||noteSubject.equals("")){
                    Toast.makeText(VisitScreen.this, "Do not leave fields empty", Toast.LENGTH_SHORT).show();
                }else{
                Recommendation recommendation = new Recommendation(noteBody,noteSubject);
                recommendationArrayList.add(recommendation);
                    dialog.hide();
            }}
        });

        adapter.notifyDataSetChanged();
    }

    public void setupRecyclerViews(){
        recyclerNotes.setLayoutManager(new LinearLayoutManager(VisitScreen.this));
        adapter = new NotesRecommendationAdapter(VisitScreen.this,noteArrayList);
        recyclerNotes.setAdapter(adapter);

        recyclerRecommendation.setLayoutManager(new LinearLayoutManager(VisitScreen.this));
        adapter = new NotesRecommendationAdapter(recommendationArrayList, VisitScreen.this);
        recyclerRecommendation.setAdapter(adapter);
    }

    public void saveVisit(){
        String sbloodp,screac,sapolproA,sapolproB,shemoglo,slipoProt;
        double rrs;
        boolean smokerer,famhitoryry;
        String smokes,famhistory;
        sbloodp = bloodPressure.getText().toString();
        screac = cReactive.getText().toString();
        sapolproA = (apolprotA.getText().toString());
        sapolproB=(apolprotB.getText().toString());
        shemoglo = (hemoA.getText().toString());
        slipoProt = (lipoprotA.getText().toString());
        smokes = smoker.getSelectedItem().toString();
        famhistory = famhist.getSelectedItem().toString();
        if((sbloodp.equals(""))||(screac.equals(""))||(sapolproA.equals(""))||(sapolproB.equals(""))||(shemoglo.equals(""))||(slipoProt.equals(""))||(smokes.equals("--Select one--"))||famhistory.equals("--Select one--")){
            Toast.makeText(VisitScreen.this, "Fill in all fields", Toast.LENGTH_SHORT).show();
        }else{

            if(smokes.equals("Yes")){
                smokerer = true;
            }else{
                smokerer = false;
            }
            if(famhistory.equals("Yes")){
                famhitoryry = true;
            }else {
                famhitoryry = false;
            }
             medrec = new MedicalRecord(Double.parseDouble(sbloodp),Double.parseDouble(screac),Double.parseDouble(sapolproB),Double.parseDouble(sapolproA),Double.parseDouble(slipoProt),Double.parseDouble(shemoglo),smokerer,famhitoryry);
            rrs = medrec.calculateReynoldsRiskScore()+10;
            txtReynoldsRiskScore.setText(Math.round(rrs)+"%");

            updateVisitMedicalRecord(visitID, medrec);
            updateNotesAndRecommendations(noteArrayList,recommendationArrayList);
            btnGeneratePDF.setVisibility(View.VISIBLE);
        }
    }
    private void updateNotesAndRecommendations(ArrayList<Note> noteArrayList, ArrayList<Recommendation> recommendationArrayList) {
        ArrayList<Map> notesArrayToSave = new ArrayList<>();

        for (int i = 0; i < noteArrayList.size(); i++){
            Map<String,Object> singleNote= new HashMap<>();
            singleNote.put("subject", noteArrayList.get(i).getDescription());
            singleNote.put("body",  noteArrayList.get(i).getBody());
            notesArrayToSave.add(singleNote);
        }

        // Prepare Recommendations
        ArrayList<Map> RecommendationArrayToSave = new ArrayList<>();
        for (int i = 0; i < recommendationArrayList.size(); i++){
            Map<String,Object> singleRecommendation= new HashMap<>();
            singleRecommendation.put("subject", recommendationArrayList.get(i).getDescription());
            singleRecommendation.put("body",  recommendationArrayList.get(i).getBody());
            RecommendationArrayToSave.add(singleRecommendation);
        }

        //Updating The Visit
        DocumentReference currentVisit = db.collection("visit").document(visitID);
        currentVisit.update( "notes", notesArrayToSave, "recommendations", RecommendationArrayToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                btnSaveVisit.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.VISIBLE);
                makeViewUneditable();
                Toast.makeText(VisitScreen.this, "Visit Updated", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VisitScreen.this, "Failed To Update Visit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateVisitMedicalRecord(String visitId, MedicalRecord medrec) {
        Map<String,Object> medicalRecordMap = new HashMap<>();
        medicalRecordMap.put("bloodPressure",medrec.getBloodPressure());
        medicalRecordMap.put("creactive", medrec.getcReactive());
        medicalRecordMap.put("apolprotA", medrec.getApolprotA());
        medicalRecordMap.put("apolprotB", medrec.getApolprotB());
        medicalRecordMap.put("hemoglobin", medrec.getHemoglobin());
        medicalRecordMap.put("lipoprotA", medrec.getLipProteinA());
        medicalRecordMap.put("smoker", medrec.isSmoker());
        medicalRecordMap.put("familyHistory", medrec.isFamilyHistory());
        medicalRecordMap.put("reynoldsRiskScore", medrec.getReynoldsRiskScore());

        DocumentReference currentVisit = db.collection("visit").document(visitId);

        currentVisit.update("medicalRecord",medicalRecordMap, "visitCompleted", true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                btnSaveVisit.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.VISIBLE);
                makeViewUneditable();
                Toast.makeText(VisitScreen.this, "Visit Updated", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VisitScreen.this, "Failed To Update Visit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeViewUneditable() {
        bloodPressure.setKeyListener(null);
        cReactive.setKeyListener(null);
        apolprotA.setKeyListener(null);
        apolprotB.setKeyListener(null);
        hemoA.setKeyListener(null);
        lipoprotA.setKeyListener(null);
        smoker.setEnabled(false);
        famhist.setEnabled(false);
    }

}