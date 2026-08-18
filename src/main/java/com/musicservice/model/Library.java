package com.musicservice.model;

import java.util.List;

public class Library {

    private final int userId;
    private List<Integer> likedTrackIds;
    private List<Integer> playlistIds;

    public Library(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public List<Integer> getLikedTrackIds() {
        return likedTrackIds;
    }

    public void setLikedTrackIds(List<Integer> likedTrackIds) {
        this.likedTrackIds = likedTrackIds;
    }

    public List<Integer> getPlaylistIds() {
        return playlistIds;
    }

    public void setPlaylistIds(List<Integer> playlistIds) {
        this.playlistIds = playlistIds;
    }

}