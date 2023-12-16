package com.gin.mobilefp_englishquizlet.StudyMode;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.gin.mobilefp_englishquizlet.Models.Record;
import com.gin.mobilefp_englishquizlet.Models.Word;
import com.gin.mobilefp_englishquizlet.R;
import com.gin.mobilefp_englishquizlet.RequestHelper;
import com.gin.mobilefp_englishquizlet.TextToSpeechHelper;
import com.gin.mobilefp_englishquizlet.Utils.ResultPopup;
import com.gin.mobilefp_englishquizlet.Utils.SimpleUTF8Normalizer;
import com.gin.mobilefp_englishquizlet.Utils.SoundResult;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

public class TypoModeLayout extends AppCompatActivity {
    AppCompatImageButton btnOptions, btnBack;
    private String mTopicID;
    private ArrayList<Word> mWords;
    private ArrayList<Integer> mResults;
    private ArrayList<String> mAnswers;
    private BottomSheetDialog mDialog;
    private TextView textViewTotal;
    private TextView textViewCurrent;
    private TextView textViewQuestion;
    private TextView textViewWarning;
    private EditText editTextAnswer;
    private View viewQuestionCard;
    private Button buttonNext;
    private ImageButton imgButtonTextToSpeech;
    private boolean mIsRevert = false;
    private boolean mIsLearnStarredOnly = false;
    private long mStartTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.typo_mode_layout);
        initViews();

        btnOptions = findViewById(R.id.btn_Typo_Options);
        btnBack = findViewById(R.id.btnBack);
        btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        btnBack.setOnClickListener(v -> {
            finish();
        });

        mTopicID = getIntent().getStringExtra("topic_id");
        initComponents();
        registerEvents();
    }

    private void initViews(){
        textViewCurrent = findViewById(R.id.text_view_current);
        textViewTotal = findViewById(R.id.text_view_total);
        editTextAnswer = findViewById(R.id.edit_text_answer);
        textViewQuestion = findViewById(R.id.text_view_question);
        imgButtonTextToSpeech = findViewById(R.id.img_button_text_to_speech);
        viewQuestionCard = findViewById(R.id.card_view_question);
        textViewWarning = findViewById(R.id.text_view_warning);
        buttonNext = findViewById(R.id.button_next);
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

    private void initComponents(){
        mWords =  new ArrayList<>();
        mResults = new ArrayList<>();
        mAnswers = new ArrayList<>();
        DatabaseReference wordsOfTopicRef = FirebaseDatabase.getInstance().getReference("topics").child(mTopicID).child("words");
        wordsOfTopicRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                String userId = user.getUid();
                for (DataSnapshot wordSnap : snapshot.getChildren()) {
                    Word currentWord = wordSnap.getValue(Word.class);
                    if(mIsLearnStarredOnly){
                        if(currentWord.getStarredList().getOrDefault(userId, Boolean.FALSE)) mWords.add(currentWord);
                    }else{
                        mWords.add(currentWord);
                    }
                }

                textViewTotal.setText(String.valueOf(mWords.size()));
                restartGame();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //TODO: query error
            }
        });
    }

    private void restartGame(){
        mResults.clear();
        mAnswers.clear();
        mStartTime = new Date().getTime();
        setupQuestion(0, mIsRevert);
        textViewWarning.setVisibility(mIsRevert ? View.GONE : View.VISIBLE);
    }

    private void setupQuestion(int pos, boolean isRevert){
        if(mWords.size()==0){
            viewQuestionCard.setVisibility(View.GONE);
            textViewCurrent.setText(String.valueOf(0));
            buttonNext.setVisibility(View.GONE);
            return;
        }

        viewQuestionCard.setVisibility(View.VISIBLE);
        buttonNext.setVisibility(View.VISIBLE);

        if(pos >= mWords.size()){
            int correct = 0;
            for (Integer result : mResults) correct += result;
            int score = Math.round(correct*100.0f/mResults.size());

            if(!mIsLearnStarredOnly){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userId = user.getUid();
                FirebaseDatabase.getInstance().getReference("topics").child(mTopicID).child("scoreRecords")
                .push().setValue(new Record(userId, score, Record.LearnMode.Typo, new Date().getTime() - mStartTime));
            }

            Intent intentResult = new Intent(TypoModeLayout.this, ResultsLayout.class);
            intentResult.putExtra("correct", correct);
            intentResult.putExtra("incorrect", mResults.size()-correct);
            intentResult.putExtra("score", score);
            intentResult.putParcelableArrayListExtra("words", mWords);
            intentResult.putIntegerArrayListExtra("results", mResults);
            intentResult.putStringArrayListExtra("answers", mAnswers);
            startActivityForResult(intentResult, RequestHelper.MULTIPLE_CHOICE_RESULT);
            return;
        }

        Word word = mWords.get(pos);

        textViewCurrent.setText(String.valueOf(pos+1));
        editTextAnswer.clearFocus();
        editTextAnswer.setText("");
        if(word != null){
            textViewQuestion.setText(isRevert ? word.getDefinition() :word.getTerm());
            imgButtonTextToSpeech.setOnClickListener(btn->{
                TextToSpeechHelper.initialize(getBaseContext(), new TextToSpeechHelper.OnInitializationListener() {
                    @Override
                    public void onInitialized() {
                        TextToSpeechHelper.speak(word.getTerm());
                    }

                    @Override
                    public void onInitializationFailed() {
                        Toast.makeText(TypoModeLayout.this, R.string.message_speaker_not_available, Toast.LENGTH_SHORT).show();
                    }
                });
            });

            imgButtonTextToSpeech.setVisibility(isRevert ?  View.GONE : View.VISIBLE);
        }
    }

    private void processLearn(String answer){
        mAnswers.add(answer);
        Word word = mWords.get(mResults.size());
        String actualAnswer = mIsRevert ? word.getTerm() : word.getDefinition();
        //Only accept answers that are either correct with all diacritics or no diacritics at all
        String normalizedActualAnswer = SimpleUTF8Normalizer.normalize(actualAnswer.trim());
        answer = answer.toLowerCase();
        boolean isCorrect = answer.equals(actualAnswer.toLowerCase()) || answer.equals(normalizedActualAnswer);

        //Accept all
        //boolean isCorrect = SimpleUTF8Normalizer.equals(answer.trim(), actualAnswer.trim());
        mResults.add(isCorrect ? 1 : 0);

        if(isCorrect){
            SoundResult.playSoundEffect(this, SoundResult.Type.Correct);
            ResultPopup.show(this, ResultPopup.PopupType.CorrectAnswer, ResultPopup.DURATION_SHORT);
        }else{
            SoundResult.playSoundEffect(this, SoundResult.Type.Incorrect);
            ResultPopup.show(this, ResultPopup.PopupType.IncorrectAnswer, ResultPopup.DURATION_SHORT);
        }

        setupQuestion(mResults.size(), mIsRevert);
    }

    private void registerEvents(){
        buttonNext.setOnClickListener(btn->{
            String answer = editTextAnswer.getText().toString();

            if(TextUtils.isEmpty(answer)){
                editTextAnswer.requestFocus();
                return;
            }

            processLearn(answer);
        });
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
