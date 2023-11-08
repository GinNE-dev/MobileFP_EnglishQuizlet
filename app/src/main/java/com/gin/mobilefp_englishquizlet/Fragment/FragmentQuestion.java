package com.gin.mobilefp_englishquizlet.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import com.gin.mobilefp_englishquizlet.R;
import com.gin.mobilefp_englishquizlet.StudyMode.FlashCard.WordItemTopic;

public class FragmentQuestion extends Fragment {
    TextView termWord;
    TextView termDefinition;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_question, container, false);

        termWord = view.findViewById(R.id.term_word);
        termDefinition = view.findViewById(R.id.definition_word);

        Bundle bundle = getArguments();
        if(bundle != null){
            WordItemTopic wordItemTopic = (WordItemTopic) bundle.get("question_object");
            if(wordItemTopic != null){
                termWord.setText(wordItemTopic.getWordTerm());
                termDefinition.setText(wordItemTopic.getWordDefinition());
            }
        }

        return view;
    }
}