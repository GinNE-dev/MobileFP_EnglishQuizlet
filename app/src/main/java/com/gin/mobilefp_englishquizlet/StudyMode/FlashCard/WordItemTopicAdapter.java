package com.gin.mobilefp_englishquizlet.StudyMode.FlashCard;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.gin.mobilefp_englishquizlet.Models.Word;
import com.gin.mobilefp_englishquizlet.R;

import java.util.List;

public class WordItemTopicAdapter extends RecyclerView.Adapter<WordItemTopicAdapter.WordViewHolder>{
    private List<Word> mWord;

    public void setData(List<Word> list){
        this.mWord = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.term_flip_item, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        Word wordItemTopic = mWord.get(position);

        //holder.wordTerm.setText(wordItemTopic.getTerm());
        //holder.wordDefinition.setText(wordItemTopic.getDefinition());

        AppCompatImageButton btn = holder.itemView.findViewById(R.id.btn_sound);
    }

    @Override
    public int getItemCount() {
        return mWord.size();
    }

    public class WordViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageButton btnSound;
        TextView term;
        TextView definition;
        TextView wordTerm;
        TextView wordDefinition;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);

            btnSound = itemView.findViewById(R.id.btn_sound);
            term = itemView.findViewById(R.id.term);
            definition = itemView.findViewById(R.id.definition);
            wordTerm = itemView.findViewById(R.id.term_word);
            wordDefinition = itemView.findViewById(R.id.definition_word);
        }
    }

}
