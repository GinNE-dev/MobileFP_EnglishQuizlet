package com.gin.mobilefp_englishquizlet.StudyMode.FlashCard;

import java.io.Serializable;

public class WordItemTopic implements Serializable {
    String wordTerm;
    String wordDefinition;

    public WordItemTopic(String wordTerm, String wordDefinition) {

        this.wordTerm = wordTerm;
        this.wordDefinition = wordDefinition;
    }

    public String getWordTerm() {
        return wordTerm;
    }

    public void setWordTerm(String wordTerm) {
        this.wordTerm = wordTerm;
    }

    public String getWordDefinition() {
        return wordDefinition;
    }

    public void setWordDefinition(String wordDefinition) {
        this.wordDefinition = wordDefinition;
    }
}
