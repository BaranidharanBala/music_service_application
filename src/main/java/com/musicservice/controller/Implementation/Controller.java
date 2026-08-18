package com.musicservice.controller.Implementation;

import com.musicservice.controller.IArtistController;
import com.musicservice.enums.Genre;
import com.musicservice.exception.*;
import com.musicservice.model.*;
import com.musicservice.service.*;
import com.musicservice.service.Implementation.LogicalPlayService;
import com.musicservice.util.Validator;
import com.musicservice.view.IView;

import java.util.Map;

public class Controller {

    private final IView view;
    private final IUserService userService;
    private final IArtistService artistService;
    private final ITrackService trackService;
    private final IAlbumService albumService;
    private final IPlaylistService playlistService;
    private final ILibraryService libraryService;
    private final IPlayService playService;
    private final IArtistController artistController;

    private User currentUser;

    public Controller(IView view, IUserService userService, IArtistService artistService, ITrackService trackService, IAlbumService albumService, IPlaylistService playlistService, ILibraryService libraryService, IPlayService playService, IArtistController artistController) {
        this.view = view;
        this.userService = userService;
        this.artistService = artistService;
        this.trackService = trackService;
        this.albumService = albumService;
        this.playlistService = playlistService;
        this.libraryService = libraryService;
        this.playService = playService;
        this.artistController = artistController;
    }

    public void start() {
        while (true) {
            try {
                int choice = view.showMainMenu();
                switch (choice) {
                    case 1:
                        login();
                        break;
                    case 2:
                        register();
                        break;
                    case 0:
                        exit();
                        return;
                    default:
                        throw new InvalidInputException("Enter a Valid Choice.(0-2)");
                }
            } catch (InvalidInputException e) {
                view.showError(e);
            }
        }
    }

    private void exit() {
        view.show("Closing Ultraviolence Music Player....");
    }

    private void login() {
        try {
            String email = view.getInputString("Email: ");
            if (!Validator.isValidEmail(email)) {
                throw new InvalidInputException("Invalid Email Format");
            }

            String password = view.getInputString("Password: ");
            if (!Validator.isValidPassword(password)) {
                throw new InvalidInputException("Password must contain 8-20 characters with at least one uppercase, one lowercase, one digit and a special character.");
            }

            boolean success = userService.login(email, password);
            if (!success) {
                throw new AuthenticationException("Enter Valid Email and Password");
            }

            currentUser = userService.findUserByEmail(email);

            view.show("\nWelcome " + currentUser.getName());

            switch (currentUser.getRole()) {
                case USER:
                    userHome();
                    return;
                case ARTIST:
                    artistHome();
                    return;
                default:
                    throw new InvalidInputException("Invalid User Role");
            }
        } catch (InvalidInputException | AuthenticationException e) {
            view.showError(e);
        }
    }

    private void register() {
        try {
            String name = view.getInputString("Name: ");
            if (!Validator.isValidName(name)) {
                throw new InvalidInputException("Name must be capitalized and if you have last name, it also needs to be capitalized. \nNo Spaces at start and end of the name. \nKeep a space between first and last name. \nName should not contains numbers or special characters. ");
            }

            String email = view.getInputString("Email: ");
            if (!Validator.isValidEmail(email)) {
                throw new InvalidInputException("Invalid Email Format");
            }

            String password = view.getInputString("Password: ");
            if (!Validator.isValidPassword(password)) {
                throw new InvalidInputException("Password must contain 8-20 characters with at least one uppercase, one lowercase, one digit and a special character.");
            }

            User user = new User(name, email, password);

            boolean success = userService.addUser(user);
            if (!success) {
                throw new UnexpectedException("This email is already registered by a user.");
            }

        } catch (InvalidInputException | UnexpectedException e) {
            view.showError(e);
        }
    }

    private void userHome() {
        while (true) {
            try {
                int choice = view.showUserHomeMenu();
                switch (choice) {
                    case 1:
                        trackMenu();
                        break;
                    case 2:
                        artistMenu();
                        break;
                    case 3:
                        albumMenu();
                        break;
                    case 4:
                        playlistMenu();
                        break;
                    case 5:
                        libraryMenu();
                        break;
                    case 6:
                        genreMenu();
                        break;
                    case 7:
                        updateProfile();
                        break;
                    case 8:
                        deleteProfile();
                        return;
                    case 9:
                        becomeArtist();
                        return;
                    case 0:
                        currentUser = null;
                        view.show("Logged Out Successfully");
                        return;
                    default:
                        throw new InvalidChoiceException("Enter Valid Menu Choice (0-9)");
                }
            } catch (InvalidChoiceException e) {
                view.showError(e);
            }
        }
    }

