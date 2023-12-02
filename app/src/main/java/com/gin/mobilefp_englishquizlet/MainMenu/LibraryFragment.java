package com.gin.mobilefp_englishquizlet.MainMenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gin.mobilefp_englishquizlet.Library.FragmentMyFolder;
import com.gin.mobilefp_englishquizlet.Library.FragmentMyTopic;
import com.gin.mobilefp_englishquizlet.R;
import com.google.android.material.tabs.TabLayout;

public class LibraryFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public LibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.library_null, container, false);

        tabLayout = rootView.findViewById(R.id.tab_layout_library);
        viewPager = rootView.findViewById(R.id.viewpager_library);

        ViewPagerAdapterLibrary viewPagerAdapterLibrary = new ViewPagerAdapterLibrary(
                getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        viewPager.setAdapter(viewPagerAdapterLibrary);
        tabLayout.setupWithViewPager(viewPager);

        return rootView;
    }

    public static class ViewPagerAdapterLibrary extends FragmentStatePagerAdapter {

        public ViewPagerAdapterLibrary(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == 1) {
                return new FragmentMyFolder();
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
}
