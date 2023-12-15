package com.gin.mobilefp_englishquizlet.MainMenu;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gin.mobilefp_englishquizlet.AuthencationActivities.LoginActivity;
import com.gin.mobilefp_englishquizlet.Library.CreateTopicActivity;
import com.gin.mobilefp_englishquizlet.R;
import com.google.android.material.search.SearchBar;
import com.google.firebase.auth.FirebaseAuth;
public class HomeFragment extends Fragment {
    CardView btnMyTopic, btnDiscover, btnCreateTopic, btnStart;
    SearchBar btnSearch;
    ViewPager viewPager;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnMyTopic = view.findViewById(R.id.btnMyTopic);
        btnDiscover = view.findViewById(R.id.btnDiscover);
        btnCreateTopic = view.findViewById(R.id.btnCreateTopic);
        btnStart = view.findViewById(R.id.btnStart);
        btnSearch = view.findViewById(R.id.btnSearch);
        viewPager = getActivity().findViewById(R.id.viewPager);

        btnMyTopic.setOnClickListener(v -> {
            viewPager.setCurrentItem(1);
        });

        btnDiscover.setOnClickListener(v -> {
            viewPager.setCurrentItem(2);
        });

        btnStart.setOnClickListener(v -> {
            viewPager.setCurrentItem(2);
        });

        btnSearch.setOnClickListener(v -> {
            viewPager.setCurrentItem(2);
        });

        btnCreateTopic.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), CreateTopicActivity.class);
            viewPager.setCurrentItem(1);
            startActivity(i);
        });
    }
}