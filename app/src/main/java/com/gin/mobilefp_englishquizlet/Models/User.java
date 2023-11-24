package com.gin.mobilefp_englishquizlet.Models;

public class User {
    String email;
    String name;
    String avaURL;

    public User() {
    }

    public User(String email, String name, String avaURL) {
        this.email = email;
        this.name = name;
        this.avaURL = avaURL;
    }

    public User(String email, String name) {
        this.email = email;
        this.name = name;
        this.avaURL = "defaultava.png";
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
}
