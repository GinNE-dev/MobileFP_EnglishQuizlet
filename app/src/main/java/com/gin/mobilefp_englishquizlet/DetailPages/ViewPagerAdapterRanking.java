package com.gin.mobilefp_englishquizlet.DetailPages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.gin.mobilefp_englishquizlet.DetailPages.RankTypoFragment;
import com.gin.mobilefp_englishquizlet.Fragment.FragmentMostFrequent;
import com.gin.mobilefp_englishquizlet.DetailPages.RankMultipleFragment;

public class ViewPagerAdapterRanking extends FragmentStatePagerAdapter {

    public ViewPagerAdapterRanking(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            default:
                return new RankMultipleFragment();
            case 1:
                return new RankTypoFragment();
        }
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
                title = "Multiple Choice";
                break;
            case 1:
                title = "Type Words";
                break;
        }
        return title;
    }
}
