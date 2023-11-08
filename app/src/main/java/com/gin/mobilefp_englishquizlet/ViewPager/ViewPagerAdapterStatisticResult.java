package com.gin.mobilefp_englishquizlet.ViewPager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.gin.mobilefp_englishquizlet.Fragment.FragmentCorrectAnswer;
import com.gin.mobilefp_englishquizlet.Fragment.FragmentIncorrectAnswer;

public class ViewPagerAdapterStatisticResult extends FragmentStatePagerAdapter {

    public ViewPagerAdapterStatisticResult(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new FragmentIncorrectAnswer();
        }
        return new FragmentCorrectAnswer();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        
        switch (position){
            case 0:
                title = "Correct";
                break;
            case 1:
                title = "Incorrect";
                break;
        }
        return title;
    }
}
