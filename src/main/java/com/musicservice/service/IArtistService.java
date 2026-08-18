package com.musicservice.service;

import com.musicservice.model.Artist;

import java.util.Map;

public interface IArtistService {

    void setUserService(IUserService userService);

    void setPlayService(IPlayService playService);

    void setTrackService(ITrackService trackService);

    boolean addArtist(int userId, Artist artist);

    boolean removeArtist(int id);

    boolean updateArtist(Artist artist);

    Artist getArtistById(int id);

    Artist getArtistByUserId(int userId);

    Map<Integer, Artist> getArtistByName(String name);

    Map<Integer, Artist> getAllArtists();

    boolean playArtist(int id);

}
