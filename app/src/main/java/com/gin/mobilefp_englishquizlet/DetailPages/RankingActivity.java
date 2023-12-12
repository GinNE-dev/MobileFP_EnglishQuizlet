package com.gin.mobilefp_englishquizlet.DetailPages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gin.mobilefp_englishquizlet.R;
import com.google.android.material.tabs.TabLayout;

public class RankingActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    AppCompatImageButton btnBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking_layout);

        tabLayout = findViewById(R.id.tab_layout_ranking);
        viewPager = findViewById(R.id.viewpager_ranking);
        btnBack = findViewById(R.id.btnBack);

        ViewPagerAdapterRanking viewPagerAdapterRanking = new ViewPagerAdapterRanking(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        viewPager.setAdapter(viewPagerAdapterRanking);
        tabLayout.setupWithViewPager(viewPager);

        Intent getInfo = getIntent();
        String topicID = getInfo.getStringExtra("topicid");

        Bundle bundle = new Bundle();
        bundle.putString("topicid", topicID);

        // Assuming you have a fragment at position 0 in your view pager
        RankMultipleFragment rankMultipleFragment = (RankMultipleFragment) viewPagerAdapterRanking.instantiateItem(viewPager, 0);
        rankMultipleFragment.setArguments(bundle);

        RankTypoFragment rankTypoFragment = (RankTypoFragment) viewPagerAdapterRanking.instantiateItem(viewPager, 1);
        rankTypoFragment.setArguments(bundle);

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }
}
