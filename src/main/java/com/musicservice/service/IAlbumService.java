package com.musicservice.service;

import com.musicservice.enums.Genre;
import com.musicservice.model.Album;

import java.util.Map;

public interface IAlbumService {

    void setTrackService(ITrackService trackService);

    void setPlayService(IPlayService playService);

    boolean addAlbum(Album album);

    boolean removeAlbum(int id);

    boolean updateAlbum(Album album);

    Album getAlbumById(int id);

    Map<Integer, Album> getAlbumByName(String name);

    Map<Integer, Album> getAlbumByArtist(int artistId);

    Map<Integer, Album> getAlbumByGenre(Genre genre);

    Map<Integer, Album> getAllAlbums();

    boolean addTrackToAlbum(int id, int trackId, int artistId);

    boolean removeTrackFromAlbum(int id, int trackId, int artistId);

    boolean playAlbum(int id);

}
