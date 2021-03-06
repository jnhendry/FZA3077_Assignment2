package com.hotmail.jamesnhendry.fza3077;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesRecommendationAdapter extends RecyclerView.Adapter<NotesRecommendationAdapter.SubHolder>{

    private ArrayList<Note> notes;
    private ArrayList<Recommendation> recommended;
    private Context cr;
    private onitemClickListener listener;

    public NotesRecommendationAdapter(Context cr, ArrayList<Note> notes) {
        this.notes = notes;
        this.cr = cr;
    }

    public void setOnItemClickListener(onitemClickListener listener){
        this.listener = listener;
    }

    public interface onitemClickListener{
        void onItemClicked(int position);
    }

    public NotesRecommendationAdapter(ArrayList<Recommendation> recommended, Context cr) {
        this.recommended = recommended;
        this.cr = cr;
    }

    @NonNull
    @Override
    public SubHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_popup, parent,false);
        return new SubHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull SubHolder holder, int position) {
            if(notes!=null){
                holder.btnAdd.setVisibility(View.GONE);
                holder.description.setText(notes.get(position).getDescription());
                holder.body.setText(notes.get(position).getBody());
                holder.body.setKeyListener(null);
                holder.description.setKeyListener(null);

            }else if(recommended!=null){
                holder.btnAdd.setVisibility(View.GONE);
                holder.description.setText(recommended.get(position).getDescription());
                holder.body.setText(recommended.get(position).getBody());
                holder.body.setKeyListener(null);
                holder.description.setKeyListener(null);

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

    public static class SubHolder extends RecyclerView.ViewHolder{
        EditText description,body;
        Button btnAdd;
        public SubHolder(@NonNull View itemView, final onitemClickListener listener) {
            super(itemView);
            description = itemView.findViewById(R.id.noteSubject);
            body = itemView.findViewById(R.id.noteBody);
            btnAdd = itemView.findViewById(R.id.newNote);


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
