package com.gin.mobilefp_englishquizlet.Models;

public class Word {
    String id;
    String term;
    String definition;
    String description;

    public Word() {
    }

    public Word(String id, String term, String definition, String description) {
        this.id = id;
        this.term = term;
        this.definition = definition;
        this.description = description;
    }

    public Word(String term, String definition, String description) {
        this.term = term;
        this.definition = definition;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
