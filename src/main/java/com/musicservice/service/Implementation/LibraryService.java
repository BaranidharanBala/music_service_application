package com.musicservice.service.Implementation;

import com.musicservice.model.Library;
import com.musicservice.model.Track;
import com.musicservice.repository.ILibraryRepository;
import com.musicservice.service.ILibraryService;
import com.musicservice.service.ITrackService;

import java.util.Map;

public class LibraryService implements ILibraryService {

    private static LibraryService instance;
    private final ILibraryRepository libraryRepository;

    private ITrackService trackService;

    private LibraryService(ILibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }

    public static synchronized LibraryService getInstance(ILibraryRepository libraryRepository) {
        if (instance == null) {
            instance = new LibraryService(libraryRepository);
        }
        return instance;
    }

    @Override
    public void setTrackService(ITrackService trackService) {
        this.trackService = trackService;
    }

    @Override
    public void addLibrary(Library library) {
        libraryRepository.save(library);
    }

    @Override
    public boolean likeTrack(int userId, int trackId) {
        Library library = libraryRepository.findByUserId(userId);
        if (library == null) {
            return false;
        }

        if (library.getLikedTrackIds().contains(trackId)) {
            return false;
        }

        library.getLikedTrackIds().add(trackId);
        libraryRepository.update(library);

        return true;
    }

    @Override
    public boolean unlikeTrack(int userId, int trackId) {
        Library library = libraryRepository.findByUserId(userId);
        if (library == null) {
            return false;
        }

        if (!library.getLikedTrackIds().contains(trackId)) {
            return false;
        }

        library.getLikedTrackIds().remove((Integer) trackId);
        libraryRepository.update(library);

        return true;
    }

    @Override
    public boolean addPlaylistToLibrary(int userId, int playlistId) {
        Library library = libraryRepository.findByUserId(userId);
        if (library == null) {
            return false;
        }

        if (library.getPlaylistIds().contains(playlistId)) {
            return false;
        }

        library.getPlaylistIds().add(playlistId);
        libraryRepository.update(library);

        return true;
    }

    @Override
    public boolean removePlaylistFromLibrary(int userId, int playlistId) {
        Library library = libraryRepository.findByUserId(userId);
        if (library == null) {
            return false;
        }

        if (!library.getPlaylistIds().contains(playlistId)) {
            return false;
        }

        library.getPlaylistIds().remove((Integer) playlistId);
        libraryRepository.update(library);

        return true;
    }

    @Override
    public Map<Integer, Track> getLikedTrack(int userId) {
        Library library = libraryRepository.findByUserId(userId);

        return trackService.getTracks(library.getLikedTrackIds());
    }

}