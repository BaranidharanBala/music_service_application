package com.musicservice.service.Implementation;

import com.musicservice.enums.Genre;
import com.musicservice.enums.PlaybackStatus;
import com.musicservice.model.Track;
import com.musicservice.repository.ITrackRepository;
import com.musicservice.service.ILibraryService;
import com.musicservice.service.IPlayService;
import com.musicservice.service.ITrackService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackService implements ITrackService {

    private static TrackService instance;
    private final ITrackRepository trackRepository;
    ILibraryService libraryService;
    IPlayService playService;

    private TrackService(ITrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    public static synchronized TrackService getInstance(ITrackRepository trackRepository) {
        if (instance == null) {
            instance = new TrackService(trackRepository);
        }
        return instance;
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
    public boolean addTrack(Track track) {
        if (track == null) {
            return false;
        }
        trackRepository.save(track);
        return true;
    }

    @Override
    public boolean removeTrack(int id) {
        if (id <= 0) {
            return false;
        }

        trackRepository.delete(id);

        return true;
    }

    @Override
    public boolean updateTrack(Track track) {
        if (track == null) {
            return false;
        }
        Track existingTrack = trackRepository.findById(track.getId());
        if (existingTrack == null) {
            return false;
        }
        trackRepository.update(track);
        return true;
    }

    @Override
    public Track getTrackById(int id) {
        return trackRepository.findById(id);
    }

    @Override
    public Map<Integer, Track> getTracks(List<Integer> trackIds) {
        Map<Integer, Track> result = new HashMap<>();
        for (int id : trackIds) {
            result.put(id, getTrackById(id));
        }
        return result;
    }

    @Override
    public Map<Integer, Track> getTracksByName(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        return trackRepository.search(name);
    }

    @Override
    public Map<Integer, Track> getTracksByArtistId(int artistId) {
        return trackRepository.findByArtist(artistId);
    }

    @Override
    public Map<Integer, Track> getTracksInAlbumId(int albumId) {
        return trackRepository.findByAlbum(albumId);
    }

    @Override
    public Map<Integer, Track> getTracksByGenre(Genre genre) {
        return trackRepository.findByGenre(genre);
    }

    @Override
    public Map<Integer, Track> getAllTracks() {
        return trackRepository.findAll();
    }

    @Override
    public boolean playTrack(int id) {
        if (id <= 0) {
            return false;
        }
        Track track = trackRepository.findById(id);
        if (track == null) {
            return false;
        }
        playService.play(List.of(id));
        return true;
    }

    @Override
    public void updatePlaybackStatus(int id, PlaybackStatus playbackStatus) {
        Track track = trackRepository.findById(id);
        if (track == null) {
            return;
        }
        track.setPlaybackStatus(playbackStatus);
        trackRepository.update(track);
    }

    @Override
    public boolean likeTrack(int userId, int id) {
        if (userId <= 0 || id <= 0) {
            return false;
        }
        return libraryService.likeTrack(userId, id);
    }

    @Override
    public boolean unlikeTrack(int userId, int id) {
        if (userId <= 0 || id <= 0) {
            return false;
        }
        return libraryService.unlikeTrack(userId, id);
    }

}
