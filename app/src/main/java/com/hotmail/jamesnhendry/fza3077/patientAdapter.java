package com.hotmail.jamesnhendry.fza3077;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class patientAdapter extends RecyclerView.Adapter<patientAdapter.subholder> {

    private ArrayList<Patient> list;
    private onItemClickListener mListener;
    Context cr;

    public patientAdapter(Context cr, ArrayList<Patient> pat){
        this.cr = cr;
        list = pat;
    }
    public void setonItemClicklistener(onItemClickListener listener) {
        mListener = listener;
    }

    public interface onItemClickListener{
        void onItemClicked(int position);
    }
    @NonNull
    @Override
    public patientAdapter.subholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patientcard, parent,false);
        patientAdapter.subholder holder = new patientAdapter.subholder(view,mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull patientAdapter.subholder holder, final int position) {
        holder.txtName.setText(list.get(position).getName());
        holder.txtPhone.setText(list.get(position).getPhoneNumber());
        holder.txtSex.setText(list.get(position).getSex());

        holder.btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 final Dialog dialog = new Dialog(cr);
                dialog.setContentView(R.layout.popupvisit);
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                String clinitianID = user.getUid();
                String patientID = list.get(position).getPatientID();
                final CalendarView calendarView = dialog.findViewById(R.id.clvDate);
                final EditText edtTime = dialog.findViewById(R.id.edtTime);
                Button btnConfirm = dialog.findViewById(R.id.btnConfirm);
                TextView txtAppointment = dialog.findViewById(R.id.txtAppointment);



                dialog.show();


                txtAppointment.setText("Create a new visit with :" + list.get(position).getName());
                final String[] dateString = new String[1];

                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(CalendarView view, int year, int month,
                                                    int dayOfMonth) {
                        String  curDate = String.valueOf(dayOfMonth);
                        String  Year = String.valueOf(year);
                        String  Month = String.valueOf(month);
                        dateString[0] = dayOfMonth+"-"+month+"-"+year;
                    }
                });

                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        String time = edtTime.getText().toString();
                        System.out.println(dateString[0] +" " + time);
                    }
                });

                String time;
                long date;


            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class subholder extends RecyclerView.ViewHolder {
        TextView txtName,txtPhone,txtSex;
        Button btnSchedule;

        public subholder(@NonNull View itemView,final onItemClickListener listener) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtNameSurname);
            txtPhone = itemView.findViewById(R.id.txtAgeClPatients);
            txtSex = itemView.findViewById(R.id.txtSex);
            btnSchedule = itemView.findViewById(R.id.btnScheduleVisit);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAbsoluteAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            listener.onItemClicked(position);
                        }

                    }
                }
            });
        }
    }


}
