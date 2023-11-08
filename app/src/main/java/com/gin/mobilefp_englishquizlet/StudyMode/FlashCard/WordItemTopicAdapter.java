package com.gin.mobilefp_englishquizlet.StudyMode.FlashCard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.gin.mobilefp_englishquizlet.R;

import java.util.List;

public class WordItemTopicAdapter extends RecyclerView.Adapter<WordItemTopicAdapter.WordViewHolder>{
    private List<WordItemTopic> mWordItemTopic;

    public void setData(List<WordItemTopic> list){
        this.mWordItemTopic = list;
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
        WordItemTopic wordItemTopic = mWordItemTopic.get(position);

        holder.wordTerm.setText(wordItemTopic.getWordTerm());
        holder.wordDefinition.setText(wordItemTopic.getWordDefinition());

    }

    @Override
    public int getItemCount() {
        return mWordItemTopic.size();
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
