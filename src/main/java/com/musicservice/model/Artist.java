package com.musicservice.model;

public class Artist {

    private int id;
    private int userId;
    private String name;
    private String bio;

    public Artist() {
    }

    public Artist(String name, String bio) {
        this.name = name;
        this.bio = bio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public String toString() {
        return "%-5d %-20s %-100s".formatted(id, name, bio);
    }

}