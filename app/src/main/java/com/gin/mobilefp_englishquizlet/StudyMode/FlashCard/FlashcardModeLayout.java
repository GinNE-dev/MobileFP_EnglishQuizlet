package com.gin.mobilefp_englishquizlet.StudyMode.FlashCard;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gin.mobilefp_englishquizlet.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class FlashcardModeLayout extends AppCompatActivity {

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

        recyclerViewCategory = findViewById(R.id.recyclerview_category);
        categoryAdapter = new CategoryAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerViewCategory.setLayoutManager(linearLayoutManager);

        categoryAdapter.setData(getListCategory());

        recyclerViewCategory.setAdapter(categoryAdapter);
    }

    private List<Category> getListCategory(){
        List<Category> list = new ArrayList<>();

        List<WordItemTopic> listWordItemTopics = new ArrayList<>();
        listWordItemTopics.add(new WordItemTopic(R.drawable.sound_svgrepo_com, "term", "definition", "wordTerm", "wordDefinition"));
        listWordItemTopics.add(new WordItemTopic(R.drawable.sound_svgrepo_com, "term2", "definition2", "wordTerm2", "wordDefinition2"));
        listWordItemTopics.add(new WordItemTopic(R.drawable.sound_svgrepo_com, "term3", "definition3", "wordTerm3", "wordDefinition3"));

        list.add(new Category(listWordItemTopics));
        return list;
    }


    private void showDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_flashcards_layout);

        dialog.show();

    }
}
