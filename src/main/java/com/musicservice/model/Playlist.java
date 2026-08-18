package com.musicservice.model;

import java.util.ArrayList;
import java.util.List;

public class Playlist {

    private int id;
    private String name;
    private int userId;
    private List<Integer> trackIds = new ArrayList<>();

    public Playlist(String name, int userId) {
        this.name = name;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Integer> getTrackIds() {
        return trackIds;
    }

    public void setTrackIds(List<Integer> trackIds) {
        this.trackIds = trackIds;
    }

    @Override
    public String toString() {
        return "[ "+id+" "+name+" ]";
    }
}