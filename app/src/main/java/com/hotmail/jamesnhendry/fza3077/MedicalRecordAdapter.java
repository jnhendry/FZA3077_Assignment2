package com.hotmail.jamesnhendry.fza3077;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MedicalRecordAdapter extends RecyclerView.Adapter<MedicalRecordAdapter.subholder>{


    private ArrayList<MedicalRecord> list;
    private patientAdapter.onItemClickListener mListener;
    Context cr;

    public MedicalRecordAdapter(Context cr, ArrayList<MedicalRecord> pat){
        this.cr = cr;
        list = pat;
    }


    public void setonItemClicklistener(patientAdapter.onItemClickListener listener) {
        mListener = listener;
    }

    public interface onItemClickListener{
        void onItemClicked(int position);
    }


    @NonNull
    @Override
    public subholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicalrecordcard, parent,false);
        MedicalRecordAdapter.subholder holder = new MedicalRecordAdapter.subholder(view,mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull subholder holder, int position) {
        String bloodp = list.get(position).getBloodpressure()+" mg";
        String apolB = list.get(position).getApolprotB()+" mg";
        String apolA = list.get(position).getApolprotA()+" mg";
        String lipA = list.get(position).getLipProteinA()+" mg";
        String cRact = list.get(position).getcReactive()+" mg";
        String rrs = list.get(position).calculateReynoldsRiskScore()+" %";


        String smoker,famist;
        if(list.get(position).isSmoker()){
            smoker = "Yes";
        }else{
            smoker = "No";
        }
        if(list.get(position).isFamhist()){
            famist = "Yes";
        }else{
            famist = "No";
        }
                holder.txtClinitian.setText(list.get(position).getClinitianName());
                holder.txtDate.setText(list.get(position).getDateUpdated());
                holder.txtPatientName.setText(list.get(position).getPatientName());
                holder.txtPatientAge.setText(list.get(position).getAge());
                holder.txtBloodp.setText(bloodp);
                holder.txtCreact.setText(cRact);
                holder.txtapolprotB.setText(apolB);
                holder.txtapolprotA.setText(apolA);
                holder.txtlipA.setText(lipA);
                holder.txtSmoker.setText(smoker);
                holder.txtFam.setText(famist);
                holder.txtRRS.setText(rrs);
                holder.txtNotes.setText(list.get(position).getClinitiannotes());



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class subholder extends RecyclerView.ViewHolder {
            TextView txtClinitian,txtDate,txtPatientName,txtPatientAge,txtBloodp,txtCreact,txtapolprotB,txtapolprotA,txtlipA,txtSmoker,txtFam,txtRRS;
            EditText txtNotes;

        public subholder(@NonNull View itemView,final patientAdapter.onItemClickListener listener) {
            super(itemView);

            txtClinitian = itemView.findViewById(R.id.txtClinitianMR);
            txtDate = itemView.findViewById(R.id.txtDateMR);
            txtPatientName = itemView.findViewById(R.id.txtPatientMR);
            txtPatientAge = itemView.findViewById(R.id.txtPatientAgeMR);
            txtBloodp = itemView.findViewById(R.id.txtBloodPressureMR);
            txtCreact = itemView.findViewById(R.id.txtCreactiveMR);
            txtapolprotB = itemView.findViewById(R.id.txtApolBMR);
            txtapolprotA = itemView.findViewById(R.id.txtApolAMR);
            txtlipA = itemView.findViewById(R.id.txtLipoAMR);
            txtSmoker = itemView.findViewById(R.id.txtSmokerMR);
            txtFam = itemView.findViewById(R.id.txtFamilyHistoryMR);
            txtRRS = itemView.findViewById(R.id.txtRRSMR);
            txtNotes = itemView.findViewById(R.id.txtClinitianNotesMR);


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