    private void artistHome() {
        while (true) {
            try {
                int choice = view.showArtistHomeMenu();
                switch (choice) {
                    case 1:
                        trackMenu();
                        break;
                    case 2:
                        artistMenu();
                        break;
                    case 3:
                        albumMenu();
                        break;
                    case 4:
                        playlistMenu();
                        break;
                    case 5:
                        libraryMenu();
                        break;
                    case 6:
                        genreMenu();
                        break;
                    case 7:
                        updateProfile();
                        break;
                    case 8:
                        deleteProfile();
                        return;
                    case 9:
                        artistController.artistDashboard(currentUser);
                        break;
                    case 0:
                        currentUser = null;
                        view.show("Logged Out Successfully");
                        return;
                    default:
                        throw new InvalidChoiceException("Enter Valid Menu Choice (0-9)");
                }
            } catch (InvalidInputException e) {
                view.showError(e);
            }
        }
    }

    //1.track

    private void trackMenu() {
        while (true) {
            try {
                int choice = view.showTrackMenu();
                switch (choice) {
                    case 1:
                        Map<Integer, Track> tracks = trackService.getAllTracks();
                        showTracks(tracks);
                        break;
                    case 2:
                        searchTrack();
                        break;
                    case 0:
                        return;
                    default:
                        throw new InvalidChoiceException("Enter Valid Menu Choice (0-2)");
                }
            } catch (InvalidChoiceException e) {
                view.showError(e);
            }
        }
    }

    private void showTracks(Map<Integer, Track> tracks) {
        try {
            if (tracks.isEmpty()) {
                view.show("No Tracks Available");
                return;
            }
            for (Track track : tracks.values()) {
                Artist artist = artistService.getArtistById(track.getArtistId());
                String artistName = artist.getName();
                view.showTrack(track, artistName);
            }

            int id = view.getInputInt("Enter Track Id (0. Back): ");
            if (id == 0) {
                return;
            }

            trackOptions(id);
        } catch (InvalidInputException | ItemNotFoundException e) {
            view.show("Enter Valid Track Id from the above list.");
        }
    }

    private void trackOptions(int id) {
        while (true) {
            try {
                view.show(trackService.getTrackById(id));

                int choice = view.showTrackOptions();
                switch (choice) {
                    case 1:
                        playTrack(id);
                        break;
                    case 2:
                        if (!trackService.likeTrack(currentUser.getId(), id)) {
                            view.show("Already Liked");
                        }
                        break;
                    case 3:
                        if (!trackService.unlikeTrack(currentUser.getId(), id)) {
                            view.show("You did not liked this track");
                        }
                        break;
                    case 0:
                        return;
                    default:
                        throw new InvalidChoiceException("Enter Valid Menu Choice (0-3)");
                }
            } catch (InvalidChoiceException e) {
                view.showError(e);
            }
        }
    }

    private void playTrack(int id) {
        try {
            if (trackService.playTrack(id)) {
                view.show("Starts Playing");

                Track track = trackService.getTrackById(playService.getCurrentTrackId());
                view.show(track);
            }

            playbackMenu();
        } catch (MusicServiceException e) {
            view.showError(e);
        }

    }

    private void playbackMenu() {
        while (true) {
            try {
                int choice = view.showPlaybackMenu();
                switch (choice) {
                    case 1:
                        pause();
                        break;
                    case 2:
                        resume();
                        break;
                    case 3:
                        next();
                        break;
                    case 4:
                        previous();
                        break;
                    case 0:
                        stop();
                        return;
                    default:
                        throw new InvalidChoiceException("Enter Valid Choice (0-4) ");
                }
            } catch (InvalidChoiceException e) {
                view.showError(e);
            }
        }
    }

    private void pause() {
        if (playService.pause()) {
            view.show("Paused");
        } else {
            view.show("Nothing is Playing");
        }
    }

