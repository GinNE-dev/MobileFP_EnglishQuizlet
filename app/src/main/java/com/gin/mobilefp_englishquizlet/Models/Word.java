package com.gin.mobilefp_englishquizlet.Models;

import java.io.Serializable;
import java.util.HashMap;

public class Word implements Serializable {
    String id;
    String term;
    String definition;
    String description;
    HashMap<String, Integer> learnCounts;

    public Word() {
        this.learnCounts = new HashMap<>();
    }

    public Word(String id, String term, String definition, String description) {
        this.id = id;
        this.term = term;
        this.definition = definition;
        this.description = description;
        this.learnCounts = new HashMap<>();
    }

    public Word(String term, String definition, String description) {
        this.term = term;
        this.definition = definition;
        this.description = description;
        this.learnCounts = new HashMap<>();
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

    public void setLearnCounts(HashMap<String, Integer> learnCounts) {
        this.learnCounts = learnCounts;
    }

    public HashMap<String, Integer> getLearnCounts() {return learnCounts;}
}
