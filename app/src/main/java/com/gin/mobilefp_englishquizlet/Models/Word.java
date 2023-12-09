package com.gin.mobilefp_englishquizlet.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;

public class Word implements Serializable, Parcelable {
    String term;
    String definition;
    String description;
    HashMap<String, Integer> learnCounts;
    HashMap<String, Boolean> starredList;

    public Word() {
        this.learnCounts = new HashMap<>();
        this.starredList = new HashMap<>();
    }

    public Word(String term, String definition, String description) {
        this.term = term;
        this.definition = definition;
        this.description = description;
        this.learnCounts = new HashMap<>();
        this.starredList = new HashMap<>();
    }

    protected Word(Parcel in) {
        term = in.readString();
        definition = in.readString();
        description = in.readString();
        learnCounts = (HashMap<String, Integer>) in.readSerializable();
        starredList = (HashMap<String, Boolean>) in.readSerializable();
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLearnCounts(HashMap<String, Integer> learnCounts) {
        this.learnCounts = learnCounts;
    }

    public HashMap<String, Integer> getLearnCounts() {return learnCounts;}

    public HashMap<String, Boolean> getStarredList() {
        return starredList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.getTerm());
        dest.writeString(this.getDefinition());
        dest.writeString(this.getDescription());
        dest.writeSerializable(this.getLearnCounts());
        dest.writeSerializable(this.getStarredList());
    }
}
