package com.musicservice.model;

import com.musicservice.enums.Genre;

public class Album {

    private int id;
    private String name;
    private int artistId;
    private Genre genre;

    public Album() {
    }

    public Album(String name, int artistId, Genre genre) {
        this.name = name;
        this.artistId = artistId;
        this.genre = genre;
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

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "%-5d %-30s".formatted(id, name);
    }

}