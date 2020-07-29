package com.example.selfjournalapp.model;

import com.google.firebase.Timestamp;

public class Journal {
    private String title;
    private String thought;
    private String ImageUrl;
    private String userId;
    private Timestamp timeadded;
    private String Username;

    public Journal() {
    }

    public Journal(String title, String thought, String imageUrl, String userId, Timestamp timeadded, String username) {
        this.title = title;
        this.thought = thought;
        ImageUrl = imageUrl;
        this.userId = userId;
        this.timeadded = timeadded;
        Username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThought() {
        return thought;
    }

    public void setThought(String thought) {
        this.thought = thought;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getTimeadded() {
        return timeadded;
    }

    public void setTimeadded(Timestamp timeadded) {
        this.timeadded = timeadded;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}