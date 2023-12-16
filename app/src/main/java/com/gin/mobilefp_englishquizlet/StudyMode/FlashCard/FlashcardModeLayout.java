package com.gin.mobilefp_englishquizlet.StudyMode.FlashCard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gin.mobilefp_englishquizlet.Models.Word;
import com.gin.mobilefp_englishquizlet.R;
import com.gin.mobilefp_englishquizlet.TextToSpeechHelper;
import com.gin.mobilefp_englishquizlet.ViewPager.ViewPagerAdapterFlashcard;
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

public class FlashcardModeLayout extends AppCompatActivity {
    AppCompatImageButton btnExit;
    AppCompatImageButton btnBack;
    AppCompatImageButton btnNext;
    TextView currentQuestion;
    TextView totalQuestion;
    ViewPager viewPagerFlashcard;
    AppCompatImageButton btnOptions;
    String mLeanerID;
    String mTopicID;
    private BottomSheetDialog mDialog;
    private boolean mIsRevert = false;
    private boolean mIsLearnStarredOnly = false;
    private ArrayList<Word> mWords;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashcard_mode_layout);
        initViews();
        btnOptions = findViewById(R.id.btn_FlashCardsMode_Options);
        btnExit = findViewById(R.id.btnExit);
        btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        btnExit.setOnClickListener(v -> {
            finish();
        });

        viewPagerFlashcard = findViewById(R.id.viewpager_flashcard);
        currentQuestion = findViewById(R.id.current_question);
        totalQuestion = findViewById(R.id.total_question);
        btnBack = findViewById(R.id.btn_back);
        btnNext = findViewById(R.id.btn_next);

        mLeanerID = getIntent().getStringExtra("user_id");
        mTopicID = getIntent().getStringExtra("topic_id");

        initComponents();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPagerFlashcard.setCurrentItem(viewPagerFlashcard.getCurrentItem() - 1);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPagerFlashcard.setCurrentItem(viewPagerFlashcard.getCurrentItem() + 1);
            }
        });
    }

    private void speak(@NonNull String text){
        TextToSpeechHelper.initialize(FlashcardModeLayout.this, new TextToSpeechHelper.OnInitializationListener() {
            @Override
            public void onInitialized() {
                if(!mIsRevert) TextToSpeechHelper.speak(text);
            }

            @Override
            public void onInitializationFailed() {
                Toast.makeText(FlashcardModeLayout.this, R.string.message_speaker_not_available, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initComponents(){
        mWords = new ArrayList<>();
        DatabaseReference wordsOfTopicRef = FirebaseDatabase.getInstance().getReference("topics").child(mTopicID).child("words");
        wordsOfTopicRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
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

                setupForLearn(mWords, mIsRevert);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });
    }

    private void setupForLearn(ArrayList<Word> words, boolean isRevert){
        ViewPagerAdapterFlashcard viewPagerAdapterFlashcard = new ViewPagerAdapterFlashcard(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, words, mTopicID, isRevert);

        viewPagerFlashcard.setAdapter(viewPagerAdapterFlashcard);

        currentQuestion.setText(words.size() != 0 ? "1" : "0");
        totalQuestion.setText(String.valueOf(words.size()));

        viewPagerFlashcard.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                speak(mWords.get(position).getTerm());
                currentQuestion.setText(String.valueOf(position + 1));
                if(position == 0){
                    firstCardUI();
                } else if (position == words.size() - 1) {
                    btnBack.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.GONE);
                } else {
                    btnBack.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if(mWords.size()>0) speak(mWords.get(0).getTerm());
    }

    private void firstCardUI(){
        btnBack.setVisibility(View.GONE);
        btnNext.setVisibility(View.VISIBLE);
    }

    private void initViews(){
        mDialog =  new BottomSheetDialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.bottom_sheet_flashcards_layout);
        RadioButton radioButtonTerm = mDialog.findViewById(R.id.btn_term);
        RadioButton radioButtonDefinition = mDialog.findViewById(R.id.btn_defition);
        RadioButton radioButtonLearnAll = mDialog.findViewById(R.id.btn_all_word);
        RadioButton radioButtonLearnStarred = mDialog.findViewById(R.id.btn_starred_word);
        radioButtonLearnAll.setChecked(!mIsLearnStarredOnly);
        radioButtonLearnStarred.setChecked(mIsLearnStarredOnly);
        radioButtonTerm.setChecked(!mIsRevert);
        radioButtonDefinition.setChecked(mIsRevert);

    }

    private void showDialog() {
        final BottomSheetDialog dialog = mDialog;
        final RadioButton radioButtonTerm = dialog.findViewById(R.id.btn_term);
        final RadioButton radioButtonDefinition = dialog.findViewById(R.id.btn_defition);
        final Button buttonShuffle = dialog.findViewById(R.id.button_shuffle);
        final Button buttonRestartLearn = dialog.findViewById(R.id.button_restart_learn);
        final RadioButton radioButtonLearnAll = dialog.findViewById(R.id.btn_all_word);
        final RadioButton radioButtonLearnStarred = dialog.findViewById(R.id.btn_starred_word);

        radioButtonTerm.setOnClickListener(v->{
            if(mIsRevert){
                mIsRevert = false;
                ((ViewPagerAdapterFlashcard) viewPagerFlashcard.getAdapter()).mIsRevert = false;

                viewPagerFlashcard.getAdapter().notifyDataSetChanged();
            }

            radioButtonTerm.setChecked(true);
            radioButtonDefinition.setChecked(false);
        });

        radioButtonDefinition.setOnClickListener(v->{
            if(!mIsRevert){
                mIsRevert = true;
                ((ViewPagerAdapterFlashcard) viewPagerFlashcard.getAdapter()).mIsRevert = true;

                viewPagerFlashcard.getAdapter().notifyDataSetChanged();
            }

            radioButtonTerm.setChecked(false);
            radioButtonDefinition.setChecked(true);
        });

        buttonShuffle.setOnClickListener(btn->{
            Random rand = new Random(Math.abs((int) new Date().getTime()));
            Collections.shuffle(mWords, rand);
            setupForLearn(mWords, mIsRevert);
            firstCardUI();
        });

        buttonRestartLearn.setOnClickListener(btn->{
            setupForLearn(mWords, mIsRevert);
            firstCardUI();
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
}
