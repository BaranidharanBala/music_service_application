package com.musicservice.repository;

import com.musicservice.enums.Genre;
import com.musicservice.model.Album;

import java.util.Map;

public interface IAlbumRepository {

    void save(Album album);

    void update(Album album);

    void delete(int id);

    Album findById(int id);

    Map<Integer, Album> search(String name);

    Map<Integer, Album> findByArtist(int artistId);

    Map<Integer, Album> findByGenre(Genre genre);

    Map<Integer, Album> findAll();

}