package com.gin.mobilefp_englishquizlet.DetailPages;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import java.util.HashMap;

public class RankTypoFragment extends Fragment {
    ArrayList<Record> records = new ArrayList<>();
    AdapterForRecords adapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rank_multiple, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String topicID = getArguments().getString("topicid");

        recyclerView = view.findViewById(R.id.recyclerView);
        swipeLayout = view.findViewById(R.id.swipeLayout);

        adapter = new AdapterForRecords(getActivity(), records);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeLayout.setOnRefreshListener(() -> {
            setUpRecordList(topicID);
            new Handler().postDelayed(() -> swipeLayout.setRefreshing(false), 500);
        });

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
                ArrayList<Record> allRecords = new ArrayList<>();

                for(DataSnapshot recordSnap: snapshot.getChildren()) {
                    Record currentRecord = recordSnap.getValue(Record.class);
                    if(currentRecord.getLearnMode().equals(Record.LearnMode.Typo)) {
                        allRecords.add(currentRecord);
                    }
                }

                allRecords.sort(new Record.RecordComparator());

                HashMap<String, Boolean> userAppeared = new HashMap<>();
                for (Record record: allRecords) {
                    if(!userAppeared.containsKey(record.getArchivedBy())) {
                        userAppeared.put(record.getArchivedBy(), true);
                        records.add(record);
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