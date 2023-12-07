package com.gin.mobilefp_englishquizlet.Models;

import java.util.ArrayList;
import java.util.HashMap;

public class Topic {
    String id;
    String title;
    String description;
    HashMap<String, Boolean> belongsToFolders;
    String owner;
    Boolean isPrivate;
    ArrayList<Word> words;

    private HashMap<String, Record> scoreRecords;

    public Topic() {
        this.scoreRecords = new HashMap<>();
    }

    public Topic(String id, String title, String description, String owner, ArrayList<Word> words, boolean isPrivate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.words = words;
        this.isPrivate = isPrivate;

        HashMap<String, Boolean> belongsToFolders = new HashMap<>();
        belongsToFolders.put("default_folder_id", false);
        this.belongsToFolders = belongsToFolders;
        this.scoreRecords = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashMap<String, Boolean> getBelongsToFolders() {
        return belongsToFolders;
    }

    public void setBelongsToFolders(HashMap<String, Boolean> belongsToFolders) {
        this.belongsToFolders = belongsToFolders;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ArrayList<Word> getWords() {
        return words;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }

    public HashMap<String, Record> getScoreRecords() {return scoreRecords;}

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }
}
