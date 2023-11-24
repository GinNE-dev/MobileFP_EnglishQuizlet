package com.gin.mobilefp_englishquizlet.Models;

public class Folder {
    String id;
    String name;
    String ownerID;

    public Folder() {
    }

    public Folder(String id, String name, String ownerID) {
        this.id = id;
        this.name = name;
        this.ownerID = ownerID;
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

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }
}
