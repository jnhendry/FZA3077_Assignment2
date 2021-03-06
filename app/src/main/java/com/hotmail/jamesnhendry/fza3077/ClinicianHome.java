package com.hotmail.jamesnhendry.fza3077;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ClinicianHome extends AppCompatActivity {

    private RecyclerView recyclerPatients, recyclerPastVisit, recyclerFutureVisit;
    private patientAdapter patientAdapter;
    private VisitAdapter pastVisitsAdapter, futureVisitsAdapter;
    private TextView edtClinicianName;
    private String name;
    private FirebaseAuth mAuth;
    private ArrayList<Patient> patients = new ArrayList<>();
    FirebaseFirestore db;
    FirebaseUser user;

    private static final int PERMISSION_REQUEST_CODE = 200;

    private Button btnSearch,btnStatistics;
    final ArrayList<Visit> visitPastArrayList = new ArrayList<>();
    final ArrayList<Visit> visitFutureArrayList = new ArrayList<>();
    private MaterialToolbar topAppBar;
    int counter = 0;
    private SearchView searchView;
    private Spinner spnSearch;
    private String searchStuff, queryForSearch;
    private int thisIsTheBreaker;


    @Override
    public void onBackPressed() {
        counter+=1;
        if(counter>1){
            super.onBackPressed();
        }else{
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinitian_home);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        btnSearch = findViewById(R.id.btnsearchpatients);
        spnSearch = findViewById(R.id.spnsearchpatient);
        searchView = findViewById(R.id.svsearchpatient);
        btnStatistics = findViewById(R.id.btnStatistics);
        edtClinicianName = findViewById(R.id.user_name_banner);
        DocumentReference snap = db.collection("clinician").document(user.getUid());

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

        snap.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
            if(documentSnapshot.exists()) {
                edtClinicianName.setText(documentSnapshot.get("name").toString());
                name = documentSnapshot.get("name").toString();
            }
            }
        });

        populateArray();

        btnStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(patients.size() > 0){
                    generateDate();
                }
                else {
                    Toast.makeText(ClinicianHome.this, "Search Results are Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // The btnSearch button is used to handle patient search requests.
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            btnStatistics.setVisibility(View.VISIBLE);

            switch(spnSearch.getSelectedItem().toString()) {
                case "Name":
                    searchPatients(searchView.getQuery().toString(),"name");
                    return;
                case "Age":
                    searchPatients(searchView.getQuery().toString(),"age");
                    return;
                case "Location":
                    searchPatients(searchView.getQuery().toString(),"suburb");
                    return;
                case "My Patients":
                    searchPatients(searchView.getQuery().toString(),"patients");
                    return;
                default:

            }
            }
        });
        setRecycle();
    }


    //Generate statistical data for the search results produced by the clinician's patient search.
    private void generateDate() {
       db.collection("visit").whereEqualTo("visitCompleted",true).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
           @Override
           public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
           ArrayList<Stats> stats = new ArrayList<>();
           ArrayList<Stats> temp = new ArrayList<>();
           for(DocumentSnapshot snap : queryDocumentSnapshots) {
               Stats stat = new Stats();
               stat.setName(snap.get("patientName").toString());
               stat.setRrs((Double) snap.get("medicalRecord.reynoldsRiskScore"));
               stat.setBloodPressure((Double) snap.get("medicalRecord.bloodPressure"));
               stat.setSmokes((Boolean) snap.get("medicalRecord.smoker"));
               stat.setFamilyHistory((Boolean) snap.get("medicalRecord.familyHistory"));
               temp.add(stat);
           }

           for(int j = 0; j < temp.size(); j++) {
               for(int i = 0; i < patients.size(); i++) {
                   if(patients.get(i).getName().equals(temp.get(j).getName())){
                       Stats tempStat = new Stats();
                       tempStat.setName(patients.get(i).getName());
                       tempStat.setFamilyHistory(temp.get(j).isFamilyHistory());
                       tempStat.setSmokes(temp.get(j).isSmokes());
                       tempStat.setBloodPressure(temp.get(j).getBloodPressure());
                       tempStat.setRrs(temp.get(j).getRrs());
                       tempStat.setLocation(patients.get(i).getLocation());
                       tempStat.setAge(patients.get(i).getPhoneNumber());
                       tempStat.setGender(patients.get(i).getSex());
                       stats.add(tempStat);
                   }
               }
           }

           double rrs = 0,smokes = 0,famhist = 0,bloodpressure = 0,age = 0;
            System.out.println(stats.size()+"");
            for(Stats st:stats){
               age+=st.getAge();
               rrs+=st.getRrs();
               if(st.isSmokes()){
                   smokes+=1;
               }
               if(st.isFamilyHistory()){
                   famhist+=1;
               }
               bloodpressure+=st.getBloodPressure();
           }

           rrs /= stats.size();
           age /= stats.size();
           smokes /= stats.size();
           famhist /= stats.size();
           bloodpressure /= stats.size();
          generatePDF(rrs,smokes,famhist,bloodpressure,age);
           }
       });
    }

    //This method handles functionality concerning searching through the collection patients.
    public void searchPatients(String patient, String search){
        patients.clear();
        patientAdapter.notifyDataSetChanged();
        searchStuff = patient;
        queryForSearch = search;
        thisIsTheBreaker = 0;

        db.collection("patient").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            if(!queryDocumentSnapshots.isEmpty()){
                for(DocumentSnapshot snap:queryDocumentSnapshots){

                    Map<String,Object> map = snap.getData();
                    DateAge test = new DateAge((long)map.get("dateOfBirth"));
                    int age = test.getAge() ;
                    Patient patient1 = new Patient(map.get("name").toString(),age,map.get("gender").toString(),snap.getId(),user.getUid(),map.get("suburb").toString());

                    switch(queryForSearch){
                        case "name":
                            if(patient1.getName().toLowerCase().startsWith(searchStuff.toLowerCase())) {
                                patients.add(patient1);
                                System.out.println( patient1.getName() + searchStuff);
                                patientAdapter.notifyDataSetChanged();
                            }
                            break;
                        case "age":
                            if(patient1.getPhoneNumber()==(Integer.parseInt(searchStuff))) {
                                patients.add(patient1);
                                System.out.println( patient1.getName() + searchStuff);
                                patientAdapter.notifyDataSetChanged();
                            }
                            break;
                        case "suburb":
                            if(patient1.getLocation().toLowerCase().startsWith(searchStuff.toLowerCase())) {
                                patients.add(patient1);
                                System.out.println( patient1.getName() + searchStuff);
                                patientAdapter.notifyDataSetChanged();
                            }
                            break;
                        case "patients":
                            while(thisIsTheBreaker == 0) {
                                populateArray();
                                patientAdapter.notifyDataSetChanged();
                                thisIsTheBreaker +=1;
                            }
                            break;
                        default:

                    }
                }
            }
            }
        });
    }

    //Generate PDF document to display statistical data.
     private void generatePDF(double rrsa, double smokes, double famhist, double bloodpressure,double age) {

         String id = "search for clinician:"+ edtClinicianName.getText().toString()+" on: " + LocalTime.now();
         String time = LocalTime.now()+"";
         time = time.replace(":","");
         time = time.replace(".","");
         String filename = edtClinicianName.getText().toString().trim()+time+".pdf";

        int pageHeight = 1120;
        int pageWidth = 792;
        Bitmap bmp, scaledbmp;
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.gfgimage);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 280, 280, false);
        if (!checkPermission()) {
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
        canvas.drawText(id,400,140,title);


        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.black));
        title.setTextSize(21);

        title.setTextAlign(Paint.Align.LEFT);

        canvas.drawText("Statitics Generated from Query: "+ queryForSearch + " using search : "+ searchStuff,100,350,title);

        title.setTextSize(15);

        canvas.drawText("Statistics Details : ",100,380,title);
        title.setTextSize(15);
        canvas.drawText("Average Age of patients :        "+Math.round(age),120,420,title);
        canvas.drawText("Average percentage of smokers  :            "+Math.round(smokes*100)+"%",120,440,title);
        canvas.drawText("Average of family History in patients :    "+Math.round(famhist*100)+"%",120,480,title);

        canvas.drawText("Average blood pressure : " + Math.round(bloodpressure),100,520,title);

        canvas.drawText("Average Reynolds risk score :                                        "+Math.round(rrsa)+"%",120,550,title);

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


        File file = new File(parentDir , filename);

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(ClinicianHome.this, "PDF file generated succesfully.", Toast.LENGTH_SHORT).show();
        } catch(IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();
    }

    // Storing a PDF document on Users device requires having access to the users storage. The methods bellow handle this issue by
    // checking for this permission and requesting for permission in the event that it has not been supplied.
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
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    //This method retrieves all the patients that have their main clinician assigned as the currently logged in clinician.
    //This list of patients
    private void populateArray() {

        db.collection("patient").whereEqualTo("clinicianId", user.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
            if(error != null) {
                return;
            }
            for(DocumentSnapshot documentSnapshot : value) {
                DateAge dateAge = new DateAge((long) documentSnapshot.get("dateOfBirth"));
                int age = dateAge.getAge();
                Patient pat = new Patient(documentSnapshot.get("name").toString(), age , documentSnapshot.get("gender").toString(), documentSnapshot.getId(),name);

                patients.add(pat);
            }

            recyclerPatients = findViewById(R.id.recyclMedicalRecord);
            recyclerPatients.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            patientAdapter = new patientAdapter(ClinicianHome.this, patients);
            recyclerPatients.setAdapter(patientAdapter);

            patientAdapter.setonItemClicklistener(new patientAdapter.onItemClickListener() {
                @Override
                public void onItemClicked(int position) {
                    Intent intent = new Intent(ClinicianHome.this,PatientHome.class);
                    intent.putExtra("UserID",patients.get(position).getPatientID());
                    intent.putExtra("isClinitian",true);
                    intent.putExtra("clinitianname", edtClinicianName.getText().toString());
                    startActivity(intent);
                }
            });
            }
        });
    }

    //This method the retrieving of the logged in clinician's past and future visits.
    public void setRecycle() {
        db.collection("visit").whereEqualTo("clinicianId", user.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
            if(error != null) {
                return;
            }
            visitPastArrayList.clear();
            visitFutureArrayList.clear();
            for( final DocumentSnapshot documentSnapshot : value) {

                String date = documentSnapshot.get("date").toString();
                String time = documentSnapshot.get("scheduleStart").toString();
                String clinicianId = documentSnapshot.get("clinicianName").toString();
                String patientID = documentSnapshot.get("patientName").toString();
                boolean visitCompleted = (boolean) documentSnapshot.get("visitCompleted");

                Visit visit = new Visit(clinicianId,patientID , date, time,documentSnapshot.getId(),visitCompleted);

                if (visitCompleted){
                    visitPastArrayList.add(visit);
                }
                else{
                    visitFutureArrayList.add(visit);
                }
            }
            recyclerFutureVisit = findViewById(R.id.recycFutureVisitclinitian);
            recyclerFutureVisit.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            futureVisitsAdapter = new VisitAdapter(visitFutureArrayList, ClinicianHome.this);
            recyclerFutureVisit.setAdapter(futureVisitsAdapter);

            futureVisitsAdapter.setonItemClicklistener(new VisitAdapter.onItemClickListener() {
                @Override
                public void onItemClicked(int position) {
                    Intent futureVisit = new Intent(ClinicianHome.this, VisitScreen.class);
                    futureVisit.putExtra("visitid",visitFutureArrayList.get(position).getVisitId());
                    futureVisit.putExtra("value",position);//if position>0 then do nothing;
                    futureVisit.putExtra("isvisitcompleted",visitFutureArrayList.get(position).isIsCompleted());
                    futureVisit.putExtra("usertype","clinitian");
                    startActivity(futureVisit);
                }
            });

            recyclerPastVisit = findViewById(R.id.recycPastVisitclinitian);
            recyclerPastVisit.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            pastVisitsAdapter = new VisitAdapter(visitPastArrayList, ClinicianHome.this);
            recyclerPastVisit.setAdapter(pastVisitsAdapter);

            pastVisitsAdapter.setonItemClicklistener(new VisitAdapter.onItemClickListener() {
                @Override
                public void onItemClicked(int position) {
                    Intent pastVisit = new Intent(ClinicianHome.this, VisitScreen.class);
                    pastVisit.putExtra("visitid",visitPastArrayList.get(position).getVisitId());
                    pastVisit.putExtra("value",position);//if position>0 then do nothing;
                    pastVisit.putExtra("isvisitcompleted",visitPastArrayList.get(position).isIsCompleted());
                    pastVisit.putExtra("usertype","clinician");
                    startActivity(pastVisit);
                }
            });
            }
        });
    }
}