package com.musicservice.repository;

import com.musicservice.model.Artist;

import java.util.Map;

public interface IArtistRepository {

    void save(Artist artist);

    void update(Artist artist);

    void delete(int id);

    Artist findById(int id);

    Artist findByUserId(int userId);

    Map<Integer, Artist> search(String stageName);

    Map<Integer, Artist> findAll();

}