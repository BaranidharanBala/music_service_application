package com.musicservice.repository.InMemory;

import com.musicservice.model.Playlist;
import com.musicservice.repository.IPlaylistRepository;

import java.util.HashMap;
import java.util.Map;

public class PlaylistRepository implements IPlaylistRepository {

    private static PlaylistRepository instance;
    private final Map<Integer, Playlist> PLAYLISTS;
    int nextId;

    private PlaylistRepository() {
        PLAYLISTS = new HashMap<>();
        nextId = 0;
    }

    public static PlaylistRepository getInstance() {
        if (instance == null) {
            instance = new PlaylistRepository();
        }
        return instance;
    }

    @Override
    public void save(Playlist playlist) {
        nextId++;
        playlist.setId(nextId);
        PLAYLISTS.put(playlist.getId(), playlist);
    }

    @Override
    public void update(Playlist playlist) {
        PLAYLISTS.put(playlist.getId(), playlist);
    }

    @Override
    public void delete(int id) {
        PLAYLISTS.remove(id);
    }

    @Override
    public Playlist findById(int id) {
        return PLAYLISTS.get(id);
    }

    @Override
    public Map<Integer, Playlist> findByName(String name) {
        Map<Integer, Playlist> result = new HashMap<>();
        for (Playlist playlist : PLAYLISTS.values()) {
            if (playlist.getName().equalsIgnoreCase(name)) {
                result.put(playlist.getId(), playlist);
            }
        }
        return result;
    }

    @Override
    public Map<Integer, Playlist> findByUserId(int userId) {
        Map<Integer, Playlist> result = new HashMap<>();
        for (Playlist playlist : PLAYLISTS.values()) {
            if (playlist.getUserId() == userId) {
                result.put(playlist.getId(), playlist);
            }
        }
        return result;
    }

    @Override
    public Map<Integer, Playlist> findAll() {
        return PLAYLISTS;
    }

}