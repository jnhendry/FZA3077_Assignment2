package com.hotmail.jamesnhendry.fza3077;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
    public void onBindViewHolder(@NonNull patientAdapter.subholder holder, int position) {
        holder.txtName.setText(list.get(position).getName());
        holder.txtPhone.setText(list.get(position).getPhoneNumber());
        holder.txtSex.setText(list.get(position).getSex());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class subholder extends RecyclerView.ViewHolder {
        TextView txtName,txtPhone,txtSex;

        public subholder(@NonNull View itemView,final onItemClickListener listener) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtNameSurname);
            txtPhone = itemView.findViewById(R.id.txtAgeClPatients);
            txtSex = itemView.findViewById(R.id.txtSex);

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
