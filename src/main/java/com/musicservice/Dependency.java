package com.musicservice;

import com.musicservice.controller.IArtistController;
import com.musicservice.controller.Implementation.ArtistController;
import com.musicservice.controller.Implementation.Controller;
import com.musicservice.repository.DataBase.*;
import com.musicservice.repository.*;
import com.musicservice.service.*;
import com.musicservice.service.Implementation.*;
import com.musicservice.view.IView;
import com.musicservice.view.View;

public class Dependency {

    private final IView view;

    private final IUserRepository userRepository;
    private final IArtistRepository artistRepository;
    private final ITrackRepository trackRepository;
    private final IAlbumRepository albumRepository;
    private final IPlaylistRepository playlistRepository;
    private final ILibraryRepository libraryRepository;

    private final IUserService userService;
    private final IArtistService artistService;
    private final ITrackService trackService;
    private final IAlbumService albumService;
    private final IPlaylistService playlistService;
    private final ILibraryService libraryService;
    private final IPlayService playService;

    private final IArtistController artistController;
    private final Controller controller;

    public Dependency() {

        view = View.getInstance();
        userRepository = DBUserRepository.getInstance();
        artistRepository = DBArtistRepository.getInstance();
        trackRepository = DBTrackRepository.getInstance();
        albumRepository = DBAlbumRepository.getInstance();
        playlistRepository = DBPlaylistRepository.getInstance();
        libraryRepository = DBLibraryRepository.getInstance();

        artistService = ArtistService.getInstance(artistRepository);
        userService = UserService.getInstance(userRepository);
        trackService = TrackService.getInstance(trackRepository);
        albumService = AlbumService.getInstance(albumRepository);
        playlistService = PlaylistService.getInstance(playlistRepository);
        libraryService = LibraryService.getInstance(libraryRepository);
        //playService = PlayService.getInstance(trackService);
        playService = LogicalPlayService.getInstance(trackService);

        userService.setArtistService(artistService);
        userService.setLibraryService(libraryService);

        artistService.setUserService(userService);
        artistService.setTrackService(trackService);
        artistService.setPlayService(playService);

        trackService.setLibraryService(libraryService);
        trackService.setPlayService(playService);

        albumService.setTrackService(trackService);
        albumService.setPlayService(playService);

        playlistService.setTrackService(trackService);
        playlistService.setLibraryService(libraryService);
        playlistService.setPlayService(playService);

        libraryService.setTrackService(trackService);

        artistController = ArtistController.getInstance(view, artistService, trackService, albumService);
        controller = new Controller(view, userService, artistService, trackService, albumService, playlistService, libraryService, playService, artistController);
    }

    public Controller getController() {
        return controller;
    }

}