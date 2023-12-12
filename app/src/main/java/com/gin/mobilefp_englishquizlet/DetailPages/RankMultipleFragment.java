package com.gin.mobilefp_englishquizlet.DetailPages;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gin.mobilefp_englishquizlet.Models.Record;
import com.gin.mobilefp_englishquizlet.Models.Topic;
import com.gin.mobilefp_englishquizlet.R;
import com.gin.mobilefp_englishquizlet.RecyclerViewAdapters.AdapterForRecords;
import com.gin.mobilefp_englishquizlet.RecyclerViewAdapters.AdapterForTopics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class RankMultipleFragment extends Fragment {
    ArrayList<Record> records = new ArrayList<>();
    AdapterForRecords adapter;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rank_multiple, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String topicID = "";

        if (getArguments() != null) {
            topicID = getArguments().getString("topicid");
        }

        recyclerView = view.findViewById(R.id.recyclerView);

        adapter = new AdapterForRecords(getActivity(), records);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setUpRecordList(topicID);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setUpRecordList(String topicID) {
        //records.add(new Record("Me", 100, "Multiple Choice", 20000));
        DatabaseReference recordsRef = FirebaseDatabase.getInstance().getReference("topics").child(topicID).child("scoreRecords");
        recordsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                records.clear();

                for(DataSnapshot recordSnap: snapshot.getChildren()) {
                    Record currentRecord = recordSnap.getValue(Record.class);
                    if(currentRecord.getLearnMode().equals(Record.LearnMode.MultipleChoice)) {
                        records.add(currentRecord);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}