package com.gin.mobilefp_englishquizlet.Library;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gin.mobilefp_englishquizlet.Models.Topic;
import com.gin.mobilefp_englishquizlet.Models.Word;
import com.gin.mobilefp_englishquizlet.R;
import com.gin.mobilefp_englishquizlet.RecyclerViewAdapters.AdapterForWords;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CreateTopicActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText edtxtTopicTitle;
    EditText edtxtTopicDescription;
    AppCompatImageButton btnBack;
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

        edtxtTopicTitle = findViewById(R.id.edtxtTopicTitle);
        edtxtTopicDescription = findViewById(R.id.edtxtTopicDescription);
        btnTopicComplete = findViewById(R.id.btnTopicComplete);
        btnAddWord = findViewById(R.id.btnAddWord);
        btnBack = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.recyclerView);

        adapter = new AdapterForWords(this, words, 1);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> {
            finish();
        });

        btnAddWord.setOnClickListener(v -> {
            showWordDialog();
        });

        btnTopicComplete.setOnClickListener(v -> {
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference topicsRef = db.getReference("topics");

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            String userEmail = mAuth.getCurrentUser().getEmail();

            String topicID = topicsRef.push().getKey();
            String topicTitle = edtxtTopicTitle.getText().toString().trim();
            String topicDescription = edtxtTopicDescription.getText().toString();

            boolean valid = true;
            if(topicTitle.equals("")) {
                valid = false;
                Toast.makeText(this, "Please name your topic!", Toast.LENGTH_SHORT).show();
            }
            else if(topicDescription.equals("")) {
                valid = false;
                Toast.makeText(this, "Please describe something about the topic!", Toast.LENGTH_SHORT).show();
            }
            else if(words.size() < 4) {
                valid = false;
                Toast.makeText(this, "Topic need to contains at least 4 words!", Toast.LENGTH_SHORT).show();
            }

            if(valid) {
                for(int i = 0; i < words.size(); i++) {
                    words.get(i).setId(Integer.toString(i));
                }

                topicsRef.child(topicID).setValue(new Topic(topicID, topicTitle, topicDescription, userEmail, words));

                Toast.makeText(this, "Create topic success!", Toast.LENGTH_SHORT).show();
                finish();
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
            String id = "undefined";
            String term = edtxtTerm.getText().toString();
            String definition = edtxtDefinition.getText().toString();
            String description = edtxtDescription.getText().toString();

            boolean valid = true;
            if(term.equals("") || definition.equals("")) {
                valid = false;
                Toast.makeText(CreateTopicActivity.this, "Term and definition can't be empty", Toast.LENGTH_SHORT).show();
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
}
