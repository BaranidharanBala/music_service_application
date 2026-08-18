package com.musicservice.view;

import com.musicservice.enums.Genre;
import com.musicservice.exception.MusicServiceException;
import com.musicservice.model.Track;
import com.musicservice.util.ScannerUtil;

import java.util.Map;
import java.util.Scanner;

public class View implements IView {

    private static View instance;
    private final Scanner scanner = ScannerUtil.getScanner();

    public static View getInstance() {
        if (instance == null) {
            instance = new View();
        }
        return instance;
    }

    //input returns to controller

    public int getInputInt(String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Enter Integer");
            }
        }
    }

    public String getInputString(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    public void showError(MusicServiceException e) {
        System.out.println("\n[ERROR]: " + e.getMessage());
    }

    //Menu

    public int showMainMenu() {
        System.out.println("\n=== ULTRAVIOLENCE MUSIC PLAYER ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("0. Exit");

        return getInputInt("Enter choice: ");
    }


    public int showUserHomeMenu() {
        System.out.println("\n==HOME PAGE==");
        System.out.println("1. Tracks");
        System.out.println("2. Artists");
        System.out.println("3. Albums");
        System.out.println("4. Playlists");
        System.out.println("5. Library");
        System.out.println("6. Genres");
        System.out.println("7. Update Profile");
        System.out.println("8. Delete Profile");
        System.out.println("9. Become Artist");
        System.out.println("0. Logout");

        return getInputInt("Select Menu: ");
    }

    public int showArtistHomeMenu() {
        System.out.println("\n==HOME PAGE==");
        System.out.println("1. Tracks");
        System.out.println("2. Artists");
        System.out.println("3. Albums");
        System.out.println("4. Playlists");
        System.out.println("5. Library");
        System.out.println("6. Genres");
        System.out.println("7. Update Profile");
        System.out.println("8. Delete Profile");
        System.out.println("9. Artist Dashboard");
        System.out.println("0. Logout");

        return getInputInt("Select Menu: ");
    }

    public int showTrackMenu() {
        System.out.println("\n========== TRACKS ==========");
        System.out.println("1. Show Tracks");
        System.out.println("2. Search Track");
        System.out.println("0. Back");

        return getInputInt("Select Track Menu: ");
    }

    public int showTrackOptions() {
        System.out.println("\n====== TRACK OPTIONS ======");
        System.out.println("1. Play");
        System.out.println("2. Like");
        System.out.println("3. Unlike");
        System.out.println("0. Back");

        return getInputInt("Select Track Action: ");
    }

    public int showArtistMenu() {
        System.out.println("\n========== ARTISTS ==========");
        System.out.println("1. Show Artists");
        System.out.println("2. Search Artist");
        System.out.println("0. Back");

        return getInputInt("Select Artist Menu: ");
    }

    public int showArtistOptions() {
        System.out.println("\n====== ARTIST OPTIONS ======");
        System.out.println("1. Play Artist");
        System.out.println("2. Show Artist's Tracks");
        System.out.println("3. Show Artist's Albums");
        System.out.println("0. Back");

        return getInputInt("Select Artist Action: ");
    }

    public int showAlbumMenu() {
        System.out.println("\n========== ALBUMS ==========");
        System.out.println("1. Show Albums");
        System.out.println("2. Search Album");
        System.out.println("0. Back");

        return getInputInt("Select Album Menu: ");
    }

    public int showAlbumOptions() {
        System.out.println("\n====== ALBUM OPTIONS ======");
        System.out.println("1. Play Album");
        System.out.println("2. Show Tracks in the Album");
        System.out.println("0. Back");

        return getInputInt("Select Album Action: ");
    }

    public int showPlaylistMenu() {
        System.out.println("\n======== PLAYLISTS ========");
        System.out.println("1. Show Playlists");
        System.out.println("2. Create Playlist");
        System.out.println("0. Back");

        return getInputInt("Select Playlist Menu: ");
    }

    public int showPlaylistOptions() {
        System.out.println("\n===== PLAYLIST OPTIONS =====");
        System.out.println("1. Play Playlist");
        System.out.println("2. Add Track");
        System.out.println("3. Remove Track");
        System.out.println("4. Rename Playlist");
        System.out.println("5. Delete Playlist");
        System.out.println("0. Back");

        return getInputInt("Select Playlist Option: ");
    }

    public int showLibraryMenu() {
        System.out.println("\n====== LIBRARY ======");
        System.out.println("1. Liked Tracks");
        System.out.println("2. My Playlists");
        System.out.println("0. Back");

        return getInputInt("Select Library Menu: ");
    }

    public int showArtistDashboard() {
        System.out.println("\n====== ARTIST DASHBOARD ======");
        System.out.println("1. Track Management");
        System.out.println("2. Album Management");
        System.out.println("3. Update Artist Profile");
        System.out.println("4. Delete Artist Profile");
        System.out.println("0. Back");

        return getInputInt("Enter Choice: ");
    }

    public int showTrackManagementMenu() {
        System.out.println("\n====== TRACK MANAGEMENT ======");
        System.out.println("1. Release Track");
        System.out.println("2. Delete Track");
        System.out.println("0. Back");

        return getInputInt("Enter Choice: ");
    }

    public int showAlbumManagementMenu() {
        System.out.println("\n====== ALBUM MANAGEMENT ======");
        System.out.println("1. Open Album Option");
        System.out.println("2. Release Album");
        System.out.println("0. Back");

        return getInputInt("Enter Choice: ");
    }

    public int showAlbumManagementOptions() {
        System.out.println("\n====== ALBUM MANAGEMENT OPTIONS ======");
        System.out.println("1. Add Track to Album");
        System.out.println("2. Remove Track from Album");
        System.out.println("3. Update Album");
        System.out.println("0. Back");

        return getInputInt("Enter Choice: ");
    }

    public int showPlaybackMenu() {
        System.out.println("\n====== PLAYBACK ======");
        System.out.println("1. Pause");
        System.out.println("2. Resume");
        System.out.println("3. Next");
        System.out.println("4. Previous");
        System.out.println("0. Stop");

        return getInputInt("Enter Choice: ");
    }

    //Show

    public void show(Object object) {
        System.out.println();
        System.out.println(object);
    }

    public void show(Map<?, ?> map) {
        System.out.println();
        if (map == null || map.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        for (Object object : map.values()) {
            System.out.println(object);
        }
    }

    public void show(String message) {
        System.out.println();
        System.out.println(message);
    }

    public void show(Genre[] genres) {
        System.out.println();
        for (int i = 0; i < genres.length; i++) {
            System.out.println((i + 1) + ". " + genres[i]);
        }
    }

    public void showTrack(Track track, String artist) {
        System.out.printf("%-5d %-30s %-25s %-15s %-8.3f%n", track.getId(), track.getName(), artist, track.getGenre(), track.getDuration());
    }

}