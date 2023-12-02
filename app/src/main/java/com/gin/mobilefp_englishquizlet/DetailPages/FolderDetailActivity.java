package com.gin.mobilefp_englishquizlet.DetailPages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import com.gin.mobilefp_englishquizlet.RecyclerViewAdapters.AdapterForTopics;
import com.gin.mobilefp_englishquizlet.RecyclerViewAdapters.AdapterForWords;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FolderDetailActivity extends AppCompatActivity {
    ArrayList<Topic> topics = new ArrayList<>();
    AdapterForTopics adapter;
    RecyclerView recyclerView;
    TextView txtviewQuantity;
    TextView txtviewTitle;
    AppCompatImageButton btnBack;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folder_detail_layout);

        recyclerView = findViewById(R.id.recyclerView);
        txtviewTitle = findViewById(R.id.txtviewTitle);
        txtviewQuantity = findViewById(R.id.txtviewQuantity);
        btnBack = findViewById(R.id.btnBack);

        Intent getInfo = getIntent();
        String folderID = getInfo.getStringExtra("id");
        String folderTitle = getInfo.getStringExtra("title");

        txtviewTitle.setText(folderTitle);

        adapter = new AdapterForTopics(this, topics);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setUpTopicList(folderID);

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void setUpTopicList(String folderID) {
        DatabaseReference topicsRef = FirebaseDatabase.getInstance().getReference("topics");
        topicsRef.orderByChild("belongsToFolders/" + folderID).equalTo("true").addValueEventListener(new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                topics.clear(); // Clear the existing list before adding new data

                for (DataSnapshot topicSnap : snapshot.getChildren()) {
                    Topic currentTopic = topicSnap.getValue(Topic.class);
                    topics.add(currentTopic);
                }

                txtviewQuantity = findViewById(R.id.txtviewQuantity);
                txtviewQuantity.setText("" + topics.size());

                adapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });
    }
}
