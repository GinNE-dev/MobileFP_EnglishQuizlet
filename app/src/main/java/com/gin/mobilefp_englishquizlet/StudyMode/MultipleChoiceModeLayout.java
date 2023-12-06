package com.gin.mobilefp_englishquizlet.StudyMode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SyncRequest;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.gin.mobilefp_englishquizlet.Models.Topic;
import com.gin.mobilefp_englishquizlet.Models.Word;
import com.gin.mobilefp_englishquizlet.R;
import com.gin.mobilefp_englishquizlet.RequestHelper;
import com.gin.mobilefp_englishquizlet.TextToSpeechHelper;
import com.gin.mobilefp_englishquizlet.Utils.ResultPopup;
import com.gin.mobilefp_englishquizlet.Utils.SimpleUTF8Normalizer;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MultipleChoiceModeLayout extends AppCompatActivity {
    AppCompatImageButton btnOptions;
    private ArrayList<Word> mWords;
    private ArrayList<Integer> mAnswers;
    private ArrayList<Word> mAllWord;
    private String mTopicID;
    private TextView textViewCurrent;
    private TextView textViewTotal;
    private TextView textViewQuestion;
    private RadioGroup radioGroupAnswers;
    private ImageButton imgButtonTextToSpeech;
    private View viewQuestionCard;
    private Boolean mIsRevert = false;
    private boolean mIsLearnStarredOnly = false;
    private BottomSheetDialog mDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiple_choice_mode_layout);
        initViews();

        btnOptions = findViewById(R.id.btn_MultipleChoicesMode_Options);
        btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        mTopicID = getIntent().getStringExtra("topic_id");

        initComponents();
        registerEvents();
    }

    private void initViews(){
        textViewCurrent = findViewById(R.id.text_view_current);
        textViewTotal = findViewById(R.id.text_view_total);
        radioGroupAnswers = findViewById(R.id.radio_group_answers);
        textViewQuestion = findViewById(R.id.text_view_question);
        imgButtonTextToSpeech = findViewById(R.id.img_button_text_to_speech);
        viewQuestionCard = findViewById(R.id.card_view_question);
        mDialog =  new BottomSheetDialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.bottom_sheet_multiplechoices_typo_layout);

        RadioButton radioButtonTerm = mDialog.findViewById(R.id.btn_term);
        RadioButton radioButtonDefinition = mDialog.findViewById(R.id.btn_defition);
        RadioButton radioButtonLearnAll = mDialog.findViewById(R.id.btn_all_word);
        RadioButton radioButtonLearnStarred = mDialog.findViewById(R.id.btn_starred_word);
        radioButtonLearnAll.setChecked(!mIsLearnStarredOnly);
        radioButtonLearnStarred.setChecked(mIsLearnStarredOnly);
        radioButtonTerm.setChecked(!mIsRevert);
        radioButtonDefinition.setChecked(mIsRevert);
    }

    private void registerEvents(){
        radioGroupAnswers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId != -1){
                    RadioButton rBtn = (RadioButton) group.findViewById(checkedId);
                    processLearn(rBtn.getText().toString());
                    group.clearCheck();
                }
            }
        });
    }

    private void processLearn(String answer){
        Word word = mWords.get(mAnswers.size());
        String actualAnswer = mIsRevert ? word.getTerm() : word.getDefinition();
        boolean isCorrect = actualAnswer.equals(answer);
        mAnswers.add( isCorrect ? 1 : 0);

        if(isCorrect){
            ResultPopup.show(this, ResultPopup.PopupType.CorrectAnswer, ResultPopup.DURATION_SHORT);
        }else{
            ResultPopup.show(this, ResultPopup.PopupType.IncorrectAnswer, ResultPopup.DURATION_SHORT);
        }

        setupQuestion(mAnswers.size(), mIsRevert);
    }

    private void initComponents(){
        mWords = new ArrayList<>();
        mAnswers = new ArrayList<>();
        mAllWord = new ArrayList<>();

        DatabaseReference wordsOfTopicRef = FirebaseDatabase.getInstance().getReference("topics").child(mTopicID).child("words");
        wordsOfTopicRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                String userId = user.getUid();
                for (DataSnapshot wordSnap : snapshot.getChildren()) {
                    Word currentWord = wordSnap.getValue(Word.class);
                    mAllWord.add(currentWord);
                    if(mIsLearnStarredOnly){
                        if(currentWord.getStarredList().getOrDefault(userId, Boolean.FALSE)) mWords.add(currentWord);
                    }else{
                        mWords.add(currentWord);
                    }
                }

                textViewTotal.setText(String.valueOf(mWords.size()));
                setupQuestion(0, mIsRevert);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //TODO: query error
            }
        });
    }

    private void setupQuestion(int pos, boolean isRevert){
        if(mWords.size()==0){
            viewQuestionCard.setVisibility(View.GONE);
            textViewCurrent.setText(String.valueOf(0));
            return;
        };

        viewQuestionCard.setVisibility(View.VISIBLE);
        if(pos >= mWords.size()){
            int correct = 0;
            for (Integer result : mAnswers) correct += result;
            int score = Math.round(correct*100.0f/mAnswers.size());
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String userId = user.getUid();
            FirebaseDatabase.getInstance().getReference("topics").child(mTopicID)
                    .child("scoreRecords").child(userId).setValue(Integer.valueOf(score));

            Intent intentResult = new Intent(MultipleChoiceModeLayout.this, ResultsLayout.class);
            intentResult.putExtra("correct", correct);
            intentResult.putExtra("incorrect", mAnswers.size()-correct);
            intentResult.putExtra("score", score);
            intentResult.putParcelableArrayListExtra("words", mWords);
            intentResult.putIntegerArrayListExtra("answers", mAnswers);
            startActivityForResult(intentResult, RequestHelper.MULTIPLE_CHOICE_RESULT);
            return;
        }

        Word word = mWords.get(pos);

        textViewCurrent.setText(String.valueOf(pos+1));
        if(word != null){
            textViewQuestion.setText(isRevert ? word.getDefinition() :word.getTerm());
            List<String> answers = new ArrayList<>();
            Random rand = new Random(Math.abs((int) new Date().getTime()));

            answers.add(mIsRevert ? word.getTerm() : word.getDefinition());

            while (answers.size() < 4 && mAllWord.size()>=4){
                int randIdx = Math.max(rand.nextInt() % mAllWord.size(), 0);
                Word w = mAllWord.get(randIdx);
                assert  w != null;
                String candidateAnswer = mIsRevert ? w.getTerm() : w.getDefinition();
                if(!answers.contains(candidateAnswer)) answers.add(candidateAnswer);
            }

            Collections.shuffle(answers, rand);
            for (int i=0; i<answers.size(); i++) {
                ((RadioButton) radioGroupAnswers.getChildAt(i)).setText(answers.get(i));
            }

            imgButtonTextToSpeech.setOnClickListener(btn->{
                TextToSpeechHelper.initialize(getBaseContext(), new TextToSpeechHelper.OnInitializationListener() {
                    @Override
                    public void onInitialized() {
                        TextToSpeechHelper.speak(word.getTerm());
                    }

                    @Override
                    public void onInitializationFailed() {

                    }
                });
            });

            imgButtonTextToSpeech.setVisibility(isRevert ?  View.GONE : View.VISIBLE);
        }
    }

    private void restartGame(){
        mAnswers.clear();
        setupQuestion(0, mIsRevert);
    }

    private void showDialog() {
        final BottomSheetDialog dialog = mDialog;

        final RadioButton radioButtonTerm = dialog.findViewById(R.id.btn_term);
        final RadioButton radioButtonDefinition = dialog.findViewById(R.id.btn_defition);
        final Button buttonShuffle = dialog.findViewById(R.id.button_shuffle);
        final RadioButton radioButtonLearnAll = dialog.findViewById(R.id.btn_all_word);
        final RadioButton radioButtonLearnStarred = dialog.findViewById(R.id.btn_starred_word);

        radioButtonTerm.setOnClickListener(v->{
            if(mIsRevert){
                mIsRevert = false;
                restartGame();
            }

            radioButtonTerm.setChecked(true);
            radioButtonDefinition.setChecked(false);
        });

        radioButtonDefinition.setOnClickListener(v->{
            if(!mIsRevert){
                mIsRevert = true;
                restartGame();
            }

            radioButtonTerm.setChecked(false);
            radioButtonDefinition.setChecked(true);
        });

        buttonShuffle.setOnClickListener(btn->{
            Random rand = new Random(Math.abs((int) new Date().getTime()));
            Collections.shuffle(mWords, rand);
            restartGame();
        });

        radioButtonLearnAll.setOnClickListener(btn->{
            if(mIsLearnStarredOnly){
                mIsLearnStarredOnly = false;
                initComponents();
            }
        });

        radioButtonLearnStarred.setOnClickListener(btn->{
            if(!mIsLearnStarredOnly){
                mIsLearnStarredOnly = true;
                initComponents();
            }
        });

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CANCELED){
            restartGame();
            return;
        }

        if(resultCode == RESULT_OK){
            if (data == null) return;
            int type = data.getIntExtra("command", RequestHelper.CommandFromResult.COMMAND_UNKNOWN);
            switch (type){
                case RequestHelper.CommandFromResult.COMMAND_RESTART_TEST:
                    restartGame();
                break;
                case RequestHelper.CommandFromResult.COMMAND_NEW_TEST:
                    finish();
                default:
            }
        }
    }
}