    private void resume() {
        if (playService.resume()) {
            view.show("Resuming");
        } else {
            view.show("Track is already Playing");
        }
    }

    private void next() {
        if (playService.next()) {
            view.show("Playing Next Track");
        } else {
            view.show("No Next Track");
        }
    }

    private void previous() {
        if (playService.previous()) {
            view.show("Previous Track Playing");
        } else {
            view.show("No Previous Track");
        }
    }

    private void stop() {
        if (playService.stop()) {
            view.show("Stopped Playing");
        } else {
            view.show("Nothing to Stop");
        }
    }

    private void searchTrack() {
        try {
            String name = view.getInputString("Enter Track Name: ");

            Map<Integer, Track> tracks = trackService.getTracksByName(name);
            if (tracks.isEmpty()) {
                view.show("No Tracks Found");
            }

            view.show(tracks);

            int id = view.getInputInt("Enter the Track Id (0. Back): ");
            if (id == 0) {
                return;
            }

            view.show(trackService.getTrackById(id));

            trackOptions(id);
        } catch (InvalidInputException | ItemNotFoundException e) {
            view.showError(e);
        }
    }

    //2.artist

    private void artistMenu() {
        while (true) {
            try {
                int choice = view.showArtistMenu();
                switch (choice) {
                    case 1:
                        showArtists();
                        break;
                    case 2:
                        searchArtist();
                        break;
                    case 0:
                        return;
                    default:
                        throw new InvalidChoiceException("Enter Valid Menu Choice (0-2) ");
                }
            } catch (InvalidChoiceException e) {
                view.showError(e);
            }
        }
    }

    private void showArtists() {
        try {
            Map<Integer, Artist> artists = artistService.getAllArtists();
            if (artists.isEmpty()) {
                view.show("No Artists Available");
                return;
            }

            view.show(artists);

            int id = view.getInputInt("Enter Artist Id (0. Back): ");
            if (id == 0) {
                return;
            }

            artistOptions(id);
        } catch (InvalidInputException | ItemNotFoundException e) {
            view.show("Enter Valid Artist Id from the above list.");
        }
    }

    private void artistOptions(int id) {
        while (true) {
            try {
                view.show(artistService.getArtistById(id));

                int choice = view.showArtistOptions();
                switch (choice) {
                    case 1:
                        playArtist(id);
                        break;
                    case 2:
                        Map<Integer, Track> tracks = trackService.getTracksByArtistId(id);
                        showTracks(tracks);
                        break;
                    case 3:
                        Map<Integer, Album> albums = albumService.getAlbumByArtist(id);
                        showAlbum(albums);
                        break;
                    case 0:
                        return;
                    default:
                        throw new InvalidChoiceException("Enter Valid Choice (0 or 1)");
                }
            } catch (InvalidChoiceException e) {
                view.showError(e);
            }
        }
    }

    private void playArtist(int id) {
        try {
            if (artistService.playArtist(id)) {
                view.show("Playing");
            }

            playbackMenu();
        } catch (MusicServiceException e) {
            view.showError(e);
        }
    }

    private void searchArtist() {
        try {
            String name = view.getInputString("Enter Artist Name: ");

            Map<Integer, Artist> artists = artistService.getArtistByName(name);
            if (artists.isEmpty()) {
                view.show("No Artist Found");
                return;
            }

            view.show(artists);

            int id = view.getInputInt("Enter Artist Id (0. Back): ");
            if (id == 0) {
                return;
            }

            view.show(artistService.getArtistById(id));

            artistOptions(id);
        } catch (InvalidInputException | ItemNotFoundException e) {
            view.showError(e);
        }
    }

    //3.Album

    private void albumMenu() {
        while (true) {
            try {
                int choice = view.showAlbumMenu();
                switch (choice) {
                    case 1:
                        Map<Integer, Album> albums = albumService.getAllAlbums();
                        showAlbum(albums);
                        break;
                    case 2:
                        searchAlbum();
                        break;
                    case 0:
                        return;
                    default:
                        throw new InvalidChoiceException("Enter Valid Menu Choice (0-2)");
                }
            } catch (InvalidChoiceException e) {
                view.showError(e);
            }
        }
    }

