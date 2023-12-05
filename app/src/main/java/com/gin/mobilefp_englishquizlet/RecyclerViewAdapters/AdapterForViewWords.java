package com.gin.mobilefp_englishquizlet.RecyclerViewAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gin.mobilefp_englishquizlet.Models.Word;
import com.gin.mobilefp_englishquizlet.R;
import com.gin.mobilefp_englishquizlet.TextToSpeechHelper;

import java.util.ArrayList;

public class AdapterForViewWords extends RecyclerView.Adapter<AdapterForViewWords.MyViewHolder>{
    Context context;
    ArrayList<Word> words;
    public AdapterForViewWords(Context context, ArrayList<Word> words) {
        //constructor
        this.context = context;
        this.words = words;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.term_item, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Word currentWord = words.get(position);

        holder.txtviewTerm.setText(currentWord.getTerm());
        holder.txtviewDefinition.setText(currentWord.getDefinition());
        if(!currentWord.getDescription().equals("")) {
            holder.txtviewDescription.setText(currentWord.getDescription());
        }
        else {
            holder.txtviewDescription.setText("None");
        }

        holder.btnSound.setOnClickListener(v -> {
            TextToSpeechHelper.initialize(context, new TextToSpeechHelper.OnInitializationListener() {
                @Override
                public void onInitialized() {
                    // TextToSpeech initialized successfully
                    TextToSpeechHelper.speak(currentWord.getTerm());
                }
                @Override
                public void onInitializationFailed() {
                }
            });
        });
    }
    @Override
    public int getItemCount() {
        //count
        return words.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtviewTerm, txtviewDefinition, txtviewDescription;
        ImageButton btnSound;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtviewTerm = itemView.findViewById(R.id.txtviewTerm);
            txtviewDefinition = itemView.findViewById(R.id.txtviewDefinition);
            txtviewDescription = itemView.findViewById(R.id.txtviewDescription);
            btnSound = itemView.findViewById(R.id.btnSound);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
