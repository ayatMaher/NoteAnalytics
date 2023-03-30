package com.example.noteanalyticsapplication.model;

import java.io.Serializable;

public class Note implements Serializable {
    String noteId;
    String name;
    String description;
    String image;

    public Note(String noteId, String name) {
        this.noteId = noteId;
        this.name = name;
    }

    public Note(String noteId, String name, String description, String image) {
        this.noteId = noteId;
        this.name = name;
        this.description = description;
        this.image = image;
    }


    public String getNoteId() {
        return noteId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }
}
