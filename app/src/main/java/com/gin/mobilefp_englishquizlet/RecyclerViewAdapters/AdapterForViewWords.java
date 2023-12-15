package com.gin.mobilefp_englishquizlet.RecyclerViewAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gin.mobilefp_englishquizlet.Models.Word;
import com.gin.mobilefp_englishquizlet.R;
import com.gin.mobilefp_englishquizlet.TextToSpeechHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterForViewWords extends RecyclerView.Adapter<AdapterForViewWords.MyViewHolder>{
    Context context;
    ArrayList<Word> words;
    String mTopicID = "";
    public void setTopicId(String topicID) {mTopicID = topicID;}
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

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        holder.checkBoxStared.setChecked(currentWord.getStarredList().getOrDefault(userId, Boolean.FALSE));
        holder.checkBoxStared.setOnClickListener(ckb->{
            boolean isChecked = ((CheckBox) ckb).isChecked();
            currentWord.getStarredList().put(userId, isChecked);
            DatabaseReference wordsRef = FirebaseDatabase.getInstance().getReference("topics").child(mTopicID).child("words");
            wordsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot wordSnap : snapshot.getChildren()) {
                        Word snpWord = wordSnap.getValue(Word.class);
                        if(currentWord.getTerm().equals(snpWord.getTerm())) {
                            wordsRef.child(wordSnap.getKey()).child("starredList").child(userId).setValue(isChecked);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

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
        TextView txtviewTerm, txtviewDefinition, txtviewDescription, txtviewMastery;
        ImageButton btnSound;
        CardView cardView;
        CheckBox checkBoxStared;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtviewMastery = itemView.findViewById(R.id.txtviewMastery);
            txtviewTerm = itemView.findViewById(R.id.txtviewTerm);
            txtviewDefinition = itemView.findViewById(R.id.txtviewDefinition);
            txtviewDescription = itemView.findViewById(R.id.txtviewDescription);
            btnSound = itemView.findViewById(R.id.btnSound);
            cardView = itemView.findViewById(R.id.cardView);
            checkBoxStared = itemView.findViewById(R.id.check_box_stared);
        }
    }
}
