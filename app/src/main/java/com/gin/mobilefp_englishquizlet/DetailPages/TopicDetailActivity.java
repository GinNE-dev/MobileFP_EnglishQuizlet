package com.gin.mobilefp_englishquizlet.DetailPages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gin.mobilefp_englishquizlet.Models.Topic;
import com.gin.mobilefp_englishquizlet.Models.Word;
import com.gin.mobilefp_englishquizlet.R;
import com.gin.mobilefp_englishquizlet.RecyclerViewAdapters.AdapterForWords;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TopicDetailActivity extends AppCompatActivity {
    ArrayList<Word> words = new ArrayList<>();
    AdapterForWords adapterWords;
    RecyclerView recyclerViewWords;
    TextView txtviewTermCount;
    TextView txtviewTitle;
    TextView txtviewDescription;
    AppCompatImageButton btnBack;
    AppCompatImageButton btnOption;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_detail_layout);

        recyclerViewWords = findViewById(R.id.recyclerViewWords);
        txtviewTermCount = findViewById(R.id.txtviewTermCount);
        txtviewTitle = findViewById(R.id.txtviewTitle);
        txtviewDescription = findViewById(R.id.txtviewDescription);
        btnBack = findViewById(R.id.btnBack);
        btnOption = findViewById(R.id.btnOption);

        Intent getInfo = getIntent();
        String topicID = getInfo.getStringExtra("id");
        String topicOwner = getInfo.getStringExtra("owner");

        adapterWords = new AdapterForWords(this, words, 0);
        recyclerViewWords.setAdapter(adapterWords);
        recyclerViewWords.setLayoutManager(new LinearLayoutManager(this));

        setUpWordList(topicID);
        setUpTopicInfo(topicID);

        btnBack.setOnClickListener(v -> {
            finish();
        });

        btnOption.setOnClickListener(v -> {
            showOptionDialog(topicOwner);
        });
    }

    private void setUpWordList(String topicID) {
        DatabaseReference wordsOfTopicRef = FirebaseDatabase.getInstance().getReference("topics").child(topicID).child("words");
        wordsOfTopicRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                words.clear(); // Clear the existing list before adding new data

                for (DataSnapshot wordSnap : snapshot.getChildren()) {
                    Word currentWord = wordSnap.getValue(Word.class);
                    words.add(currentWord);
                }

                adapterWords.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });
    }

    private void setUpTopicInfo(String topicID) {
        DatabaseReference topicRef = FirebaseDatabase.getInstance().getReference("topics").child(topicID);
        topicRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Topic topic = snapshot.getValue(Topic.class);

                txtviewTitle = findViewById(R.id.txtviewTitle);
                txtviewTermCount = findViewById(R.id.txtviewTermCount);
                txtviewDescription = findViewById(R.id.txtviewDescription);
                assert topic != null;
                txtviewTitle.setText(topic.getTitle());
                txtviewDescription.setText("Description: " + topic.getDescription());
                txtviewTermCount.setText(words.size() + " words");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });
    }

    private void showOptionDialog(String topicOwner) {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.option_topic_detail_layout, null);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);

        // Find and set up your buttons or other views inside the bottom sheet
        Button btnAddToFolder = bottomSheetView.findViewById(R.id.btnAddToFolder);
        Button btnEditTopic = bottomSheetView.findViewById(R.id.btnEditTopic);

        btnAddToFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click for adding to folder
                bottomSheetDialog.dismiss(); // Dismiss the bottom sheet if needed
            }
        });

        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if(topicOwner.equals(userEmail)) {
            btnEditTopic.setText("Edit this topic");
            btnEditTopic.setOnClickListener(v -> {
                // Handle button click for editing topic
                bottomSheetDialog.dismiss(); // Dismiss the bottom sheet if needed
            });
        }
        else {
            btnEditTopic.setText("View topic info");
            btnEditTopic.setOnClickListener(v -> {
                // Handle button click for editing topic
                bottomSheetDialog.dismiss(); // Dismiss the bottom sheet if needed
            });
        }

        bottomSheetDialog.show();
    }
}
