package com.musicservice.repository;

import com.musicservice.enums.Genre;
import com.musicservice.model.Track;

import java.util.Map;

public interface ITrackRepository {

    void save(Track track);

    void update(Track track);

    void delete(int id);

    Track findById(int id);

    Map<Integer, Track> findByArtist(int artistId);

    Map<Integer, Track> findByAlbum(int albumId);

    Map<Integer, Track> findByGenre(Genre genre);

    Map<Integer, Track> findAll();

    Map<Integer, Track> search(String name);

}