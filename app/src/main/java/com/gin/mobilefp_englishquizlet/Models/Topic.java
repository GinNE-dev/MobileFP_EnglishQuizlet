package com.gin.mobilefp_englishquizlet.Models;

import java.util.HashMap;

public class Topic {
    String id;
    String name;
    String description;
    String avaURL;
    HashMap<String, String> belongsToFolders;
    String ownerID;

    public Topic() {
    }

    public Topic(String id, String name, String description, String ownerID) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerID = ownerID;
        this.avaURL = null;

        HashMap<String, String> belongsToFolders = new HashMap<>();
        belongsToFolders.put("default_folder_id", "false");
        this.belongsToFolders = belongsToFolders;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvaURL() {
        return avaURL;
    }

    public void setAvaURL(String avaURL) {
        this.avaURL = avaURL;
    }

    public HashMap<String, String> getBelongsToFolders() {
        return belongsToFolders;
    }

    public void setBelongsToFolders(HashMap<String, String> belongsToFolders) {
        this.belongsToFolders = belongsToFolders;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }
}
