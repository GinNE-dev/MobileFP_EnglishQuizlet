package com.gin.mobilefp_englishquizlet.ViewPager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.gin.mobilefp_englishquizlet.Fragment.FragmentFolder;
import com.gin.mobilefp_englishquizlet.Fragment.FragmentMyTopic;

public class ViewPagerAdapterLibrary extends FragmentStatePagerAdapter {

    public ViewPagerAdapterLibrary(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new FragmentFolder();
        }
        return new FragmentMyTopic();
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
                title = "My Topic";
                break;
            case 1:
                title = "My Folder";
                break;
        }
        return title;
    }
}
