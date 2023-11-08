package com.gin.mobilefp_englishquizlet.StudyMode.FlashCard;

public class WordItemTopic {
    int resourceId;
    String term;
    String definition;
    String wordTerm;
    String wordDefinition;

    public WordItemTopic(int resourceId, String term, String definition, String wordTerm, String wordDefinition) {
        this.resourceId = resourceId;
        this.term = term;
        this.definition = definition;
        this.wordTerm = wordTerm;
        this.wordDefinition = wordDefinition;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

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
