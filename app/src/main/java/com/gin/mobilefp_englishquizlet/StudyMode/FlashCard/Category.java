package com.gin.mobilefp_englishquizlet.StudyMode.FlashCard;

import java.util.List;

public class Category {

    private List<WordItemTopic> wordItemTopics;

    public Category(List<WordItemTopic> wordItemTopics) {
        this.wordItemTopics = wordItemTopics;
    }

    public List<WordItemTopic> getWordItemTopics() {
        return wordItemTopics;
    }

    public void setWordItemTopics(List<WordItemTopic> wordItemTopics) {
        this.wordItemTopics = wordItemTopics;
    }
}
