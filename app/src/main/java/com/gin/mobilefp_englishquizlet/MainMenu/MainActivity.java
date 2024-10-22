package com.gin.mobilefp_englishquizlet.MainMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.gin.mobilefp_englishquizlet.R;
import com.gin.mobilefp_englishquizlet.TextToSpeechHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextToSpeechHelper.initialize(this, new TextToSpeechHelper.OnInitializationListener() {
            @Override
            public void onInitialized() {
                // TextToSpeech initialized successfully
                TextToSpeechHelper.speak("Welcome to VBD English!");
            }
            @Override
            public void onInitializationFailed() {
            }
        });

        viewPager = findViewById(R.id.viewPager);
        bottomNav = findViewById(R.id.bottomNav);

        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Not needed for updating BottomNavigationView
            }

            @Override
            public void onPageSelected(int position) {
                // Update the selected item in the BottomNavigationView when the page changes
                bottomNav.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Not needed for updating BottomNavigationView
            }
        });

        bottomNav.setOnNavigationItemSelectedListener(item -> {
            // Handle BottomNavigationView item clicks
            int itemId = item.getItemId();

            if (itemId == R.id.menu_home) {
                viewPager.setCurrentItem(0, true); // Home fragment
                return true;
            }
            if (itemId == R.id.menu_library) {
                viewPager.setCurrentItem(1, true); // Library fragment
                return true;
            }
            if (itemId == R.id.menu_discover) {
                viewPager.setCurrentItem(2, true); // Discover fragment
                return true;
            }
            if (itemId == R.id.menu_profile) {
                viewPager.setCurrentItem(3, true); // Profile fragment
                return true;
            }
            return false;
        });
    }

    private static class MyViewPagerAdapter extends FragmentStatePagerAdapter {
        MyViewPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            // Return the fragment at the specified position
            switch (position) {
                default:
                    return new HomeFragment();
                case 1:
                    return new LibraryFragment();
                case 2:
                    return new DiscoverFragment();
                case 3:
                    return new ProfileFragment();
            }
        }

        @Override
        public int getCount() {
            // Return the total number of fragments
            return 4;
        }
    }
}
