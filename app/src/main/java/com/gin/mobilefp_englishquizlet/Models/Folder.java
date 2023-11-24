package com.gin.mobilefp_englishquizlet.Models;

public class Folder {
    String id;
    String name;

    public Folder() {
    }

    public Folder(String id, String name) {
        this.id = id;
        this.name = name;
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
}
