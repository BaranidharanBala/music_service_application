package com.musicservice.service.Implementation;

import com.musicservice.enums.Role;
import com.musicservice.model.Artist;
import com.musicservice.model.Track;
import com.musicservice.model.User;
import com.musicservice.repository.IArtistRepository;
import com.musicservice.service.IArtistService;
import com.musicservice.service.IPlayService;
import com.musicservice.service.ITrackService;
import com.musicservice.service.IUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArtistService implements IArtistService {

    private static ArtistService instance;
    private final IArtistRepository artistRepository;

    private IUserService userService;
    private ITrackService trackService;
    private IPlayService playService;

    private ArtistService(IArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public static synchronized ArtistService getInstance(IArtistRepository artistRepository) {
        if (instance == null) {
            instance = new ArtistService(artistRepository);
        }
        return instance;
    }

    @Override
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public void setPlayService(IPlayService playService) {
        this.playService = playService;
    }

    @Override
    public void setTrackService(ITrackService trackService) {
        this.trackService = trackService;
    }

    @Override
    public boolean addArtist(int userId, Artist artist) {
        if (userId <= 0 || artist == null) {
            return false;
        }

        User user = userService.findUserById(userId);
        if (user == null) {
            return false;
        }

        if (artistRepository.findByUserId(userId) != null) {
            return false;
        }

        artist.setUserId(userId);
        artistRepository.save(artist);

        user.setRole(Role.ARTIST);
        userService.updateUser(user);

        return true;
    }

    @Override
    public boolean removeArtist(int id) {
        if (id <= 0) {
            return false;
        }

        Artist artist = artistRepository.findById(id);
        if (artist == null) {
            return false;
        }

        User user = userService.findUserById(artist.getUserId());
        if (user == null) {
            return false;
        }

        user.setRole(Role.USER);
        userService.updateUser(user);

        artistRepository.delete(id);

        return true;
    }

    @Override
    public boolean updateArtist(Artist artist) {
        if (artist == null) {
            return false;
        }

        Artist existingArtist = artistRepository.findById(artist.getId());
        if (existingArtist == null) {
            return false;
        }

        artistRepository.update(artist);

        return true;
    }

    @Override
    public Artist getArtistById(int id) {
        return artistRepository.findById(id);
    }

    @Override
    public Artist getArtistByUserId(int userId) {
        return artistRepository.findByUserId(userId);
    }

    @Override
    public Map<Integer, Artist> getArtistByName(String name) {
        return artistRepository.search(name);
    }

    @Override
    public Map<Integer, Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    @Override
    public boolean playArtist(int id) {
        if (id <= 0) {
            return false;
        }

        Artist artist = artistRepository.findById(id);
        if (artist == null) {
            return false;
        }

        Map<Integer, Track> tracks = trackService.getTracksByArtistId(id);
        if (tracks == null || tracks.isEmpty()) {
            return false;
        }

        List<Integer> trackIds = new ArrayList<>(tracks.keySet());

        return playService.play(trackIds);
    }

}
