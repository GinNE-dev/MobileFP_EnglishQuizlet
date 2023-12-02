package com.gin.mobilefp_englishquizlet.RecyclerViewAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gin.mobilefp_englishquizlet.Models.Word;
import com.gin.mobilefp_englishquizlet.R;

import java.util.ArrayList;

public class AdapterForWords extends RecyclerView.Adapter<AdapterForWords.MyViewHolder>{
    Context context;
    ArrayList<Word> words;
    int mode;
    //0: just view
    //1: create new topic activity
    public AdapterForWords(Context context, ArrayList<Word> words, int mode) {
        //constructor
        this.context = context;
        this.words = words;
        this.mode = mode;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.word_item, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtviewTerm.setText(words.get(position).getTerm());
        holder.txtviewDefinition.setText(words.get(position).getDefinition());
        if(!words.get(position).getDescription().equals("")) {
            holder.txtviewDescription.setText(words.get(position).getDescription());
        }
        else {
            holder.txtviewDescription.setText("None");
        }

        //mode 1 is for create new topic activity
        if(mode == 1) {
            holder.cardView.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Remove this word?")
                        .setMessage("Are you sure you want to remove this word?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            words.remove(position);
                            notifyItemRemoved(position);
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            });
        }
    }

    @Override
    public int getItemCount() {
        //count
        return words.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtviewTerm, txtviewDefinition, txtviewDescription;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtviewTerm = itemView.findViewById(R.id.txtviewTerm);
            txtviewDefinition = itemView.findViewById(R.id.txtviewDefinition);
            txtviewDescription = itemView.findViewById(R.id.txtviewDescription);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
