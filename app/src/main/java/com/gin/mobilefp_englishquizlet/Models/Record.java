package com.gin.mobilefp_englishquizlet.Models;

import java.util.Date;

public class Record {
    public enum LearnMode{
        MultipleChoice,
        Typo
    }

    private int score;
    private LearnMode learnMode;
    private long timeConsumed; //millisecond

    public Record() {
    }

    public Record(int score, LearnMode learnMode, long timeConsumed) {
        this.score = score;
        this.learnMode = learnMode;
        this.timeConsumed = timeConsumed;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LearnMode getLearnMode() {
        return learnMode;
    }

    public void setLearnMode(LearnMode learnMode) {
        this.learnMode = learnMode;
    }

    public long getTimeConsumed() {
        return timeConsumed;
    }

    public void setTimeConsumed(long timeConsumed) {
        this.timeConsumed = timeConsumed;
    }
}
