package com.gin.mobilefp_englishquizlet.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.gin.mobilefp_englishquizlet.Models.Word;
import com.gin.mobilefp_englishquizlet.R;
import com.gin.mobilefp_englishquizlet.TextToSpeechHelper;
import com.gin.mobilefp_englishquizlet.ViewPager.ViewPagerAdapterFlashcard;
import com.wajahatkarim3.easyflipview.EasyFlipView;

public class FragmentQuestion extends Fragment {
    public interface LearnListener{
        void handleLearn(Word word);
    }

    TextView termWord;
    TextView termDefinition;
    ImageButton buttonSound;
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
        buttonSound = view.findViewById(R.id.btn_sound);

        Bundle bundle = getArguments();
        if(bundle != null){
            Word wordItemTopic = (Word) bundle.get("question_object");
            boolean isRevert = bundle.getBoolean("is_revert");
            if(wordItemTopic != null){
                String term = wordItemTopic.getTerm();
                String definition = wordItemTopic.getDefinition();
                termWord.setText(isRevert ? definition : term);
                termDefinition.setText(isRevert ? term : definition);
            }

            buttonSound.setOnClickListener(btn->{
                TextToSpeechHelper.initialize(getContext(), new TextToSpeechHelper.OnInitializationListener() {
                    @Override
                    public void onInitialized() {
                        TextToSpeechHelper.speak(wordItemTopic.getTerm());
                    }

                    @Override
                    public void onInitializationFailed() {

                    }
                });
            });

            ((EasyFlipView) view.findViewById(R.id.view_flip_component)).setOnFlipListener((v, state)->{
                if(state.equals(EasyFlipView.FlipState.BACK_SIDE)){
                    if(learnListener != null) learnListener.handleLearn(wordItemTopic);
                }
            });
        }

        return view;
    }
}