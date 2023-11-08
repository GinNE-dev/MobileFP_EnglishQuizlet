package com.gin.mobilefp_englishquizlet.ViewPager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.gin.mobilefp_englishquizlet.Fragment.FragmentFastestCompletion;
import com.gin.mobilefp_englishquizlet.Fragment.FragmentMostFrequent;
import com.gin.mobilefp_englishquizlet.Fragment.FragmentTopScorer;

public class ViewPagerAdapterRanking extends FragmentStatePagerAdapter {

    public ViewPagerAdapterRanking(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FragmentTopScorer();
            case 1:
                return new FragmentFastestCompletion();
            case 2:
                return new FragmentMostFrequent();

            default:
                return new FragmentTopScorer();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        
        switch (position){
            case 0:
                title = "Top Scorer";
                break;
            case 1:
                title = "Fastest";
                break;
            case 2:
                title = "Frequently";
                break;
        }
        return title;
    }
}
