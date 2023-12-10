package com.gin.mobilefp_englishquizlet.MainMenu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gin.mobilefp_englishquizlet.Models.Topic;
import com.gin.mobilefp_englishquizlet.R;
import com.gin.mobilefp_englishquizlet.RecyclerViewAdapters.AdapterForTopics;
import com.google.android.material.search.SearchBar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class DiscoverFragment extends Fragment {
    RecyclerView recyclerView;
    AdapterForTopics adapter;
    SwipeRefreshLayout swipeLayout;
    SearchView searchBar;
    ArrayList<Topic> topics = new ArrayList<>();
    public DiscoverFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.discover_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        swipeLayout = view.findViewById(R.id.swipeLayout);
        searchBar = view.findViewById(R.id.searchBar);

        adapter = new AdapterForTopics(getActivity(), topics);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setUpTopicList();

        swipeLayout.setOnRefreshListener(() -> {
            setUpTopicList();
            new Handler().postDelayed(() -> swipeLayout.setRefreshing(false), 500);
        });

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")) {
                    adapter.updateList(topics);
                }
                else {
                    searching(newText);
                }
                return true;
            }
        });
    }

    private void setUpTopicList() {
        DatabaseReference topicRef = FirebaseDatabase.getInstance().getReference("topics");
        topicRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                topics.clear();

                for(DataSnapshot topicSnap: snapshot.getChildren()) {
                    Topic currentTopic = topicSnap.getValue(Topic.class);
                    if(!currentTopic.getPrivate()) {
                        topics.add(currentTopic);
                    }
                }

                Collections.reverse(topics);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searching(String key) {
        ArrayList<Topic> searchingList = new ArrayList<>();

        key = key.toLowerCase();
        for (Topic topic: topics) {
            if(topic.getTitle().toLowerCase().contains(key) || topic.getDescription().toLowerCase().contains(key)) {
                searchingList.add(topic);
            }
        }
        adapter.updateList(searchingList);
    }
}