package com.gin.mobilefp_englishquizlet.DetailPages;

import android.annotation.SuppressLint;
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

import com.gin.mobilefp_englishquizlet.Models.Folder;
import com.gin.mobilefp_englishquizlet.Models.Topic;
import com.gin.mobilefp_englishquizlet.R;
import com.gin.mobilefp_englishquizlet.RecyclerViewAdapters.AdapterForTopics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FolderDetailActivity extends AppCompatActivity {
    ArrayList<Topic> topics = new ArrayList<>();
    AdapterForTopics adapter;
    RecyclerView recyclerView;
    TextView txtviewQuantity;
    TextView txtviewTitle;
    AppCompatImageButton btnBack;
    AppCompatImageButton btnOption;
    AlertDialog folderDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folder_detail_layout);

        recyclerView = findViewById(R.id.recyclerView);
        txtviewTitle = findViewById(R.id.txtviewTitle);
        txtviewQuantity = findViewById(R.id.txtviewQuantity);
        btnBack = findViewById(R.id.btnBack);
        btnOption = findViewById(R.id.btnOption);

        Intent getInfo = getIntent();
        String folderID = getInfo.getStringExtra("id");
        String folderTitle = getInfo.getStringExtra("title");

        txtviewTitle.setText(folderTitle);

        adapter = new AdapterForTopics(this, topics, folderID);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setUpTopicList(folderID);

        btnBack.setOnClickListener(v -> {
            finish();
        });

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(folderID.equals(userID)) {
            btnOption.setVisibility(View.GONE);
        }
        else {
            btnOption.setOnClickListener(v -> {
                showOptionDialog(folderID);
            });
        }
    }

    private void setUpTopicList(String folderID) {
        DatabaseReference topicsRef = FirebaseDatabase.getInstance().getReference("topics");
        topicsRef.orderByChild("belongsToFolders/" + folderID).equalTo(true).addValueEventListener(new ValueEventListener() {
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

    private void showOptionDialog(String folderID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options");

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference foldersRef = FirebaseDatabase.getInstance().getReference("users").child(userID).child("folders");

        String[] options = {"Rename folder", "Delete this folder"};

        builder.setItems(options, (dialog, which) -> {
            String selectedOption = options[which];
            if(selectedOption.equals("Rename folder")) {
                Toast.makeText(this, "You want to rename the folder", Toast.LENGTH_SHORT).show();
                showFolderDialog(folderID);
            }
            if(selectedOption.equals("Delete this folder")) {
                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);
                confirmBuilder.setMessage("Permanently delete this folder (its topics will not be affected)?")
                        .setPositiveButton("Delete", (dialog1, id) -> {
                            foldersRef.child(folderID).removeValue((error, ref) -> {
                                Toast.makeText(FolderDetailActivity.this, "Permanently deleted the folder!", Toast.LENGTH_SHORT).show();
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

    private void showFolderDialog(String folderID) {
        // Create a new AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_folder, null);
        builder.setView(dialogView);

        TextView txtviewHeader = dialogView.findViewById(R.id.txtviewHeader);
        EditText edtxtTitle = dialogView.findViewById(R.id.edtxtTitle);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        txtviewHeader.setText("Change folder's name");
        edtxtTitle.setText(txtviewTitle.getText().toString());

        // Set OnClickListener for the Confirm button
        btnConfirm.setOnClickListener(view -> {
            String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference folderRef = FirebaseDatabase.getInstance().getReference("users").child(userUID).child("folders").child(folderID);

            String title = edtxtTitle.getText().toString();

            boolean valid = true;
            if(title.equals("")) {
                valid = false;
                Toast.makeText(this, "Folder name can't be empty", Toast.LENGTH_SHORT).show();
            }
            if(valid) {
                folderRef.child("title").setValue(edtxtTitle.getText().toString());
                Toast.makeText(this, "Changed folder's name!", Toast.LENGTH_SHORT).show();
                folderDialog.dismiss();
                txtviewTitle.setText(edtxtTitle.getText().toString());
            }
        });
        // Create and show the AlertDialog
        folderDialog = builder.create();
        folderDialog.show();
    }
}
