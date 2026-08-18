package com.musicservice.controller.Implementation;

import com.musicservice.controller.IArtistController;
import com.musicservice.enums.Genre;
import com.musicservice.exception.InvalidChoiceException;
import com.musicservice.exception.InvalidInputException;
import com.musicservice.exception.ItemNotFoundException;
import com.musicservice.exception.UnexpectedException;
import com.musicservice.model.Album;
import com.musicservice.model.Artist;
import com.musicservice.model.Track;
import com.musicservice.model.User;
import com.musicservice.service.IAlbumService;
import com.musicservice.service.IArtistService;
import com.musicservice.service.ITrackService;
import com.musicservice.util.Validator;
import com.musicservice.view.IView;

import java.util.Map;

public class ArtistController implements IArtistController {

    private static ArtistController instance;
    private final IView view;
    private final IArtistService artistService;
    private final ITrackService trackService;
    private final IAlbumService albumService;

    public ArtistController(IView view, IArtistService artistService, ITrackService trackService, IAlbumService albumService) {
        this.view = view;
        this.artistService = artistService;
        this.trackService = trackService;
        this.albumService = albumService;
    }

    public static ArtistController getInstance(IView view, IArtistService artistService, ITrackService trackService, IAlbumService albumService) {
        if (instance == null) {
            instance = new ArtistController(view, artistService, trackService, albumService);
        }
        return instance;
    }

    @Override
    public void artistDashboard(User currentUser) {
        Artist artist = artistService.getArtistByUserId(currentUser.getId());
        int id = artist.getId();
        
        while (true) {
            try {
                int choice = view.showArtistDashboard();
                switch (choice) {
                    case 1:
                        trackManagement(id);
                        break;
                    case 2:
                        albumManagement(id);
                        break;
                    case 3:
                        updateArtistProfile(id);
                        break;
                    case 4:
                        deleteArtistProfile(id);
                        return;
                    case 0:
                        return;
                    default:
                        throw new InvalidChoiceException("Enter Valid Choice (0-4)");
                }
            } catch (InvalidChoiceException e) {
                view.showError(e);
            }
        }
    }

    private void trackManagement(int artistId) {
        Map<Integer, Track> tracks = trackService.getTracksByArtistId(artistId);
        view.show(tracks);
        
        while (true) {
            try {
                int choice = view.showTrackManagementMenu();
                switch (choice) {
                    case 1:
                        releaseTrack(artistId);
                        break;
                    case 2:
                        deleteTrack(artistId);
                        break;
                    case 0:
                        return;
                    default:
                        throw new InvalidChoiceException("Enter Valid Choice (0-2)");
                }
            } catch (InvalidChoiceException e) {
                view.showError(e);
            }
        }
    }

    private void releaseTrack(int artistId) {
        try {
            String name = view.getInputString("Track Title: ");
            if (name.isBlank()) {
                throw new InvalidInputException("Track title cannot be empty.");
            }
            
            String path = view.getInputString("File Path: ");
            if (path.isBlank()) {
                throw new InvalidInputException("Track path cannot be empty.");
            }
            
            int duration = view.getInputInt("Duration (in seconds): ");
            
            Genre[] genres = Genre.values();
            view.show(genres);
            
            int choice = view.getInputInt("Select Genre: ");
            if (choice < 1 || choice > genres.length) {
                throw new InvalidChoiceException("Enter Valid Genre Choice.");
            }
            
            Genre genre = genres[choice - 1];
            
            Track track = new Track(name, path, artistId, genre, duration);
            if (!trackService.addTrack(track)) {
                throw new UnexpectedException("Can not able to release Track.");
            }
            
            view.show("Track released successfully.");
        } catch (InvalidInputException | InvalidChoiceException | UnexpectedException e) {
            view.showError(e);
        }
    }