    private void showAlbum(Map<Integer, Album> albums) {
        try {
            if (albums.isEmpty()) {
                view.show("No Album Available");
                return;
            }

            view.show(albums);

            int id = view.getInputInt("Enter Album Id (0. Back): ");
            if (id == 0) {
                return;
            }

            albumOptions(id);
        } catch (InvalidInputException | ItemNotFoundException e) {
            view.show(e);
        }
    }

    private void albumOptions(int id) {
        while (true) {
            try {
                view.show(albumService.getAlbumById(id));

                int choice = view.showAlbumOptions();
                switch (choice) {
                    case 1:
                        playAlbum(id);
                        break;
                    case 2:
                        Map<Integer, Track> tracks = trackService.getTracksInAlbumId(id);
                        showTracks(tracks);
                        break;
                    case 0:
                        return;
                    default:
                        throw new InvalidChoiceException("Enter Valid Menu Choice (0-2)");
                }
            } catch (InvalidChoiceException e) {
                view.showError(e);
            }
        }
    }

    private void playAlbum(int id) {
        try {
            if (albumService.playAlbum(id)) {
                System.out.println("Playing Album");
            }

            playbackMenu();
        } catch (MusicServiceException e) {
            view.showError(e);
        }
    }

    private void searchAlbum() {
        try {
            String name = view.getInputString("Enter Album Name: ");

            Map<Integer, Album> albums = albumService.getAlbumByName(name);
            if (albums.isEmpty()) {
                view.show("No Album Found");
                return;
            }

            view.show(albums);

            int id = view.getInputInt("Enter Album Id (0. Back): ");
            if (id == 0) {
                return;
            }

            view.show(albumService.getAlbumById(id));

            albumOptions(id);
        } catch (InvalidInputException | ItemNotFoundException e) {
            view.showError(e);
        }
    }

    //4.Playlist

    private void playlistMenu() {
        while (true) {
            try {
                int choice = view.showPlaylistMenu();
                switch (choice) {
                    case 1:
                        showPlaylists();
                        break;
                    case 2:
                        createPlaylist();
                        break;
                    case 0:
                        return;
                    default:
                        throw new InvalidChoiceException("Enter Valid Menu Choice (0-2)");
                }
            } catch (InvalidChoiceException e) {
                view.showError(e);
            }
        }
    }

    private void showPlaylists() {
        try {
            Map<Integer, Playlist> playlists = playlistService.getAllPlaylists();
            if (playlists.isEmpty()) {
                view.show("No Playlists Available");
                return;
            }

            view.show(playlists);

            int id = view.getInputInt("Enter Playlist Id (0. Back): ");
            if (id == 0) {
                return;
            }

            playlistOptions(id);
        } catch (InvalidInputException | ItemNotFoundException e) {
            view.show(e);
        }
    }

    private void playlistOptions(int id) {
        while (true) {
            try {
                view.show(playlistService.getPlaylistById(id));
                view.show(playlistService.getTracksInPlaylist(id));

                int choice = view.showPlaylistOptions();
                switch (choice) {
                    case 1:
                        playPlaylist(id);
                        break;
                    case 2:
                        addTrackToPlaylist(id);
                        break;
                    case 3:
                        removeTrackFromPlaylist(id);
                        break;
                    case 4:
                        Playlist playlist = playlistService.getPlaylistById(id);

                        playlist.setName(view.getInputString("Enter New Playlist Name: "));

                        if (!playlistService.updatePlaylist(playlist)) {
                            throw new MusicServiceException("Can not able to Rename");
                        }

                        break;
                    case 5:
                        if (!playlistService.removePlaylist(id)) {
                            throw new MusicServiceException("Can not able to Delete");
                        }

                        return;
                    case 0:
                        return;
                    default:
                        throw new InvalidChoiceException("Enter Valid Menu Choice (0-5)");
                }
            } catch (MusicServiceException e) {
                view.showError(e);
            }
        }
    }

    private void playPlaylist(int id) {
        try {
            if (playlistService.playPlaylist(id)) {
                view.show("Playing Playlist");
            }

            playbackMenu();
        } catch (MusicServiceException e) {
            view.showError(e);
        }
    }

