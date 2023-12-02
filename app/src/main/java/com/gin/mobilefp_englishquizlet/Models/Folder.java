package com.gin.mobilefp_englishquizlet.Models;

public class Folder {
    String id;
    String title;
    boolean editable;

    public Folder() {
    }

    public Folder(String id, String title, boolean editable) {
        this.id = id;
        this.title = title;
        this.editable = editable;
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

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
