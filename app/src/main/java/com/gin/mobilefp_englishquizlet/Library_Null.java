package com.gin.mobilefp_englishquizlet;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class Library_Null extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_null);

        tabLayout = findViewById(R.id.tab_layout_library);
        viewPager = findViewById(R.id.viewpager_library);

        ViewPagerAdapterLibrary viewPagerAdapterLibrary = new ViewPagerAdapterLibrary(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        viewPager.setAdapter(viewPagerAdapterLibrary);
        tabLayout.setupWithViewPager(viewPager);
    }
}
