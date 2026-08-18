package com.musicservice.repository.InMemory;

import com.musicservice.enums.Genre;
import com.musicservice.model.Album;
import com.musicservice.repository.IAlbumRepository;

import java.util.HashMap;
import java.util.Map;

public class AlbumRepository implements IAlbumRepository {

    private static AlbumRepository instance;
    private final Map<Integer, Album> ALBUMS;
    private int nextId;

    private AlbumRepository() {
        ALBUMS = new HashMap<>();
        nextId = 0;
    }

    public static AlbumRepository getInstance() {
        if (instance == null) {
            instance = new AlbumRepository();
        }
        return instance;
    }

    @Override
    public void save(Album album) {
        nextId++;
        album.setId(nextId);
        ALBUMS.put(album.getId(), album);
    }

    @Override
    public void update(Album album) {
        ALBUMS.put(album.getId(), album);
    }

    @Override
    public void delete(int id) {
        ALBUMS.remove(id);
    }

    @Override
    public Album findById(int id) {
        return ALBUMS.get(id);
    }

    @Override
    public Map<Integer, Album> search(String name) {
        Map<Integer, Album> result = new HashMap<>();
        for (Album album : ALBUMS.values()) {
            if (album.getName().equalsIgnoreCase(name)) {
                result.put(album.getId(), album);
            }
        }
        return result;
    }

    @Override
    public Map<Integer, Album> findByArtist(int artistId) {
        Map<Integer, Album> result = new HashMap<>();
        for (Album album : ALBUMS.values()) {
            if (album.getArtistId() == artistId) {
                result.put(album.getId(), album);
            }
        }
        return result;
    }

    @Override
    public Map<Integer, Album> findByGenre(Genre genre) {
        Map<Integer, Album> result = new HashMap<>();
        for (Album album : ALBUMS.values()) {
            if (album.getGenre() == genre) {
                result.put(album.getId(), album);
            }
        }
        return result;
    }

    @Override
    public Map<Integer, Album> findAll() {
        return ALBUMS;
    }

}