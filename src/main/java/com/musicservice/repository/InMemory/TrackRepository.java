package com.musicservice.repository.InMemory;

import com.musicservice.enums.Genre;
import com.musicservice.model.Track;
import com.musicservice.repository.ITrackRepository;

import java.util.HashMap;
import java.util.Map;

public class TrackRepository implements ITrackRepository {

    private static TrackRepository instance;
    private final Map<Integer, Track> TRACKS;
    private int nextId;

    private TrackRepository() {
        TRACKS = new HashMap<>();
        nextId = 0;
    }

    public static TrackRepository getInstance() {
        if (instance == null) {
            instance = new TrackRepository();
        }
        return instance;
    }

    @Override
    public void save(Track track) {
        nextId++;
        track.setId(nextId);
        TRACKS.put(track.getId(), track);
    }

    @Override
    public void update(Track track) {
        TRACKS.put(track.getId(), track);
    }

    @Override
    public void delete(int id) {
        TRACKS.remove(id);
    }

    @Override
    public Track findById(int id) {
        return TRACKS.get(id);
    }

    @Override
    public Map<Integer, Track> findByArtist(int artistId) {
        Map<Integer, Track> result = new HashMap<>();
        for (Track track : TRACKS.values()) {
            if (track.getArtistId() == artistId) {
                result.put(track.getId(), track);
            }
        }
        return result;
    }

    @Override
    public Map<Integer, Track> findByAlbum(int albumId) {
        Map<Integer, Track> result = new HashMap<>();
        for (Track track : TRACKS.values()) {
            if (track.getAlbumId() == albumId) {
                result.put(track.getId(), track);
            }
        }
        return result;
    }

    @Override
    public Map<Integer, Track> findByGenre(Genre genre) {
        Map<Integer, Track> result = new HashMap<>();
        for (Track track : TRACKS.values()) {
            if (track.getGenre() == genre) {
                result.put(track.getId(), track);
            }
        }
        return result;
    }

    @Override
    public Map<Integer, Track> findAll() {
        return TRACKS;
    }

    @Override
    public Map<Integer, Track> search(String name) {
        Map<Integer, Track> result = new HashMap<>();
        for (Track track : TRACKS.values()) {
            if (track.getName().equalsIgnoreCase(name)) {
                result.put(track.getId(), track);
            }
        }
        return result;
    }


}