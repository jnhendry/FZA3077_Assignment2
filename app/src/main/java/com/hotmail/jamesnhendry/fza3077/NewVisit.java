package com.hotmail.jamesnhendry.fza3077;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class NewVisit extends AppCompatActivity {

    private EditText bloodpressure,creactive,apolprotA,apolprotB,lipoprotA,hemoA;
    private Spinner smoker,famhist;
    private TextView txtclinititanname,txtpatientname,txtpatientGender,txtpatientage,txtpatientlocation,txtreynoldsriskscore;
    private RecyclerView recyclnotes,recyclRecommendation;
    private Button btnaddNote,btnaddRecommendation,btnSaveVisit;
    private notes_recommendationadapter adapter;
    private ArrayList<Note> noteArrayList = new ArrayList<>();
    private ArrayList<Recommendation> recommendationArrayList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button btnUpdate,btnSimulate,btnGeneratePDF;
    String usertype,futureorpast;
    String visitID;
    private MedicalRecord medrec;
    private static final int PERMISSION_REQUEST_CODE = 200;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_visit);
        boolean completed;
        int isMostRecent;

        declareelements();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatenotesandrecommendations(noteArrayList,recommendationArrayList);
            }
        });

        btnaddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewNote();
            }
        });

        btnaddRecommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createnewRecommendation();
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
                String sbloodp,screac,sapolproA,sapolproB,shemoglo,slipoProt;
                double rrs;
                boolean smokerer,famhitoryry;
                String smokes,famhistory;
                sbloodp =bloodpressure.getText().toString();
                screac = creactive.getText().toString();
                sapolproA = (apolprotA.getText().toString());
                sapolproB=(apolprotB.getText().toString());
                shemoglo = (hemoA.getText().toString());
                slipoProt = (lipoprotA.getText().toString());
                smokes = smoker.getSelectedItem().toString();
                famhistory = famhist.getSelectedItem().toString();
                if((sbloodp.equals(""))||(screac.equals(""))||(sapolproA.equals(""))||(sapolproB.equals(""))||(shemoglo.equals(""))||(slipoProt.equals(""))||(smokes.equals("--Select one--"))||famhistory.equals("--Select one--")){
                    Toast.makeText(NewVisit.this, "Fill in all fields", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(NewVisit.this, "works", Toast.LENGTH_SHORT).show();

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
                    MedicalRecord medrec = new MedicalRecord(Double.parseDouble(sbloodp),Double.parseDouble(screac),Double.parseDouble(sapolproB),Double.parseDouble(sapolproA),Double.parseDouble(slipoProt),Double.parseDouble(shemoglo),smokerer,famhitoryry);
                    rrs = medrec.calculateReynoldsRiskScore();
                    txtreynoldsriskscore.setText(Math.round(rrs)+"%");
                }
            }});
        Intent intent = getIntent();

        visitID = intent.getStringExtra("visitid");//use this to gather from the DB
        isMostRecent = intent.getIntExtra("value",1);
        completed = intent.getBooleanExtra("isvisitcompleted",false);
        usertype = intent.getStringExtra("usertype");
        Toast.makeText(this, usertype, Toast.LENGTH_SHORT).show();
        populateemptyfields(visitID);

        //isEditable(isMostRecent);

        //TODO make medical record editable for lastest visit for patients
        //TODO make medical record, notes and recommendations editable of the latest visit for clinitians

        if(usertype!=null) {
            switch(usertype) {
                case "patient":
                    btnSimulate.setVisibility(View.VISIBLE);
                    btnUpdate.setVisibility(View.GONE);
                    btnSaveVisit.setVisibility(View.GONE);
                    btnaddNote.setVisibility(View.GONE);
                    btnaddRecommendation.setVisibility(View.GONE);
                    btnGeneratePDF.setVisibility(View.VISIBLE);
                    updateviewforcompleted();
                    return;
                case "clinician":
                    if(completed) {
                       btnSaveVisit.setVisibility(View.GONE);
                       btnUpdate.setVisibility(View.VISIBLE);
                       btnSimulate.setVisibility(View.GONE);
                       updateviewforcompleted();
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
                    //throw out an error
            }
        }




            //get the data from the database and populate notes and medical record as well as write out recommendations.




        btnSaveVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:import the pdf viewer and handle the data to the database. PDFs will be local.
                //TODO: handle notifications for clinitians and patients.
               saveVisit();
            }
        });




        setuprecyclers();
    }

    private void generatePDF() {
        int pageHeight = 1120;
        int pagewidth = 792;
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

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
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

        canvas.drawText("Clinitian : "+txtclinititanname.getText().toString(),100,350,title);

        title.setTextSize(21);

        canvas.drawText("Patient Details : ",100,380,title);
        title.setTextSize(15);
        canvas.drawText("Name :        "+txtpatientname.getText().toString(),120,420,title);
        canvas.drawText("Age :            "+txtpatientage.getText().toString(),120,440,title);
        canvas.drawText("Gender :      "+txtpatientGender.getText().toString(),120,460,title);
        canvas.drawText("Location :    "+txtpatientlocation.getText().toString(),120,480,title);

        title.setTextSize(21);


        canvas.drawText("Lab Measurements : ",100,520,title);

        title.setTextSize(15);

        canvas.drawText("Blood pressure :                                        "+bloodpressure.getText().toString(),120,550,title);
        canvas.drawText("C Reactive protein :                                   "+creactive.getText().toString(),120,570,title);
        canvas.drawText("Apolipo protein A :                                    "+apolprotA.getText().toString(),120,590,title);
        canvas.drawText("Apolipo protein B :                                    "+apolprotB.getText().toString(),120,610,title);
        canvas.drawText("Lipoprotein A :                                           "+lipoprotA.getText().toString(),120,630,title);
        canvas.drawText("Hemoglobin A1 :                                       "+hemoA.getText().toString(),120,650,title);
        canvas.drawText("Is a smoker :                                              "+smoker.getSelectedItem().toString(),120,670,title);
        canvas.drawText("Family History of heart disease :              "+famhist.getSelectedItem().toString(),120,690,title);
        canvas.drawText("Reynolds risk score :                                  "+txtreynoldsriskscore.getText().toString(),120,710,title);

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



    //blood pressure
        //c reactive Protein
        //apolipo protein A
        //apolipo Protein B
        //lipoprotein A
        //Hemoglobin A1
        //smoker
        //family history






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
            String filename =  visitID + ".pdf";


            File file = new File(parentDir , filename);

            try {
                // after creating a file name we will
                // write our PDF file to that location.
                pdfDocument.writeTo(new FileOutputStream(file));

                // below line is to print toast message
                // on completion of PDF generation.
                Toast.makeText(NewVisit.this, "PDF file generated succesfully.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }


    private void updateviewforcompleted() {
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

                    setuprecyclers();
                    bloodpressure.setText(bp+"");
                    creactive.setText( cr+"");
                    apolprotA.setText( apa+"");
                    apolprotB.setText(apb+"");
                    lipoprotA.setText( lipa+"");
                    hemoA.setText(hem+"");
                    txtreynoldsriskscore.setText(Math.round(rrs)+"%");

                    if(medicalRecord.isSmoker()) {
                        smoker.setSelection(1);
                    } else {
                        smoker.setSelection(2);
                    }
                    if(medicalRecord.isFamhist()) {
                        famhist.setSelection(1);
                    } else {
                        famhist.setSelection(2);

                    }


                }
                }

        });

    }



    private void populateemptyfields(String visitID) {
        db.collection("visit").document(visitID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                txtclinititanname.setText(documentSnapshot.get("clinicianName").toString());
                txtpatientname.setText(documentSnapshot.get("patientName").toString());

                db.collection("patient").document(documentSnapshot.get("patientId").toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        txtpatientGender.setText(documentSnapshot.get("gender").toString());
                        txtpatientlocation.setText(documentSnapshot.get("suburb").toString());
                        DateAge age = new DateAge((long)documentSnapshot.get("dateOfBirth"));

                        txtpatientage.setText(age.getAge()+"");

                    }
                });
            }
        });
    }



    private void declareelements() {
        //please do this
        bloodpressure = findViewById(R.id.bloodpressureMedRec);
        creactive = findViewById(R.id.creactiveMedRec);
        apolprotA = findViewById(R.id.apolipoproteinAMedRec);
        apolprotB = findViewById(R.id.apolipoproteinBMedRec);
        lipoprotA = findViewById(R.id.lipoproteinAMedRec);
        hemoA = findViewById(R.id.HemoglobinA1MedRec);
        smoker = findViewById(R.id.spnSmokerMedRec);
        famhist = findViewById(R.id.spnFamilyHistory);
        txtclinititanname = findViewById(R.id.txtClintiannameMedRec);
        txtpatientname = findViewById(R.id.patientnameMedRec);
        txtpatientGender = findViewById(R.id.patientGenderMedRec);
        txtpatientage = findViewById(R.id.txtPatientAgeMedical);
        txtpatientlocation = findViewById(R.id.patientLocationMedRec);
        txtreynoldsriskscore = findViewById(R.id.reynoldsriskscoreMedRec);
        recyclnotes = findViewById(R.id.recyclNotes);
        recyclRecommendation = findViewById(R.id.recyclRecommendations);
        btnaddNote = findViewById(R.id.addNote);
        btnaddRecommendation = findViewById(R.id.addRecom);
        btnSaveVisit = findViewById(R.id.btnSaveVisit);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnSimulate = findViewById(R.id.btnSimulate);
        btnGeneratePDF = findViewById(R.id.btnGeneratePdf);
    }



    public void createNewNote(){
        //add a new note to the arraylist and update the recycler view. notifydatsetchanged
        final EditText nBody, nDescription;
        Button saveNote;
        final Dialog popup = new Dialog(this);
        popup.setContentView(R.layout.notes_popup);

        nBody = popup.findViewById(R.id.noteBody);
        nDescription = popup.findViewById(R.id.noteSubject);
        saveNote = popup.findViewById(R.id.newNote);
        popup.show();

        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nbody, nsubject;
                nbody = nBody.getText().toString();
                nsubject = nDescription.getText().toString();
                if (nbody.equals("")||nsubject.equals("")){
                    Toast.makeText(NewVisit.this, "Do not leave fields empty", Toast.LENGTH_SHORT).show();
                }else {
                    Note note = new Note(nbody,nsubject);
                    noteArrayList.add(note);
                }

                popup.hide();
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void createnewRecommendation(){
        //add a new recommendation to the arraylist and update the recycler view. notifydatsetchanged
        final Dialog dialog = new Dialog( NewVisit.this);
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
                String notebody,notesubject;
                notebody = body.getText().toString();
                notesubject = description.getText().toString();
                if(notebody.equals("")||notesubject.equals("")){
                    Toast.makeText(NewVisit.this, "Do not leave fields empty", Toast.LENGTH_SHORT).show();
                }else{
                Recommendation recommendation = new Recommendation(notebody,notesubject);
                recommendationArrayList.add(recommendation);
                    dialog.hide();
            }}
        });

        adapter.notifyDataSetChanged();
    }

    public void setuprecyclers(){
        recyclnotes.setLayoutManager(new LinearLayoutManager(NewVisit.this));
        adapter = new notes_recommendationadapter(NewVisit.this,noteArrayList);
        recyclnotes.setAdapter(adapter);

        recyclRecommendation.setLayoutManager(new LinearLayoutManager(NewVisit.this));
        adapter = new notes_recommendationadapter(recommendationArrayList,NewVisit.this);
        recyclRecommendation.setAdapter(adapter);
    }

    public void saveVisit(){
        String sbloodp,screac,sapolproA,sapolproB,shemoglo,slipoProt;
        double rrs;
        boolean smokerer,famhitoryry;
        String smokes,famhistory;
        sbloodp =bloodpressure.getText().toString();
        screac = creactive.getText().toString();
        sapolproA = (apolprotA.getText().toString());
        sapolproB=(apolprotB.getText().toString());
        shemoglo = (hemoA.getText().toString());
        slipoProt = (lipoprotA.getText().toString());
        smokes = smoker.getSelectedItem().toString();
        famhistory = famhist.getSelectedItem().toString();
        if((sbloodp.equals(""))||(screac.equals(""))||(sapolproA.equals(""))||(sapolproB.equals(""))||(shemoglo.equals(""))||(slipoProt.equals(""))||(smokes.equals("--Select one--"))||famhistory.equals("--Select one--")){
            Toast.makeText(NewVisit.this, "Fill in all fields", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(NewVisit.this, "works", Toast.LENGTH_SHORT).show();

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
            txtreynoldsriskscore.setText(Math.round(rrs)+"%");

            updateVisitMedicalRecord(visitID, medrec);
            updatenotesandrecommendations(noteArrayList,recommendationArrayList);
            btnGeneratePDF.setVisibility(View.VISIBLE);
            // newvisit.put("")
        }
    }
    private void updatenotesandrecommendations(ArrayList<Note> noteArrayList, ArrayList<Recommendation> recommendationArrayList) {
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
                Toast.makeText(NewVisit.this, "Visit Updated", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewVisit.this, "Failed To Update Visit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateVisitMedicalRecord(String visitId, MedicalRecord medrec) {


        Map<String,Object> medicalRecordMap = new HashMap<>();
        medicalRecordMap.put("bloodPressure",medrec.getBloodpressure());
        medicalRecordMap.put("creactive", medrec.getcReactive());
        medicalRecordMap.put("apolprotA", medrec.getApolprotA());
        medicalRecordMap.put("apolprotB", medrec.getApolprotB());
        medicalRecordMap.put("hemoglobin", medrec.getHemoglobin());
        medicalRecordMap.put("lipoprotA", medrec.getLipProteinA());
        medicalRecordMap.put("smoker", medrec.isSmoker());
        medicalRecordMap.put("familyHistory", medrec.isFamhist());
        medicalRecordMap.put("reynoldsRiskScore", medrec.getReynoldsRiskScore());




        DocumentReference currentVisit = db.collection("visit").document(visitId);

        currentVisit.update("medicalRecord",medicalRecordMap, "visitCompleted", true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                btnSaveVisit.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.VISIBLE);
                makeViewUneditable();
                Toast.makeText(NewVisit.this, "Visit Updated", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewVisit.this, "Failed To Update Visit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeViewUneditable() {
        bloodpressure.setKeyListener(null);
        creactive.setKeyListener(null);
        apolprotA.setKeyListener(null);
        apolprotB.setKeyListener(null);
        hemoA.setKeyListener(null);
        lipoprotA.setKeyListener(null);
        smoker.setEnabled(false);
        famhist.setEnabled(false);
    }

    public void isEditable(int val){//set intent to grab a boolean of true if first element of recycler view is selected in PatientHome.class else, set nothing.
        if(val==0&&usertype.equals("patient")){
                btnSaveVisit.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.GONE);
                btnSimulate.setVisibility(View.VISIBLE);
        }else{
               makeViewUneditable();
        }
    }
}