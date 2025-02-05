package com.gin.mobilefp_englishquizlet.DetailPages;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gin.mobilefp_englishquizlet.GlobalUtil;
import com.gin.mobilefp_englishquizlet.Library.EditTopicActivity;
import com.gin.mobilefp_englishquizlet.Models.Folder;
import com.gin.mobilefp_englishquizlet.Models.Topic;
import com.gin.mobilefp_englishquizlet.Models.User;
import com.gin.mobilefp_englishquizlet.Models.Word;
import com.gin.mobilefp_englishquizlet.R;
import com.gin.mobilefp_englishquizlet.RecyclerViewAdapters.AdapterForViewWords;
import com.gin.mobilefp_englishquizlet.StudyMode.FlashCard.FlashcardModeLayout;
import com.gin.mobilefp_englishquizlet.StudyMode.MultipleChoiceModeLayout;
import com.gin.mobilefp_englishquizlet.StudyMode.TypoModeLayout;
import com.gin.mobilefp_englishquizlet.Utils.Permission;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopicDetailActivity extends AppCompatActivity {
    ArrayList<Word> words = new ArrayList<>();
    ArrayList<Folder> folders = new ArrayList<>();
    AdapterForViewWords adapterWords;
    RecyclerView recyclerViewWords;
    TextView txtviewTermCount;
    TextView txtviewTitle;
    TextView txtviewDescription;
    TextView txtviewOwner;
    CircleImageView imgAvatar;
    AppCompatImageButton btnBack;
    AppCompatImageButton btnOption;
    AppCompatImageButton btnRank;
    private View flashCardLearn;
    private View multipleChoiceLearn;
    private View typeWordLearn;

    private String mTopicID;
    private ActivityResultLauncher<String> mAsker;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_detail_layout);

        recyclerViewWords = findViewById(R.id.recyclerViewWords);
        txtviewTermCount = findViewById(R.id.txtviewTermCount);
        txtviewTitle = findViewById(R.id.txtviewTitle);
        txtviewDescription = findViewById(R.id.txtviewDescription);
        txtviewOwner = findViewById(R.id.txtviewOwner);
        imgAvatar = findViewById(R.id.imgAvatar);
        btnBack = findViewById(R.id.btnBack);
        btnOption = findViewById(R.id.btnOption);
        btnRank = findViewById(R.id.btnRank);
        flashCardLearn = findViewById(R.id.panel_flashcard_learning_mode);
        multipleChoiceLearn = findViewById(R.id.panel_multiple_choice_learning_mode);
        typeWordLearn = findViewById(R.id.panel_type_word_learning_mode);

        Intent getInfo = getIntent();
        String topicID = getInfo.getStringExtra("id");
        String topicOwner = getInfo.getStringExtra("owner");
        mTopicID = topicID;

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        adapterWords = new AdapterForViewWords(this, words);
        adapterWords.setTopicId(topicID);
        recyclerViewWords.setAdapter(adapterWords);
        recyclerViewWords.setLayoutManager(new LinearLayoutManager(this));

        setUpWordList(topicID);
        setUpTopicInfo(topicID);
        //setUpUserInfo(ownerEmail);
        setUpFolderList();

        btnBack.setOnClickListener(v -> {
            finish();
        });

        btnOption.setOnClickListener(v -> {
            showOptionDialog(topicID, topicOwner);
        });

        btnRank.setOnClickListener(v -> {
            Intent ranking = new Intent(this, RankingActivity.class);
            ranking.putExtra("topicid", topicID);
            startActivity(ranking);
        });

        registerEvents();
        mAsker = Permission.createPermissionAsker(this, ()->{
            exportWords();
        });
    }
    private void registerEvents(){
        flashCardLearn.setOnClickListener(v->{
            Intent flashCardLearnIntent = new Intent(TopicDetailActivity.this, FlashcardModeLayout.class);
            flashCardLearnIntent.putExtra("topic_id", mTopicID);
            startActivity(flashCardLearnIntent);
        });

        multipleChoiceLearn.setOnClickListener(v->{
            Intent multipleChoiceLearnIntent = new Intent(TopicDetailActivity.this, MultipleChoiceModeLayout.class);
            multipleChoiceLearnIntent.putExtra("topic_id", mTopicID);
            startActivity(multipleChoiceLearnIntent);
        });

        typeWordLearn.setOnClickListener(v->{
            Intent typeWordLearnIntent = new Intent(TopicDetailActivity.this, TypoModeLayout.class);
            typeWordLearnIntent.putExtra("topic_id", mTopicID);
            startActivity(typeWordLearnIntent);
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
            }
        });
    }

    private void setUpFolderList() {
        String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference foldersRef = FirebaseDatabase.getInstance().getReference("users").child(userUID).child("folders");
        foldersRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                folders.clear();

                for(DataSnapshot folderSnap: snapshot.getChildren()) {
                    Folder currentFolder = folderSnap.getValue(Folder.class);
                    folders.add(currentFolder);
                }
                Collections.reverse(folders);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });
    }

    private void setUpTopicInfo(String topicID) {
        DatabaseReference topicRef = FirebaseDatabase.getInstance().getReference("topics").child(topicID);
        topicRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Topic topic = snapshot.getValue(Topic.class);

                txtviewTitle = findViewById(R.id.txtviewTitle);
                txtviewTermCount = findViewById(R.id.txtviewTermCount);
                txtviewDescription = findViewById(R.id.txtviewDescription);
                txtviewOwner = findViewById(R.id.txtviewOwner);
                imgAvatar = findViewById(R.id.imgAvatar);

                assert topic != null;
                txtviewTitle.setText(topic.getTitle());
                txtviewDescription.setText("Description: " + topic.getDescription());
                txtviewTermCount.setText(words.size() + " words");

                String ownerID = topic.getOwner();

                DatabaseReference ownerRef = FirebaseDatabase.getInstance().getReference("users").child(ownerID);
                ownerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String userName = snapshot.child("name").getValue().toString();
                        String userAva = snapshot.child("avaURL").getValue().toString();

                        txtviewOwner.setText(userName);
                        int imageSize = 100; // Size in dp
                        int imageSizePixels = (int) (imageSize * getResources().getDisplayMetrics().density);
                        Glide.with(TopicDetailActivity.this)
                                .load(userAva)
                                .apply(new RequestOptions()
                                        .override(imageSizePixels, imageSizePixels)
                                        .centerCrop())
                                .into(imgAvatar);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error if needed
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showOptionDialog(String topicID, String topicOwner) {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.option_topic_detail_layout, null);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);

        // Find and set up your buttons or other views inside the bottom sheet
        MaterialButton btnAddToFolder = bottomSheetView.findViewById(R.id.btnAddToFolder);
        MaterialButton btnExport = bottomSheetDialog.findViewById(R.id.btnExport);
        MaterialButton btnEditTopic = bottomSheetView.findViewById(R.id.btnEditTopic);

        btnAddToFolder.setOnClickListener(v -> {
            showFolderSelectionDialog(topicID);
            bottomSheetDialog.dismiss();
        });

        btnExport.setOnClickListener(v -> {
            mAsker.launch(Permission.WRITE_EXTERNAL_STORAGE);
            bottomSheetDialog.dismiss();
        });

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(topicOwner.equals(userID)) {
            btnEditTopic.setText("Edit this topic");
            btnEditTopic.setOnClickListener(v -> {
                bottomSheetDialog.dismiss(); // Dismiss the bottom sheet if needed
                Intent goToEdit = new Intent(this, EditTopicActivity.class);
                goToEdit.putExtra("topicID", topicID);
                startActivity(goToEdit);
                finish();
            });
        }
        else {
            btnEditTopic.setVisibility(View.GONE);
        }

        bottomSheetDialog.show();
    }

    private void showFolderSelectionDialog(String topicID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Folder");

        ArrayList<String> foldersNames = new ArrayList<>();
        for (Folder folder : folders) {
            foldersNames.add(folder.getTitle());
        }

        String[] folderArray = foldersNames.toArray(new String[0]);

        builder.setItems(folderArray, (dialog, which) -> {
            // Handle the folder selection here
            Folder selectedFolder = folders.get(which);

            DatabaseReference topicBelongsToFoldersRef = FirebaseDatabase.getInstance().getReference("topics").child(topicID).child("belongsToFolders");
            topicBelongsToFoldersRef.child(selectedFolder.getId()).setValue(true);

            Toast.makeText(this, "This topic has been added to your folder", Toast.LENGTH_SHORT).show();
        });

        builder.show();
    }

    private void exportWords() {
        StringBuilder fileContent = new StringBuilder();
        for (Word word: words) {
            fileContent
                    .append(word.getTerm())
                    .append(",")
                    .append(word.getDefinition())
                    .append(",")
                    .append(word.getDescription())
                    .append("\n");
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String fileName = "exported_topic_" + dateFormat.format(date) + ".csv";
        Log.i("FILE NAME", fileName);

        GlobalUtil.writeFileToDownloadDirectory(fileContent.toString(), fileName);

        Toast.makeText(this, "Exported file to sdcard/Download/", Toast.LENGTH_SHORT).show();
    }
}
