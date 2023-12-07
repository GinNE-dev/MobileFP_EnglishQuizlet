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
import com.gin.mobilefp_englishquizlet.TextToSpeechHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class WordInResultAdapter extends RecyclerView.Adapter<WordInResultAdapter.WordInResultHolder> {
    public class WordInResultHolder extends RecyclerView.ViewHolder{

        private TextView textViewTerm;
        private TextView textViewDefinition;
        private TextView textViewYourAnswer;
        private ImageButton imgButtonSound;
        public WordInResultHolder(@NonNull View itemView) {
            super(itemView);

            this.textViewTerm = itemView.findViewById(R.id.text_view_term);
            this.textViewDefinition = itemView.findViewById(R.id.text_view_definition);
            this.imgButtonSound = itemView.findViewById(R.id.image_button_sound);
            this.textViewYourAnswer = itemView.findViewById(R.id.text_view_your_answer);
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

        public TextView getTextViewYourAnswer() {return textViewYourAnswer;}
    }

    private ArrayList<Word> mWords;
    private ArrayList<String> mAnswers;
    public WordInResultAdapter(ArrayList<Word> words, ArrayList<String> answers){
        this.mWords = words;
        this.mAnswers = answers;
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
        String answer = mAnswers.get(position);

        holder.getTextViewTerm().setText(word.getTerm());
        holder.getTextViewDefinition().setText(word.getDefinition());
        holder.getTextViewYourAnswer().setText(answer);
        holder.getImgButtonSound().setOnClickListener(btn->{
            TextToSpeechHelper.initialize(holder.getTextViewTerm().getContext(), new TextToSpeechHelper.OnInitializationListener() {
                @Override
                public void onInitialized() {
                    TextToSpeechHelper.speak(word.getTerm());
                }

                @Override
                public void onInitializationFailed() {

                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return mWords != null ? mWords.size() : 0;
    }
}
