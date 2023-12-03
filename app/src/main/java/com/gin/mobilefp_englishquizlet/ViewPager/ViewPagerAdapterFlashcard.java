package com.gin.mobilefp_englishquizlet.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.gin.mobilefp_englishquizlet.Fragment.FragmentQuestion;
import com.gin.mobilefp_englishquizlet.Models.Word;
import com.gin.mobilefp_englishquizlet.StudyMode.FlashCard.WordItemTopic;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class ViewPagerAdapterFlashcard extends FragmentStatePagerAdapter {
    private List<Word> mListQuestion;
    private String mTopicID;
    public ViewPagerAdapterFlashcard(@NonNull FragmentManager fm, int behavior, List<Word> list, String topicID) {
        super(fm, behavior);
        this.mListQuestion = list;
        this.mTopicID = topicID;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        Word wordItemTopic = mListQuestion.get(position);

        FragmentQuestion fragmentQuestion = new FragmentQuestion();
        Bundle bundle = new Bundle();
        bundle.putSerializable("question_object", wordItemTopic);

        fragmentQuestion.setArguments(bundle);
        fragmentQuestion.registerLearn(word->{
            updateLearnProgress(word);
        });

        return fragmentQuestion;
    }

    private void updateLearnProgress(Word word){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        HashMap<String, Integer> learnCounts = word.getLearnCounts();
        assert learnCounts != null;
        int count = learnCounts.getOrDefault(user.getUid(), 0);
        learnCounts.put(user.getUid(), ++count);
        FirebaseDatabase.getInstance().getReference("topics").child(mTopicID).child("words").child(word.getId()).child("learnCounts").setValue(word.getLearnCounts());
    }

    @Override
    public int getCount() {
        return mListQuestion.size();
    }
}
