package com.gin.mobilefp_englishquizlet.Fragment;

import android.annotation.SuppressLint;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import com.gin.mobilefp_englishquizlet.Models.Word;
import com.gin.mobilefp_englishquizlet.R;
import com.gin.mobilefp_englishquizlet.StudyMode.FlashCard.WordItemTopic;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.HashMap;

public class FragmentQuestion extends Fragment {
    public interface LearnListener{
        void handleLearn(Word word);
    }

    TextView termWord;
    TextView termDefinition;
    View view;

    private LearnListener learnListener;
    public void registerLearn(LearnListener learnListener){
        this.learnListener = learnListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_question, container, false);

        termWord = view.findViewById(R.id.term_word);
        termDefinition = view.findViewById(R.id.definition_word);

        Bundle bundle = getArguments();
        if(bundle != null){
            Word wordItemTopic = (Word) bundle.get("question_object");
            if(wordItemTopic != null){
                termWord.setText(wordItemTopic.getTerm());
                termDefinition.setText(wordItemTopic.getDefinition());
            }

            ((EasyFlipView) view.findViewById(R.id.view_flip_component)).setOnFlipListener((v, state)->{
                if(state.equals(EasyFlipView.FlipState.BACK_SIDE)){
                    if(learnListener != null) learnListener.handleLearn(wordItemTopic);
                }

            });
        }

        return view;
    }
}