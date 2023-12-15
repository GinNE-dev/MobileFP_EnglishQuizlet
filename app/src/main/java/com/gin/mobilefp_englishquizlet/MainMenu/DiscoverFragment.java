package com.gin.mobilefp_englishquizlet.MainMenu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageButton;
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
    TextView txtviewSort;
    AppCompatImageButton btnSort;
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

        txtviewSort = view.findViewById(R.id.txtviewSort);
        btnSort = view.findViewById(R.id.btnSort);
        recyclerView = view.findViewById(R.id.recyclerView);
        swipeLayout = view.findViewById(R.id.swipeLayout);
        searchBar = view.findViewById(R.id.searchBar);

        adapter = new AdapterForTopics(getActivity(), topics);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setUpTopicList("trending");
        txtviewSort.setText("Trending Topics");

        btnSort.setOnClickListener(v -> {
            showSortDialog();
        });

        swipeLayout.setOnRefreshListener(() -> {
            setUpTopicList("trending");
            txtviewSort.setText("Trending Topics");
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

    private void setUpTopicList(String sortMode) {
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

                if(sortMode.equals("trending")) {
                    topics.sort(new Topic.ScoreRecordsComparator());
                }
                if(sortMode.equals("newest")) {
                    topics.sort(new Topic.LastUpdatedDateComparator());
                }

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

    private void showSortDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sort criteria");

        String[] options = {"Trending Topics", "Newest Topics"};

        builder.setItems(options, (dialog, which) -> {
            String selectedOption = options[which];
            if(selectedOption.equals("Trending Topics")) {
                setUpTopicList("trending");
                txtviewSort.setText("Trending Topics");
            }
            if(selectedOption.equals("Newest Topics")) {
                setUpTopicList("newest");
                txtviewSort.setText("Newest Topics");
            }
        });
        builder.show();
    }
}