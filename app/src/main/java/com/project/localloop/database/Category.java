package com.project.localloop.database;

import androidx.annotation.NonNull;

public class Category {
    private String categoryUID;
    private String name;
    private String description;

    // Default constructor for firebase use
    public Category() {}

    // With params
    public Category(String id, String name, String description) {
        this.categoryUID = id;
        this.name = name;
        this.description = description;
    }

    // Getter 和 Setter
    public String getCategoryId() {
        return categoryUID;
    }
    public void setCategoryId(String id) {
        this.categoryUID = id;
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

    @NonNull
    @Override
    public String toString() {
        return name;  // for test use, not necessarily necessary
    }
}
