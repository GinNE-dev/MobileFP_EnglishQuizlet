package com.gin.mobilefp_englishquizlet.Models;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    String id;
    String email;
    String name;
    String avaURL;
    HashMap<String, Folder> folders;

    public User() {
    }

    public User(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.avaURL = "https://firebasestorage.googleapis.com/v0/b/finalproject-395e5.appspot.com/o/defaultava.png?alt=media&token=abfccaa7-6875-47f0-a999-e8d495b7eea1";

        Folder fav = new Folder(id, "My Favorite", false);
        this.folders = new HashMap<>();
        this.folders.put(id, fav);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvaURL() {
        return avaURL;
    }

    public void setAvaURL(String avaURL) {
        this.avaURL = avaURL;
    }

    public HashMap<String, Folder> getFolders() {
        return folders;
    }

    public void setFolders(HashMap<String, Folder> folders) {
        this.folders = folders;
    }
}
