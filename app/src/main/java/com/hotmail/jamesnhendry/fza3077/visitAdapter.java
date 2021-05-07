package com.hotmail.jamesnhendry.fza3077;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class visitAdapter extends RecyclerView.Adapter<visitAdapter.subholder>{

    private ArrayList<Visit> list;
    private onItemClickListener mListener;
    Context cr;

    public visitAdapter(ArrayList<Visit> list, Context cr) {
        this.list = list;
        this.cr = cr;
    }

    public void setonItemClicklistener(onItemClickListener listener) {
        mListener = listener;
    }

    public interface onItemClickListener{
        void onItemClicked(int position);
    }

    @NonNull
    @Override
    public subholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.visitcard, parent,false);
        visitAdapter.subholder holder = new visitAdapter.subholder(view,mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull subholder holder, int position) {
                holder.txtVisitDate.setText(list.get(position).getDate().toString());
                holder.txtVisitClinitian.setText(list.get(position).getClinitianID());
                holder.txtVisitPatient.setText(list.get(position).getPatientID());

        String medicalrecord;

        if(list.get(position).getMedicalRecord()!=null){
            medicalrecord = "Completed";
        }else{
            medicalrecord = "Not Completed";
        }

        holder.txtVisitMedicalRecord.setText(medicalrecord);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class subholder extends RecyclerView.ViewHolder{

        TextView txtVisitDate,txtVisitTime,txtVisitClinitian,txtVisitPatient,txtVisitMedicalRecord;

        public subholder(@NonNull View itemView,final onItemClickListener listener) {
            super(itemView);

            txtVisitDate = itemView.findViewById(R.id.txtVisitDate);
            txtVisitTime = itemView.findViewById(R.id.txtVisitTime);
            txtVisitClinitian = itemView.findViewById(R.id.txtVisitClinitian);
            txtVisitPatient = itemView.findViewById(R.id.txtVisitPatient);
            txtVisitMedicalRecord = itemView.findViewById(R.id.txtVisitMedicalRecord);



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
