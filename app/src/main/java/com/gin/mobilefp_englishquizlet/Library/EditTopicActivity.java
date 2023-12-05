package com.gin.mobilefp_englishquizlet.Library;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gin.mobilefp_englishquizlet.MainMenu.LibraryFragment;
import com.gin.mobilefp_englishquizlet.MainMenu.MainActivity;
import com.gin.mobilefp_englishquizlet.Models.Folder;
import com.gin.mobilefp_englishquizlet.Models.Topic;
import com.gin.mobilefp_englishquizlet.Models.Word;
import com.gin.mobilefp_englishquizlet.R;
import com.gin.mobilefp_englishquizlet.RecyclerViewAdapters.AdapterForWords;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditTopicActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView txtviewActivityTitle;
    EditText edtxtTopicTitle;
    EditText edtxtTopicDescription;
    AppCompatImageButton btnBack;
    AppCompatImageButton btnOption;
    AppCompatImageButton btnTopicComplete;
    FloatingActionButton btnAddWord;
    ArrayList<Word> words = new ArrayList<>();
    AdapterForWords adapter;
    AlertDialog wordDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_topic_layout);

        txtviewActivityTitle = findViewById(R.id.txtviewActivityTitle);
        edtxtTopicTitle = findViewById(R.id.edtxtTopicTitle);
        edtxtTopicDescription = findViewById(R.id.edtxtTopicDescription);
        btnTopicComplete = findViewById(R.id.btnTopicComplete);
        btnAddWord = findViewById(R.id.btnAddWord);
        btnOption = findViewById(R.id.btnOption);
        btnBack = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.recyclerView);

        txtviewActivityTitle.setText("Edit topic");

        Intent getInfo = getIntent();
        String topicID = getInfo.getStringExtra("topicID");

        adapter = new AdapterForWords(this, words, 2);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> {
            finish();
        });

        btnAddWord.setOnClickListener(v -> {
            showWordDialog();
        });

        btnOption.setOnClickListener(v -> {
            showOptionDialog(topicID);
        });

        btnTopicComplete.setOnClickListener(v -> {
            DatabaseReference topicRef = FirebaseDatabase.getInstance().getReference("topics").child(topicID);

            topicRef.child("title").setValue(edtxtTopicTitle.getText().toString());
            topicRef.child("description").setValue(edtxtTopicDescription.getText().toString());
            topicRef.child("words").setValue(words);
            Toast.makeText(this, "Edit topic success!", Toast.LENGTH_SHORT).show();
            finish();
        });

        setUpWordList(topicID);
        setUpTopicInfo(topicID);
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

                adapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
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

                edtxtTopicTitle = findViewById(R.id.edtxtTopicTitle);
                edtxtTopicDescription = findViewById(R.id.edtxtTopicDescription);
                assert topic != null;
                edtxtTopicTitle.setText(topic.getTitle());
                edtxtTopicDescription.setText(topic.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });
    }

    private void showWordDialog() {
        // Create a new AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_word, null);
        builder.setView(dialogView);

        // Find the EditText fields in the dialog layout
        EditText edtxtTerm = dialogView.findViewById(R.id.edtxtTerm);
        EditText edtxtDefinition = dialogView.findViewById(R.id.edtxtDefinition);
        EditText edtxtDescription = dialogView.findViewById(R.id.edtxtDescription);

        // Find the Confirm button in the dialog layout
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        // Set OnClickListener for the Confirm button
        btnConfirm.setOnClickListener(view -> {
            String id = Integer.toString(words.size());
            String term = edtxtTerm.getText().toString();
            String definition = edtxtDefinition.getText().toString();
            String description = edtxtDescription.getText().toString();

            boolean valid = true;
            if(term.equals("") || definition.equals("")) {
                valid = false;
                Toast.makeText(EditTopicActivity.this, "Term and definition can't be empty", Toast.LENGTH_SHORT).show();
            }

            if(valid) {
                Word newWord = new Word(id, term, definition, description);
                words.add(newWord);

                adapter.notifyDataSetChanged();

                // Dismiss the dialog
                wordDialog.dismiss();
            }
        });

        // Create and show the AlertDialog
        wordDialog = builder.create();
        wordDialog.show();
    }

    private void showOptionDialog(String topicID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options");

        String[] options = {"Import words", "Delete this topic"};

        builder.setItems(options, (dialog, which) -> {
            String selectedOption = options[which];
            if(selectedOption.equals("Import words")) {
                Toast.makeText(this, "Give us a csv file", Toast.LENGTH_SHORT).show();
            }
            if(selectedOption.equals("Delete this topic")) {
                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);
                confirmBuilder.setMessage("Are you sure you want to permanently delete this topic?")
                        .setPositiveButton("Delete", (dialog1, id) -> {
                            Toast.makeText(this, "Permanently deleted the topic!", Toast.LENGTH_SHORT).show();
                            DatabaseReference topicsRef = FirebaseDatabase.getInstance().getReference("topics");
                            topicsRef.child(topicID).removeValue((error, ref) -> {
                                finish();
                            });
                        })
                        .setNegativeButton("Cancel", (dialog12, id) -> {
                        });
                confirmBuilder.show();
            }
        });
        builder.show();
    }
}
