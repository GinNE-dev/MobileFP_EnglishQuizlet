package com.gin.mobilefp_englishquizlet.StudyMode;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gin.mobilefp_englishquizlet.Models.Word;
import com.gin.mobilefp_englishquizlet.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class WordInResultAdapter extends RecyclerView.Adapter<WordInResultAdapter.WordInResultHolder> {
    public class WordInResultHolder extends RecyclerView.ViewHolder{

        private TextView textViewTerm;
        private TextView textViewDefinition;
        private ImageButton imgButtonSound;
        public WordInResultHolder(@NonNull View itemView) {
            super(itemView);

            this.textViewTerm = itemView.findViewById(R.id.text_view_term);
            this.textViewDefinition = itemView.findViewById(R.id.text_view_definition);
            this.imgButtonSound = itemView.findViewById(R.id.image_button_sound);
        }

        public TextView getTextViewTerm() {
            return textViewTerm;
        }

        public TextView getTextViewDefinition() {
            return textViewDefinition;
        }

        public ImageButton getImgButtonSound() {
            return imgButtonSound;
        }
    }

    private ArrayList<Word> mWords;

    public WordInResultAdapter(ArrayList<Word> words){
        this.mWords = words;
    }

    @NonNull
    @Override
    public WordInResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_in_result, parent, false);
        return new WordInResultHolder(row) {};
    }

    @Override
    public void onBindViewHolder(@NonNull WordInResultHolder holder, int position) {
        Word word = mWords.get(position);

        holder.getTextViewTerm().setText(word.getTerm());
        holder.getTextViewDefinition().setText(word.getDefinition());
        holder.getImgButtonSound().setOnClickListener(btn->{
            //TODO: sound
        });
    }

    @Override
    public int getItemCount() {
        return mWords != null ? mWords.size() : 0;
    }
}
