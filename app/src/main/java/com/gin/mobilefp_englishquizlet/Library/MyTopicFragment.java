package com.gin.mobilefp_englishquizlet.Library;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gin.mobilefp_englishquizlet.Models.Topic;
import com.gin.mobilefp_englishquizlet.R;
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
import java.util.Collections;

public class MyTopicFragment extends Fragment {
    MaterialButton btnAddFirstTopic;
    FloatingActionButton btnAddTopic;
    LinearLayout linearLayoutEmpty;
    LinearLayout linearLayoutNotEmpty;
    RecyclerView recyclerView;
    AdapterForTopics adapter;
    SwipeRefreshLayout swipeLayout;
    ArrayList<Topic> topics = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_topic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnAddTopic = view.findViewById(R.id.btnAddTopic);
        btnAddFirstTopic = view.findViewById(R.id.btnAddFirstTopic);
        linearLayoutEmpty = view.findViewById(R.id.linearLayoutEmpty);
        linearLayoutNotEmpty = view.findViewById(R.id.linearLayoutNotEmpty);
        recyclerView = view.findViewById(R.id.recyclerView);
        swipeLayout = view.findViewById(R.id.swipeLayout);

        btnAddTopic.setVisibility(View.INVISIBLE);
        linearLayoutNotEmpty.setVisibility(View.INVISIBLE);

        btnAddTopic.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), CreateTopicActivity.class);
            startActivity(i);
        });

        btnAddFirstTopic.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), CreateTopicActivity.class);
            startActivity(i);
        });

        adapter = new AdapterForTopics(getActivity(), topics);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setUpTopicList();

        swipeLayout.setOnRefreshListener(() -> {
            setUpTopicList();
            new Handler().postDelayed(() -> swipeLayout.setRefreshing(false), 500);
        });
    }

    private void setUpTopicList() {
        DatabaseReference topicsRef = FirebaseDatabase.getInstance().getReference("topics");
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        topicsRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                topics.clear();

                for(DataSnapshot topicSnap: snapshot.getChildren()) {
                    Topic currentTopic = topicSnap.getValue(Topic.class);
                    if(currentTopic.getOwner().equals(userID)) {
                        topics.add(currentTopic);
                    }
                }

                Collections.reverse(topics);
                adapter.notifyDataSetChanged();

                if(topics.size() > 0) {
                    linearLayoutEmpty.setVisibility(View.GONE);
                    btnAddTopic.setVisibility(View.VISIBLE);
                    linearLayoutNotEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });
    }
}