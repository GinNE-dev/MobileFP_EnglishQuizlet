package com.gin.mobilefp_englishquizlet.ViewPager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.gin.mobilefp_englishquizlet.Fragment.FragmentQuestion;
import com.gin.mobilefp_englishquizlet.StudyMode.FlashCard.WordItemTopic;

import java.util.List;

public class ViewPagerAdapterFlashcard extends FragmentStatePagerAdapter {
    private List<WordItemTopic> mListQuestion;
    public ViewPagerAdapterFlashcard(@NonNull FragmentManager fm, int behavior, List<WordItemTopic> list) {
        super(fm, behavior);
        this.mListQuestion = list;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        WordItemTopic wordItemTopic = mListQuestion.get(position);

        FragmentQuestion fragmentQuestion = new FragmentQuestion();
        Bundle bundle = new Bundle();
        bundle.putSerializable("question_object", wordItemTopic);

        fragmentQuestion.setArguments(bundle);

        return fragmentQuestion;
    }

    @Override
    public int getCount() {
        return mListQuestion.size();
    }
}
