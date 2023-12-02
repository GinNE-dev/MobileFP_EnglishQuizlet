package com.gin.mobilefp_englishquizlet.Library;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gin.mobilefp_englishquizlet.Models.Folder;
import com.gin.mobilefp_englishquizlet.Models.Topic;
import com.gin.mobilefp_englishquizlet.R;
import com.gin.mobilefp_englishquizlet.RecyclerViewAdapters.AdapterForFolders;
import com.gin.mobilefp_englishquizlet.RecyclerViewAdapters.AdapterForTopics;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyFolderFragment extends Fragment {
    FloatingActionButton btnAddFolder;
    RecyclerView recyclerView;
    AdapterForFolders adapter;
    ArrayList<Folder> folders = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_folder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnAddFolder = view.findViewById(R.id.btnAddTopic);
        recyclerView = view.findViewById(R.id.recyclerView);

//        btnAddFolder.setOnClickListener(v -> {
//            Intent i = new Intent(getActivity(), CreateTopicActivity.class);
//            startActivity(i);
//        });

        adapter = new AdapterForFolders(getActivity(), folders);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setUpFolderList();
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

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });
    }
}