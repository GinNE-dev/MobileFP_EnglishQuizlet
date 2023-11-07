package com.gin.mobilefp_englishquizlet;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class Ranking_Layout extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking_layout);

        tabLayout = findViewById(R.id.tab_layout_ranking);
        viewPager = findViewById(R.id.viewpager_ranking);

        ViewPagerAdapterRanking viewPagerAdapterRanking = new ViewPagerAdapterRanking(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        viewPager.setAdapter(viewPagerAdapterRanking);
        tabLayout.setupWithViewPager(viewPager);
    }
}
