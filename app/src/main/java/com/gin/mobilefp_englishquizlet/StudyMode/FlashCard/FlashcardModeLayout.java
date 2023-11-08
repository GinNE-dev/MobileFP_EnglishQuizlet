package com.gin.mobilefp_englishquizlet.StudyMode.FlashCard;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.gin.mobilefp_englishquizlet.R;
import com.gin.mobilefp_englishquizlet.ViewPager.ViewPagerAdapterFlashcard;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class FlashcardModeLayout extends AppCompatActivity {

    AppCompatImageButton btnBack;
    AppCompatImageButton btnNext;
    TextView currentQuestion;
    TextView totalQuestion;
    ViewPager viewPagerFlashcard;
    List<WordItemTopic> mListQuestion;
    RecyclerView recyclerViewCategory;
    CategoryAdapter categoryAdapter;
    AppCompatImageButton btnOptions;
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

        mListQuestion = getListQuestion();

        ViewPagerAdapterFlashcard viewPagerAdapterFlashcard = new ViewPagerAdapterFlashcard(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mListQuestion);

        viewPagerFlashcard.setAdapter(viewPagerAdapterFlashcard);

        currentQuestion.setText("1");
        totalQuestion.setText(String.valueOf(mListQuestion.size()));

        viewPagerFlashcard.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentQuestion.setText(String.valueOf(position + 1));
                if(position == 0){
                    btnBack.setVisibility(View.GONE);
                    btnNext.setVisibility(View.VISIBLE);
                } else if (position == mListQuestion.size() - 1) {
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
//        recyclerViewCategory = findViewById(R.id.recyclerview_category);
//        categoryAdapter = new CategoryAdapter(this);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
//        recyclerViewCategory.setLayoutManager(linearLayoutManager);
//
//        categoryAdapter.setData(getListCategory());
//
//        recyclerViewCategory.setAdapter(categoryAdapter);
    }

    private List<WordItemTopic> getListQuestion(){
        List<WordItemTopic> list = new ArrayList<>();

        list.add(new WordItemTopic("wordTerm", "wordDefinition"));
        list.add(new WordItemTopic("wordTerm1", "wordDefinition1"));
        list.add(new WordItemTopic("wordTerm2", "wordDefinition2"));
        list.add(new WordItemTopic("wordTerm3", "wordDefinition3"));
        list.add(new WordItemTopic("wordTerm4", "wordDefinition4"));
        return list;
    }

//    private List<Category> getListCategory(){
//        List<Category> list = new ArrayList<>();
//
//        List<WordItemTopic> listWordItemTopics = new ArrayList<>();
//        listWordItemTopics.add(new WordItemTopic(R.drawable.sound_svgrepo_com, "term", "definition", "wordTerm", "wordDefinition"));
//        listWordItemTopics.add(new WordItemTopic(R.drawable.sound_svgrepo_com, "term2", "definition2", "wordTerm2", "wordDefinition2"));
//        listWordItemTopics.add(new WordItemTopic(R.drawable.sound_svgrepo_com, "term3", "definition3", "wordTerm3", "wordDefinition3"));
//
//        list.add(new Category(listWordItemTopics));
//        return list;
//    }


    private void showDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_flashcards_layout);

        dialog.show();

    }
}