    private void deleteTrack(int artistId) {
        try {
            Map<Integer, Track> tracks = trackService.getTracksByArtistId(artistId);
            if (tracks.isEmpty()) {
                view.show("No Tracks Available.");
                return;
            }
            
            view.show(tracks);
            
            int id = view.getInputInt("Enter Track Id (0. Back): ");
            if (id == 0) {
                return;
            }
            
            Track track = trackService.getTrackById(id);
            if (track == null) {
                throw new ItemNotFoundException("Enter Track Id from the above list.");
            }
            
            String delete = view.getInputString("Delete " + track.getName() + " ?\nEnter Y to Delete.\nPress any other key to go Back: ");
            if (!delete.equalsIgnoreCase("Y")) {
                return;
            }
            
            trackService.removeTrack(id);
            
            view.show("Track deleted successfully.");
        } catch (InvalidInputException | ItemNotFoundException e) {
            view.showError(e);
        }
    }

    private void albumManagement(int artistId) {
        while (true) {
            try {
                Map<Integer, Album> albums = albumService.getAlbumByArtist(artistId);
                view.show(albums);
                
                int choice = view.showAlbumManagementMenu();
                switch (choice) {
                    case 1:
                        openAlbums(artistId);
                        break;
                    case 2:
                        releaseAlbum(artistId);
                        break;
                    case 0:
                        return;
                    default:
                        throw new InvalidChoiceException("Enter Valid Choice (0-3)");
                }
            } catch (InvalidChoiceException e) {
                view.showError(e);
            }
        }
    }

    private void releaseAlbum(int artistId) {
        try {
            String name = view.getInputString("Enter Album Name: ");
            if (name.isBlank()) {
                throw new InvalidInputException("Album name cannot be empty.");
            }
            
            view.show(Genre.values());
            
            int choice = view.getInputInt("Select Genre: ");
            
            Genre[] genres = Genre.values();
            if (choice < 1 || choice > genres.length) {
                throw new InvalidChoiceException("Enter Valid Genre Choice.");
            }
            
            Genre genre = genres[choice - 1];
            
            Album album = new Album(name, artistId, genre);
            if (!albumService.addAlbum(album)) {
                throw new UnexpectedException("Unable to release Album");
            }
            
            view.show("Album released successfully.");
        } catch (InvalidInputException | InvalidChoiceException e) {
            view.showError(e);
        }
    }

    private void openAlbums(int artistId) {
        try {
            Map<Integer, Album> albums = albumService.getAlbumByArtist(artistId);
            if (albums.isEmpty()) {
                view.show("No Albums Available.");
                return;
            }
            
            view.show(albums);
            
            int id = view.getInputInt("Enter Album Id (0. Back): ");
            if (id == 0) {
                return;
            }
            
            if (!albums.containsKey(id)) {
                throw new ItemNotFoundException("Enter Valid Album Id from the above list.");
            }
            
            albumManagementOptions(id, artistId);
        } catch (InvalidInputException | ItemNotFoundException e) {
            view.showError(e);
        }
    }

    private void albumManagementOptions(int id, int artistId) {
        while (true) {
            try {
                int choice = view.showAlbumManagementOptions();
                switch (choice) {
                    case 1:
                        addTrackToAlbum(id, artistId);
                        break;
                    case 2:
                        removeTrackFromAlbum(id, artistId);
                        break;
                    case 3:
                        updateAlbum(id, artistId);
                        break;
                    case 0:
                        return;
                    default:
                        throw new InvalidChoiceException("Enter Valid Choice (0-3)");
                }

            } catch (InvalidChoiceException e) {
                view.showError(e);
            }
        }
    }

