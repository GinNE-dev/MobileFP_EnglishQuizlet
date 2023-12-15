package com.gin.mobilefp_englishquizlet.Models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public class Topic {
    String id;
    String title;
    String description;
    HashMap<String, Boolean> belongsToFolders;
    String owner;
    Boolean isPrivate;
    ArrayList<Word> words;
    String lastUpdatedDate;
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

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        this.lastUpdatedDate = dateFormat.format(date);

        this.belongsToFolders = new HashMap<>();
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

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public int getScoreRecordsSize() {
        return scoreRecords.size();
    }

    public Date getLastUpdatedDateAsDate() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            return dateFormat.parse(lastUpdatedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Handle parsing exception as needed
        }
    }

    public static class ScoreRecordsComparator implements Comparator<Topic> {
        @Override
        public int compare(Topic topic1, Topic topic2) {
            return Integer.compare(topic2.getScoreRecordsSize(), topic1.getScoreRecordsSize());
        }
    }

    public static class LastUpdatedDateComparator implements Comparator<Topic> {
        @Override
        public int compare(Topic topic1, Topic topic2) {
            return topic2.getLastUpdatedDateAsDate().compareTo(topic1.getLastUpdatedDateAsDate());
        }
    }
}
