package com.musicservice.view;

import com.musicservice.enums.Genre;
import com.musicservice.exception.MusicServiceException;
import com.musicservice.model.Track;

import java.util.Map;

public interface IView {

    int getInputInt(String message);

    String getInputString(String message);

    void showError(MusicServiceException e);

    int showMainMenu();

    int showUserHomeMenu();

    int showArtistHomeMenu();

    int showTrackMenu();

    int showTrackOptions();

    int showArtistMenu();

    int showArtistOptions();

    int showAlbumMenu();

    int showAlbumOptions();

    int showPlaylistMenu();

    int showPlaylistOptions();

    int showLibraryMenu();

    int showArtistDashboard();

    int showTrackManagementMenu();

    int showAlbumManagementMenu();

    int showAlbumManagementOptions();

    int showPlaybackMenu();

    void show(Object object);

    void show(Map<?, ?> map);

    void show(String message);

    void show(Genre[] genres);

    void showTrack(Track track, String artist);

}