package com.musicservice.model;

import com.musicservice.enums.Genre;
import com.musicservice.enums.PlaybackStatus;

public class Track {

    private int id;
    private String name;
    private String path;
    private int artistId;
    private Integer albumId;
    private Genre genre;
    private double duration;
    private PlaybackStatus playbackStatus;

    public Track() {
    }

    public Track(String name, String path, int artistId, Genre genre, double duration) {
        this.name = name;
        this.path = path;
        this.artistId = artistId;
        this.genre = genre;
        this.duration = duration;
        this.playbackStatus = PlaybackStatus.STOPPED;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public PlaybackStatus getPlaybackStatus() {
        return playbackStatus;
    }

    public void setPlaybackStatus(PlaybackStatus playbackStatus) {
        this.playbackStatus = playbackStatus;
    }

    @Override
    public String toString() {
        return "%-5d %-20s %-10s %-10s  ".formatted(id,name,genre,playbackStatus);
    }

}