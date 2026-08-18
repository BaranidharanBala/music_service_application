package com.musicservice.service.Implementation;

import com.musicservice.enums.Genre;
import com.musicservice.model.Album;
import com.musicservice.model.Track;
import com.musicservice.repository.IAlbumRepository;
import com.musicservice.service.IAlbumService;
import com.musicservice.service.IPlayService;
import com.musicservice.service.ITrackService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlbumService implements IAlbumService {

    private static AlbumService instance;
    private final IAlbumRepository albumRepository;
    ITrackService trackService;
    IPlayService playService;

    private AlbumService(IAlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public static AlbumService getInstance(IAlbumRepository albumRepository) {
        if (instance == null) {
            instance = new AlbumService(albumRepository);
        }
        return instance;
    }

    @Override
    public void setTrackService(ITrackService trackService) {
        this.trackService = trackService;
    }

    @Override
    public void setPlayService(IPlayService playService) {
        this.playService = playService;
    }

    @Override
    public boolean addAlbum(Album album) {
        if (album == null) {
            return false;
        }

        albumRepository.save(album);

        return true;
    }

    @Override
    public boolean removeAlbum(int id) {
        Album album = albumRepository.findById(id);
        if (album == null) {
            return false;
        }

        albumRepository.delete(id);

        return true;
    }

    @Override
    public boolean updateAlbum(Album album) {
        if (album == null) {
            return false;
        }

        albumRepository.update(album);

        return true;
    }

    @Override
    public Album getAlbumById(int id) {
        return albumRepository.findById(id);
    }

    @Override
    public Map<Integer, Album> getAlbumByName(String name) {
        return albumRepository.search(name);
    }

    @Override
    public Map<Integer, Album> getAlbumByArtist(int artistId) {
        return albumRepository.findByArtist(artistId);
    }

    @Override
    public Map<Integer, Album> getAlbumByGenre(Genre genre) {
        return albumRepository.findByGenre(genre);
    }

    @Override
    public Map<Integer, Album> getAllAlbums() {
        return albumRepository.findAll();
    }

    @Override
    public boolean addTrackToAlbum(int albumId, int trackId, int artistId) {
        Album album = albumRepository.findById(albumId);
        if (album == null) {
            return false;
        }

        Track track = trackService.getTrackById(trackId);
        if (track == null) {
            return false;
        }

        if (album.getArtistId() != artistId || track.getArtistId() != artistId || track.getAlbumId() != null) {
            return false;
        }

        track.setAlbumId(albumId);

        return trackService.updateTrack(track);
    }

    @Override
    public boolean removeTrackFromAlbum(int albumId, int trackId, int artistId) {
        Album album = albumRepository.findById(albumId);
        if (album == null) {
            return false;
        }

        Track track = trackService.getTrackById(trackId);
        if (track == null) {
            return false;
        }

        if (album.getArtistId() != artistId || track.getArtistId() != artistId || track.getAlbumId() != albumId) {
            return false;
        }

        track.setAlbumId(0);

        return trackService.updateTrack(track);
    }

    @Override
    public boolean playAlbum(int id) {
        if (id <= 0) {
            return false;
        }

        Map<Integer, Track> tracks = trackService.getTracksInAlbumId(id);
        if (tracks == null || tracks.isEmpty()) {
            return false;
        }

        List<Integer> trackIds = new ArrayList<>(tracks.keySet());

        return playService.play(trackIds);
    }

}
