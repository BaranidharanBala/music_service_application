package com.musicservice.service;

import com.musicservice.model.Library;
import com.musicservice.model.Track;

import java.util.Map;

public interface ILibraryService {

    void setTrackService(ITrackService trackService);

    void addLibrary(Library library);

    boolean likeTrack(int userId, int trackId);

    boolean unlikeTrack(int userId, int trackId);

    boolean addPlaylistToLibrary(int userId, int playlistId);

    boolean removePlaylistFromLibrary(int userId, int playlistId);

    Map<Integer, Track> getLikedTrack(int userId);

}
