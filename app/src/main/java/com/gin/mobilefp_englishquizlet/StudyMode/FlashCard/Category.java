package com.gin.mobilefp_englishquizlet.StudyMode.FlashCard;

import com.gin.mobilefp_englishquizlet.Models.Word;

import java.util.List;

public class Category {

    private List<Word> wordItemTopics;

    public Category(List<Word> wordItemTopics) {
        this.wordItemTopics = wordItemTopics;
    }

    public List<Word> getWordItemTopics() {
        return wordItemTopics;
    }

    public void setWordItemTopics(List<Word> wordItemTopics) {
        this.wordItemTopics = wordItemTopics;
    }
}
