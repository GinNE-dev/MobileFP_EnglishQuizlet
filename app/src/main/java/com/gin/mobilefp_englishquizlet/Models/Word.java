package com.gin.mobilefp_englishquizlet.Models;

public class Word {
    String id;
    String term;
    String definition;
    String description;
    String belongsToTopic;
    String avtURL;

    public Word() {
    }

    public Word(String id, String term, String definition, String belongsToTopic) {
        this.id = id;
        this.term = term;
        this.definition = definition;
        this.belongsToTopic = belongsToTopic;

        this.description = "An English word with definition";
        this.avtURL = null;
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

    public String getBelongsToTopic() {
        return belongsToTopic;
    }

    public void setBelongsToTopic(String belongsToTopic) {
        this.belongsToTopic = belongsToTopic;
    }

    public String getAvtURL() {
        return avtURL;
    }

    public void setAvtURL(String avtURL) {
        this.avtURL = avtURL;
    }
}
