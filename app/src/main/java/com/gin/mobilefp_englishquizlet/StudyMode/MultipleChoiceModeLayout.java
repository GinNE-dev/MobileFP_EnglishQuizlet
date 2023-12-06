package com.gin.mobilefp_englishquizlet.StudyMode;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.gin.mobilefp_englishquizlet.Models.Word;
import com.gin.mobilefp_englishquizlet.R;
import com.gin.mobilefp_englishquizlet.RequestHelper;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MultipleChoiceModeLayout extends AppCompatActivity {
    AppCompatImageButton btnOptions;
    private ArrayList<Word> mWords;
    private ArrayList<String> mAnswers;
    private String mTopicID;
    private TextView textViewCurrent;
    private TextView textViewTotal;
    private TextView textViewQuestion;
    private RadioGroup radioGroupAnswers;
    private Boolean mIsRevert = false;
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
        mDialog =  new BottomSheetDialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.bottom_sheet_multiplechoices_typo_layout);
    }

    private void registerEvents(){
        radioGroupAnswers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId != -1){
                    RadioButton rBtn = (RadioButton) group.findViewById(checkedId);
                    processLearn(rBtn.getText().toString(), textViewQuestion.getText().toString());
                    group.clearCheck();
                }
            }
        });
    }

    private void processLearn(String answer, String actualAnswer){
        mAnswers.add(answer);
        if(actualAnswer.equals(answer)){
            //TODO: Learn
        }

        setupQuestion(mAnswers.size(), mIsRevert);
    }

    private void initComponents(){
        mWords = new ArrayList<>();
        mAnswers = new ArrayList<>();
        DatabaseReference wordsOfTopicRef = FirebaseDatabase.getInstance().getReference("topics").child(mTopicID).child("words");
        wordsOfTopicRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot wordSnap : snapshot.getChildren()) {
                    Word currentWord = wordSnap.getValue(Word.class);
                    mWords.add(currentWord);
                }
                textViewTotal.setText(String.valueOf(mWords.size()));
                setupQuestion(0, mIsRevert);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });
    }

    private void setupQuestion(int pos, boolean isRevert){
        if(pos >= mWords.size()){

            int correct = 0;
            for (int i=0; i<mWords.size(); i++) {
                String actualAnswer = isRevert ? mWords.get(i).getTerm() : mWords.get(i).getDefinition();
                if(mAnswers.get(i).equals(actualAnswer)) correct++;
            }
            int score = Math.round(correct*100.0f/mAnswers.size());
            Intent intentResult = new Intent(MultipleChoiceModeLayout.this, ResultsLayout.class);
            intentResult.putExtra("correct", correct);
            intentResult.putExtra("incorrect", mAnswers.size()-correct);
            intentResult.putExtra("score", score);
            intentResult.putParcelableArrayListExtra("words", mWords);
            intentResult.putStringArrayListExtra("answers", mAnswers);
            intentResult.putExtra("is_revert", mIsRevert);
            startActivityForResult(intentResult, RequestHelper.MULTIPLE_CHOICE_RESULT);
            return;
        }

        Word word = mWords.get(pos);

        textViewCurrent.setText(String.valueOf(pos+1));
        if(word != null){
            textViewQuestion.setText(isRevert ? word.getDefinition() :word.getTerm());
            List<String> answers = new ArrayList<>();
            Random rand = new Random(Math.abs((int) new Date().getTime()));

            HashMap<Integer, Boolean> indexes = new HashMap<>();
            indexes.put(pos, Boolean.TRUE);

            int randIdx;
            while (indexes.size() < 4 && mWords.size()>=4){
                randIdx = Math.max(rand.nextInt() % mWords.size(), 0);
                if(indexes.getOrDefault(randIdx, Boolean.FALSE)) continue;
                indexes.put(randIdx, Boolean.TRUE);
            }

            for(Integer idx:indexes.keySet()){
                answers.add(isRevert ? mWords.get(idx).getTerm() :mWords.get(idx).getDefinition());
            }

            Collections.shuffle(answers, rand);
            for (int i=0; i<answers.size(); i++) {
                ((RadioButton) radioGroupAnswers.getChildAt(i)).setText(answers.get(i));
            }
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
