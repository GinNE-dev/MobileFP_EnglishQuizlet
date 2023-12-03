package com.gin.mobilefp_englishquizlet.StudyMode.FlashCard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.gin.mobilefp_englishquizlet.Models.Word;
import com.gin.mobilefp_englishquizlet.R;
import com.gin.mobilefp_englishquizlet.ViewPager.ViewPagerAdapterFlashcard;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FlashcardModeLayout extends AppCompatActivity {

    AppCompatImageButton btnBack;
    AppCompatImageButton btnNext;
    TextView currentQuestion;
    TextView totalQuestion;
    ViewPager viewPagerFlashcard;
    RecyclerView recyclerViewCategory;
    CategoryAdapter categoryAdapter;
    AppCompatImageButton btnOptions;
    String mLeanerID;
    String mTopicID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashcard_mode_layout);

        btnOptions = findViewById(R.id.btn_FlashCardsMode_Options);
        btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        viewPagerFlashcard = findViewById(R.id.viewpager_flashcard);
        currentQuestion = findViewById(R.id.current_question);
        totalQuestion = findViewById(R.id.total_question);
        btnBack = findViewById(R.id.btn_back);
        btnNext = findViewById(R.id.btn_next);

        mLeanerID = getIntent().getStringExtra("user_id");
        mTopicID = getIntent().getStringExtra("topic_id");

        updateUI();

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

    private void updateUI(){
        DatabaseReference wordsOfTopicRef = FirebaseDatabase.getInstance().getReference("topics").child(mTopicID).child("words");
        wordsOfTopicRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Word> list = new ArrayList<>();
                for (DataSnapshot wordSnap : snapshot.getChildren()) {
                    Word currentWord = wordSnap.getValue(Word.class);
                    list.add(currentWord);
                }

                ViewPagerAdapterFlashcard viewPagerAdapterFlashcard = new ViewPagerAdapterFlashcard(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, list, mTopicID);

                viewPagerFlashcard.setAdapter(viewPagerAdapterFlashcard);

                currentQuestion.setText("1");
                totalQuestion.setText(String.valueOf(list.size()));

                viewPagerFlashcard.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        //Toast.makeText(FlashcardModeLayout.this, ""+position, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPageSelected(int position) {
                        currentQuestion.setText(String.valueOf(position + 1));
                        if(position == 0){
                            btnBack.setVisibility(View.GONE);
                            btnNext.setVisibility(View.VISIBLE);
                        } else if (position == list.size() - 1) {
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });

    }


    private void showDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_flashcards_layout);

        dialog.show();

    }
}
