package com.gin.mobilefp_englishquizlet.RecyclerViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gin.mobilefp_englishquizlet.Models.Word;
import com.gin.mobilefp_englishquizlet.R;

import java.util.ArrayList;

public class AdapterForAddWords extends RecyclerView.Adapter<AdapterForAddWords.MyViewHolder>{
    Context context;
    ArrayList<Word> words;
    public AdapterForAddWords(Context context, ArrayList<Word> words) {
        //constructor
        this.context = context;
        this.words = words;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.word_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.edtxtTerm.setText(words.get(position).getTerm());
        holder.edtxtDefinition.setText(words.get(position).getDefinition());
        holder.edtxtDescription.setText(words.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        //count
        return words.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        EditText edtxtTerm, edtxtDefinition, edtxtDescription;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            edtxtTerm = itemView.findViewById(R.id.edtxtTerm);
            edtxtDefinition = itemView.findViewById(R.id.edtxtDefinition);
            edtxtDescription = itemView.findViewById(R.id.edtxtDescription);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