    private void addTrackToPlaylist(int id) {
        try {
            Map<Integer, Track> tracks = trackService.getAllTracks();
            if (tracks.isEmpty()) {
                view.show("No Tracks Available");
            }

            view.show(tracks);

            int trackId = view.getInputInt("Enter Track Id to Add: ");

            if (!playlistService.addTrackToPlaylist(id, trackId)) {
                throw new MusicServiceException("The Track is Already in the Playlist");
            }
        } catch (MusicServiceException e) {
            view.showError(e);
        }
    }

    private void removeTrackFromPlaylist(int id) {
        try {
            int trackId = view.getInputInt("Enter Track Id to Remove (0. Back): ");
            if (id == 0) {
                return;
            }

            if (!playlistService.removeTrackFromPlaylist(id, trackId)) {
                throw new ItemNotFoundException("Enter Track Id in the above list to Delete: ");
            }
        } catch (ItemNotFoundException e) {
            view.showError(e);
        }
    }

    private void createPlaylist() {
        try {
            String name = view.getInputString("Enter Playlist Name: ");
            if (!playlistService.addPlaylist(new Playlist(name, currentUser.getId()))) {
                throw new MusicServiceException("Can not able to create playlist");
            }
        } catch (MusicServiceException e) {
            view.showError(e);
        }
    }

    private void libraryMenu() {
        while (true) {
            try {
                int choice = view.showLibraryMenu();
                switch (choice) {
                    case 1:
                        Map<Integer, Track> tracks = libraryService.getLikedTrack(currentUser.getId());

                        showTracks(tracks);
                        break;
                    case 2:
                        showPlaylists();
                        break;
                    case 0:
                        return;
                    default:
                        throw new InvalidChoiceException("Enter Valid Menu Choice (0-2)");
                }
            } catch (InvalidChoiceException e) {
                view.showError(e);
            }
        }
    }

    private void genreMenu() {
        try {
            Genre[] genres = Genre.values();

            view.show(genres);

            int choice = view.getInputInt("Select Genre: ");
            if (choice <= 0 || choice > genres.length) {
                throw new InvalidChoiceException("Enter Valid Choice (1-" + genres.length + ")");
            }

            Genre genre = genres[choice - 1];

            Map<Integer, Track> tracks = trackService.getTracksByGenre(genre);
            showTracks(tracks);
        } catch (MusicServiceException e) {
            view.showError(e);
        }
    }

    private void updateProfile() {
        try {
            String name = view.getInputString("New Name: ");

            if (!name.isBlank()) {
                if (!Validator.isValidName(name)) {
                    throw new InvalidInputException("Enter Valid Name.");
                }
                currentUser.setName(name);
            }

            String email = view.getInputString("New Email: ");

            if (!email.isBlank()) {
                if (!Validator.isValidEmail(email)) {
                    throw new InvalidInputException("Enter Valid Email.");
                }
                currentUser.setEmail(email);
            }

            String password = view.getInputString("New Password: ");

            if (!password.isBlank()) {
                if (!Validator.isValidPassword(password)) {
                    throw new InvalidInputException("Enter Valid Password.");
                }
                currentUser.setPassword(password);
            }

            if (!userService.updateUser(currentUser)) {
                view.show("Profile update failed.");
                return;
            }

            view.show("Profile Updated Successfully");
        } catch (InvalidInputException e) {
            view.showError(e);
        }
    }

    private void deleteProfile() {
        try {
            String delete = view.getInputString("Enter Y to Delete.\nPress any other key to go Back: ");
            if (!delete.equalsIgnoreCase("y")) {
                return;
            }

            if (!userService.removeUser(currentUser.getId())) {
                throw new UnexpectedException("Can not able to Delete");
            }

            currentUser = null;
        } catch (UnexpectedException e) {
            view.showError(e);
        }
    }

    private void becomeArtist() {
        try {
            String name = view.getInputString("Stage Name: ");
            if (!Validator.isValidName(name)) {
                throw new InvalidInputException("Enter Valid Name");
            }

            String bio = view.getInputString("Bio: ");

            Artist artist = new Artist(name, bio);
            if (!artistService.addArtist(currentUser.getId(), artist)) {
                throw new UnexpectedException("Can not able to Create Artist Profile");
            }
        } catch (MusicServiceException e) {
            view.showError(e);
        }
    }

}