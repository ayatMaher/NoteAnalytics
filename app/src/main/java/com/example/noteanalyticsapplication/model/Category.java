package com.example.noteanalyticsapplication.model;

import java.io.Serializable;

public class Category implements Serializable {
    String categoryId;
    String name;

    public Category(String id, String name) {
        this.categoryId = id;
        this.name = name;
    }


    public String getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }
}
