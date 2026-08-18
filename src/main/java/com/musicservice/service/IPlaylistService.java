package com.musicservice.service;

import com.musicservice.model.Playlist;
import com.musicservice.model.Track;

import java.util.Map;

public interface IPlaylistService {

    void setTrackService(ITrackService trackService);

    void setLibraryService(ILibraryService libraryService);

    void setPlayService(IPlayService playService);

    boolean addPlaylist(Playlist playlist);

    boolean removePlaylist(int id);

    boolean updatePlaylist(Playlist playlist);

    Playlist getPlaylistById(int id);

    Map<Integer, Playlist> getAllPlaylists();

    Map<Integer, Track> getTracksInPlaylist(int id);

    boolean addTrackToPlaylist(int id, int trackId);

    boolean removeTrackFromPlaylist(int id, int trackId);

    boolean playPlaylist(int id);

}
