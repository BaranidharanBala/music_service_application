package com.musicservice.service.Implementation;

import com.musicservice.model.Playlist;
import com.musicservice.model.Track;
import com.musicservice.repository.IPlaylistRepository;
import com.musicservice.service.ILibraryService;
import com.musicservice.service.IPlayService;
import com.musicservice.service.IPlaylistService;
import com.musicservice.service.ITrackService;

import java.util.Map;

public class PlaylistService implements IPlaylistService {

    private static PlaylistService instance;
    private final IPlaylistRepository playlistRepository;
    ITrackService trackService;
    ILibraryService libraryService;
    IPlayService playService;

    private PlaylistService(IPlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public static synchronized PlaylistService getInstance(IPlaylistRepository playlistRepository) {
        if (instance == null) {
            instance = new PlaylistService(playlistRepository);
        }
        return instance;
    }

    @Override
    public void setTrackService(ITrackService trackService) {
        this.trackService = trackService;
    }

    @Override
    public void setLibraryService(ILibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @Override
    public void setPlayService(IPlayService playService) {
        this.playService = playService;
    }

    @Override
    public boolean addPlaylist(Playlist playlist) {
        if (playlist == null) {
            return false;
        }

        playlistRepository.save(playlist);

        return libraryService.addPlaylistToLibrary(playlist.getUserId(), playlist.getId());
    }

    @Override
    public boolean removePlaylist(int id) {
        if (id <= 0) {
            return false;
        }

        playlistRepository.delete(id);

        return libraryService.removePlaylistFromLibrary(playlistRepository.findById(id).getUserId(), id);
    }

    @Override
    public boolean updatePlaylist(Playlist playlist) {
        if (playlist == null) {
            return false;
        }

        Playlist existingPlaylist = playlistRepository.findById(playlist.getId());
        if (existingPlaylist == null) {
            return false;
        }

        playlistRepository.update(playlist);

        return true;
    }

    @Override
    public Playlist getPlaylistById(int id) {
        return playlistRepository.findById(id);
    }

    @Override
    public Map<Integer, Playlist> getAllPlaylists() {
        return playlistRepository.findAll();
    }

    @Override
    public Map<Integer, Track> getTracksInPlaylist(int id) {
        Playlist playlist = playlistRepository.findById(id);

        return trackService.getTracks(playlist.getTrackIds());
    }

    @Override
    public boolean addTrackToPlaylist(int id, int trackId) {
        if (id <= 0 || trackId <= 0) {
            return false;
        }

        Playlist playlist = playlistRepository.findById(id);
        if (playlist.getTrackIds().contains(id)) {
            return false;
        }

        playlist.getTrackIds().add(trackId);
        playlistRepository.update(playlist);

        return true;
    }

    @Override
    public boolean removeTrackFromPlaylist(int id, int trackId) {
        if (id <= 0 || trackId <= 0) {
            return false;
        }

        Playlist playlist = playlistRepository.findById(id);
        if (!playlist.getTrackIds().contains(id)) {
            return false;
        }

        playlist.getTrackIds().remove(trackId);
        playlistRepository.update(playlist);

        return true;
    }

    @Override
    public boolean playPlaylist(int id) {
        if (id <= 0) {
            return false;
        }

        Playlist playlist = playlistRepository.findById(id);
        if (playlist == null) {
            return false;
        }

        if (playlist.getTrackIds() == null) {
            return false;
        }

        return playService.play(playlist.getTrackIds());
    }

}