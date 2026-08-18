package com.musicservice.service;

import com.musicservice.enums.Genre;
import com.musicservice.enums.PlaybackStatus;
import com.musicservice.model.Track;

import java.util.List;
import java.util.Map;

public interface ITrackService {

    void setLibraryService(ILibraryService libraryService);

    void setPlayService(IPlayService playService);

    boolean addTrack(Track track);

    boolean removeTrack(int id);

    boolean updateTrack(Track track);

    Track getTrackById(int id);

    Map<Integer, Track> getTracks(List<Integer> trackIds);

    Map<Integer, Track> getTracksByName(String name);

    Map<Integer, Track> getTracksByArtistId(int artistId);

    Map<Integer, Track> getTracksInAlbumId(int albumId);

    Map<Integer, Track> getTracksByGenre(Genre genre);

    Map<Integer, Track> getAllTracks();

    boolean playTrack(int id);

    void updatePlaybackStatus(int id, PlaybackStatus playbackStatus);

    boolean likeTrack(int userId, int id);

    boolean unlikeTrack(int userId, int id);

}