    private void addTrackToAlbum(int artistId, int albumId) {
        try {
            Map<Integer, Track> tracks = trackService.getTracksByArtistId(artistId);
            if (tracks.isEmpty()) {
                view.show("No Tracks Available.");
                return;
            }
            
            view.show(tracks);
            
            int trackId = view.getInputInt("Enter Track Id (0. Back): ");
            if (trackId == 0) {
                return;
            }
            
            if (!tracks.containsKey(trackId)) {
                throw new ItemNotFoundException("Enter Track Id from the above list.");
            }
            
            if (!albumService.addTrackToAlbum(albumId, trackId, artistId)) {
                throw new UnexpectedException("Can not able to add Track");
            }
            
            view.show("Track added to album successfully.");
        } catch (InvalidInputException | UnexpectedException | ItemNotFoundException e) {
            view.showError(e);
        }
    }

    private void removeTrackFromAlbum(int id, int artistId) {
        try {
            Map<Integer, Track> tracks = trackService.getTracksInAlbumId(id);
            if (tracks.isEmpty()) {
                view.show("No Tracks Available in this Album.");
                return;
            }
            
            view.show(tracks);
            
            int trackId = view.getInputInt("Enter Track Id (0. Back): ");
            if (trackId == 0) {
                return;
            }
            
            if (!tracks.containsKey(trackId)) {
                throw new ItemNotFoundException("Enter Track Id from the above list.");
            }
            
            if (!albumService.removeTrackFromAlbum(id, trackId, artistId)) {
                throw new UnexpectedException("Can not able to remove Track");
            }
            
            view.show("Track removed from album successfully.");
        } catch (InvalidInputException | UnexpectedException | ItemNotFoundException e) {
            view.showError(e);
        }
    }

    private void updateAlbum(int id, int artistId) {
        try {
            Album album = albumService.getAlbumById(id);
            if (album == null || album.getArtistId() != artistId) {
                throw new ItemNotFoundException("Album not found.");
            }
            
            String name = view.getInputString("Enter New Album Name (blank to keep current): ");
            if (!name.isBlank()) {
                album.setName(name);
            }
            
            String changeGenre = view.getInputString("Change Genre? Enter Y to change.\nPress any other key to go Back: ");
            if (!changeGenre.equalsIgnoreCase("Y")) {
                return;
            }
            
            view.show(Genre.values());
            
            int choice = view.getInputInt("Select Genre: ");
            Genre[] genres = Genre.values();
            if (choice < 1 || choice > genres.length) {
                throw new InvalidChoiceException("Enter Valid Genre Choice.");
            }
            
            album.setGenre(genres[choice - 1]);
            if (!albumService.updateAlbum(album)) {
                view.show("Unable to update album.");
                return;
            }
            view.show("Album updated successfully.");

        } catch (InvalidInputException | InvalidChoiceException | ItemNotFoundException e) {
            view.showError(e);
        }
    }

    private void updateArtistProfile(int id) {
        try {
            Artist artist = artistService.getArtistById(id);

            String name = view.getInputString("Enter New Stage Name (blank to keep current): ");

            if (!name.isBlank()) {
                if(!Validator.isValidName(name)) {
                    throw new InvalidInputException("Enter Valid Name format");
                }
                artist.setName(name);
            }

            String bio = view.getInputString("Enter New Bio (blank to keep current): ");
            if (!bio.isBlank()) {
                artist.setBio(bio);
            }

            if (!artistService.updateArtist(artist)) {
                throw new UnexpectedException("Cannot able to update profile");
            }

            view.show("Artist profile updated successfully.");
        } catch (InvalidInputException e) {
            view.showError(e);
        }
    }

    private void deleteArtistProfile(int id) {
        try {
            Artist artist = artistService.getArtistById(id);

            String confirmation = view.getInputString("Delete artist profile \"" + artist.getName() + "\"? Enter Y to Delete\" Press any other key to Back: ");
            if (!confirmation.equalsIgnoreCase("Y")) {
                return;
            }

            if (!artistService.removeArtist(id)) {
                throw new UnexpectedException("Can not able to delete");
            }

            view.show("Artist profile deleted successfully.");
        } catch (InvalidInputException | UnexpectedException e) {
            view.showError(e);
        }
    }

}