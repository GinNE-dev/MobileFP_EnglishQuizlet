package com.gin.mobilefp_englishquizlet;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class Statistic_Result_Layout extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistic_result_layout);

        tabLayout = findViewById(R.id.tab_layout_statistic_result);
        viewPager = findViewById(R.id.viewpager_statistic_result);

        ViewPagerAdapterStatisticResult viewPagerAdapterStatisticResult = new ViewPagerAdapterStatisticResult(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        viewPager.setAdapter(viewPagerAdapterStatisticResult);
        tabLayout.setupWithViewPager(viewPager);
    }
}
