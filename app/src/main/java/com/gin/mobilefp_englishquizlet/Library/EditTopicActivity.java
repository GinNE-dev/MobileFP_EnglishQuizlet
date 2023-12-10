package com.gin.mobilefp_englishquizlet.Library;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gin.mobilefp_englishquizlet.MainMenu.MainActivity;
import com.gin.mobilefp_englishquizlet.Models.Topic;
import com.gin.mobilefp_englishquizlet.Models.Word;
import com.gin.mobilefp_englishquizlet.R;
import com.gin.mobilefp_englishquizlet.RecyclerViewAdapters.AdapterForEditableWords;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditTopicActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView txtviewActivityTitle;
    EditText edtxtTopicTitle;
    EditText edtxtTopicDescription;
    RadioGroup radioPrivacy;
    AppCompatImageButton btnBack;
    AppCompatImageButton btnOption;
    AppCompatImageButton btnTopicComplete;
    FloatingActionButton btnAddWord;
    ArrayList<Word> words = new ArrayList<>();
    AdapterForEditableWords adapter;
    AlertDialog wordDialog;
    AlertDialog csvDialog;
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
        radioPrivacy = findViewById(R.id.radioPrivacy);
        recyclerView = findViewById(R.id.recyclerView);

        txtviewActivityTitle.setText("Edit topic");

        Intent getInfo = getIntent();
        String topicID = getInfo.getStringExtra("topicID");

        adapter = new AdapterForEditableWords(this, words);
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

            boolean isPrivate;
            int checkedRadioButtonID = radioPrivacy.getCheckedRadioButtonId();
            if (checkedRadioButtonID == R.id.btn_justMe) {
                isPrivate = true;
            }
            else {
                isPrivate = false;
            }

            String newTitle = edtxtTopicTitle.getText().toString();
            String newDescription = edtxtTopicDescription.getText().toString();

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String lastUpdatedDate = dateFormat.format(date);

            boolean valid = true;
            if(newTitle.equals("")) {
                valid = false;
                Toast.makeText(this, "Please name your topic!", Toast.LENGTH_SHORT).show();
            }
            else if(newDescription.equals("")) {
                valid = false;
                Toast.makeText(this, "Please describe something about the topic!", Toast.LENGTH_SHORT).show();
            }
            else if(words.size() < 4) {
                valid = false;
                Toast.makeText(this, "Topic need to contains at least 4 words!", Toast.LENGTH_SHORT).show();
            }

            if(valid) {
                topicRef.child("title").setValue(edtxtTopicTitle.getText().toString());
                topicRef.child("description").setValue(edtxtTopicDescription.getText().toString());
                topicRef.child("words").setValue(words);
                topicRef.child("private").setValue(isPrivate);
                topicRef.child("lastUpdatedDate").setValue(lastUpdatedDate);

                Toast.makeText(EditTopicActivity.this, "Edit topic success!", Toast.LENGTH_SHORT).show();
                finish();
            }
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
        topicRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Topic topic = snapshot.getValue(Topic.class);

                edtxtTopicTitle = findViewById(R.id.edtxtTopicTitle);
                edtxtTopicDescription = findViewById(R.id.edtxtTopicDescription);
                radioPrivacy = findViewById(R.id.radioPrivacy);

                assert topic != null;
                edtxtTopicTitle.setText(topic.getTitle());
                edtxtTopicDescription.setText(topic.getDescription());
                if(!topic.getPrivate()) {
                    radioPrivacy.check(R.id.btn_everyone);
                }
                if(topic.getPrivate()) {
                    radioPrivacy.check(R.id.btn_justMe);
                }
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
            String term = edtxtTerm.getText().toString();
            String definition = edtxtDefinition.getText().toString();
            String description = edtxtDescription.getText().toString();

            boolean valid = true;
            if(term.equals("") || definition.equals("")) {
                valid = false;
                Toast.makeText(EditTopicActivity.this, "Term and definition can't be empty", Toast.LENGTH_SHORT).show();
            }
            for (Word curWord: words) {
                if(curWord.getTerm().equals(term)) {
                    valid = false;
                    Toast.makeText(EditTopicActivity.this, "This term is already existed!", Toast.LENGTH_SHORT).show();
                    break;
                }
            }

            if(valid) {
                Word newWord = new Word(term, definition, description);
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
                showCSVPickerDialog();
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

    private void showCSVPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_import, null);
        builder.setView(dialogView);

        // Find the Confirm button in the dialog layout
        Button btnChoose = dialogView.findViewById(R.id.btnChoose);

        // Set OnClickListener for the Confirm button
        btnChoose.setOnClickListener(view -> {
            chooseCSVFile();
            csvDialog.dismiss();
        });

        // Create and show the AlertDialog
        csvDialog = builder.create();
        csvDialog.show();
    }

    private void chooseCSVFile() {
        ActivityCompat.requestPermissions(this,
                new String[]{WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if(Environment.isExternalStorageManager()){
                //choosing csv file
                Intent intent=new Intent();
                intent.setType("text/*");
                intent.putExtra(Intent.EXTRA_AUTO_LAUNCH_SINGLE_CHOICE,true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select CSV File "),666);
            }
            else{
                //getting permission from user
                Intent intent=new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                Uri uri=Uri.fromParts("package",getPackageName(),null);
                startActivity(intent);
            }
        }
        else{
            // for below android 11

            Intent intent=new Intent();
            intent.setType("text/*");
            intent.putExtra(Intent.EXTRA_AUTO_LAUNCH_SINGLE_CHOICE,true);
            intent.setAction(Intent.ACTION_GET_CONTENT);

            ActivityCompat.requestPermissions(this,new String[] {WRITE_EXTERNAL_STORAGE},102);
            startActivityForResult(Intent.createChooser(intent,"Select CSV File "),666);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addWordsFromCSV(Uri uri){
        try {
            InputStream in = getContentResolver().openInputStream(uri);
            BufferedReader r = new BufferedReader(new InputStreamReader(in));

            int wordsAdded = 0;
            for (String line; (line = r.readLine()) != null; ) {
                String[] info = line.trim().replace("\"", "").split(",");
                boolean valid = true;
                if(info.length != 3) {
                    valid = false;
                }
                for (Word word: words) {
                    if(info[0].trim().replace("\"", "").equals(word.getTerm())) {
                        valid = false;
                        break;
                    }
                }

                if(valid) {
                    String term = info[0].trim().replace("\"", "");
                    String definition = info[1].trim().replace("\"", "");
                    String description = info[2].trim().replace("\"", "");

                    words.add(new Word(term, definition, description));
                    wordsAdded++;
                    adapter.notifyDataSetChanged();
                }
            }
            Toast.makeText(this, "Added: " + wordsAdded + " words", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ignored) {

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 666 && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            Log.i("URI", uri.toString());
            addWordsFromCSV(uri);
        }
    }
}
