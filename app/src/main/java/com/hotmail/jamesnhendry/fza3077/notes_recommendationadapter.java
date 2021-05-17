package com.hotmail.jamesnhendry.fza3077;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class notes_recommendationadapter extends RecyclerView.Adapter<notes_recommendationadapter.subholder>{

    private ArrayList<Note> notes;
    private ArrayList<Recommendation> recommended;
    private Context cr;

    public notes_recommendationadapter(Context cr,ArrayList<Note> notes) {
        this.notes = notes;
        this.cr = cr;
    }

    public notes_recommendationadapter(ArrayList<Recommendation> recommended, Context cr) {
        this.recommended = recommended;
        this.cr = cr;
    }

    @NonNull
    @Override
    public subholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_popup, parent,false);
        return new subholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull subholder holder, int position) {
            if(notes!=null){
                holder.btnAdd.setVisibility(View.INVISIBLE);
                holder.description.setText(notes.get(position).getDescription());
                holder.body.setText(notes.get(position).getBody());
            }else if(recommended!=null){
                holder.btnAdd.setVisibility(View.INVISIBLE);
                holder.description.setText(recommended.get(position).getDescription());
                holder.body.setText(recommended.get(position).getBody());
            }
    }

    @Override
    public int getItemCount() {
        if(notes!=null){
            return notes.size();
        }else if(recommended!=null){
            return recommended.size();
        }else{
            return 0;
        }
    }

    public static class subholder extends RecyclerView.ViewHolder{
        EditText description,body;
        Button btnAdd;
        public subholder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.noteSubject);
            body = itemView.findViewById(R.id.noteBody);
            btnAdd = itemView.findViewById(R.id.newNote);
        }
    }
}
