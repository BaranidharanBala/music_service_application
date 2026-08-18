package com.musicservice.repository.InMemory;

import com.musicservice.model.Artist;
import com.musicservice.repository.IArtistRepository;

import java.util.HashMap;
import java.util.Map;

public class ArtistRepository implements IArtistRepository {

    private static ArtistRepository instance;
    private final Map<Integer, Artist> ARTISTS;
    private int nextId;

    private ArtistRepository() {
        ARTISTS = new HashMap<>();
        nextId = 0;
    }

    public static ArtistRepository getInstance() {
        if (instance == null) {
            instance = new ArtistRepository();
        }
        return instance;
    }

    @Override
    public void save(Artist artist) {
        nextId++;
        artist.setId(nextId);
        ARTISTS.put(artist.getId(), artist);
    }

    @Override
    public void update(Artist artist) {
        ARTISTS.put(artist.getId(), artist);
    }

    @Override
    public void delete(int id) {
        ARTISTS.remove(id);
    }

    @Override
    public Artist findById(int id) {
        return ARTISTS.get(id);
    }

    @Override
    public Artist findByUserId(int userId) {
        for (Artist artist : ARTISTS.values()) {
            if (artist.getUserId() == userId) {
                return artist;
            }
        }
        return null;
    }

    @Override
    public Map<Integer, Artist> search(String name) {
        Map<Integer, Artist> result = new HashMap<>();
        for (Artist artist : ARTISTS.values()) {
            if (artist.getName().equalsIgnoreCase(name)) {
                result.put(artist.getId(), artist);
            }
        }
        return result;
    }

    @Override
    public Map<Integer, Artist> findAll() {
        return ARTISTS;
    }
}