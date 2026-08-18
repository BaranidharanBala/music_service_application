package com.musicservice.repository;

import com.musicservice.model.Playlist;

import java.util.Map;

public interface IPlaylistRepository {

    void save(Playlist playlist);

    void update(Playlist playlist);

    void delete(int id);

    Playlist findById(int id);

    Map<Integer, Playlist> findByName(String name);

    Map<Integer, Playlist> findByUserId(int userId);

    Map<Integer, Playlist> findAll();

}