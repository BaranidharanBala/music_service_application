package com.musicservice.repository.DataBase;

import com.musicservice.model.Library;
import com.musicservice.repository.ILibraryRepository;
import com.musicservice.util.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBLibraryRepository implements ILibraryRepository {

    private static DBLibraryRepository instance;

    private DBLibraryRepository() {
    }

    public static DBLibraryRepository getInstance() {
        if (instance == null) {
            instance = new DBLibraryRepository();
        }
        return instance;
    }

    @Override
    public void save(Library library) {
        String librarySql = """
                INSERT INTO libraries ("userId")
                VALUES (?)
                """;

        String trackSql = """
                INSERT INTO library_tracks ("userId", "trackId")
                VALUES (?, ?)
                """;

        String playlistSql = """
                INSERT INTO library_playlists ("userId", "playlistId")
                VALUES (?, ?)
                """;

        try (Connection connection = JDBCConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(librarySql)) {
                statement.setInt(1, library.getUserId());
                statement.executeUpdate();
            }
            saveTracks(connection, trackSql, library);
            savePlaylists(connection, playlistSql, library);

            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Library library) {
        String deleteTracksSql = """
                DELETE FROM library_tracks
                WHERE "userId" = ?
                """;

        String deletePlaylistsSql = """
                DELETE FROM library_playlists
                WHERE "userId" = ?
                """;

        String trackSql = """
                INSERT INTO library_tracks ("userId", "trackId")
                VALUES (?, ?)
                """;

        String playlistSql = """
                INSERT INTO library_playlists ("userId", "playlistId")
                VALUES (?, ?)
                """;

        try (Connection connection = JDBCConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(deleteTracksSql)) {
                statement.setInt(1, library.getUserId());
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(deletePlaylistsSql)) {
                statement.setInt(1, library.getUserId());
                statement.executeUpdate();
            }
            saveTracks(connection, trackSql, library);
            savePlaylists(connection, playlistSql, library);

            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int userId) {
        String sql = """
                DELETE FROM libraries
                WHERE "userId" = ?
                """;

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Library findByUserId(int userId) {
        String librarySql = """
                SELECT "userId"
                FROM libraries
                WHERE "userId" = ?
                """;

        String trackSql = """
                SELECT "trackId"
                FROM library_tracks
                WHERE "userId" = ?
                """;

        String playlistSql = """
                SELECT "playlistId"
                FROM library_playlists
                WHERE "userId" = ?
                """;

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement libraryStatement = connection.prepareStatement(librarySql)) {
            libraryStatement.setInt(1, userId);

            try (ResultSet resultSet = libraryStatement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                Library library = new Library(resultSet.getInt("userId"));

                try (PreparedStatement statement = connection.prepareStatement(trackSql)) {
                    statement.setInt(1, userId);

                    try (ResultSet trackResultSet = statement.executeQuery()) {
                        List<Integer> trackIds = new ArrayList<>();

                        while (trackResultSet.next()) {
                            trackIds.add(trackResultSet.getInt("trackId"));
                        }
                        library.setLikedTrackIds(trackIds);
                    }
                }

                try (PreparedStatement statement = connection.prepareStatement(playlistSql)) {
                    statement.setInt(1, userId);

                    try (ResultSet playlistResultSet = statement.executeQuery()) {
                        List<Integer> playlistIds = new ArrayList<>();

                        while (playlistResultSet.next()) {
                            playlistIds.add(playlistResultSet.getInt("playlistId"));
                        }
                        library.setPlaylistIds(playlistIds);
                    }
                }

                return library;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveTracks(Connection connection, String sql, Library library) throws SQLException {
        if (library.getLikedTrackIds() == null) {
            return;
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Integer trackId : library.getLikedTrackIds()) {
                statement.setInt(1, library.getUserId());
                statement.setInt(2, trackId);
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private void savePlaylists(Connection connection, String sql, Library library) throws SQLException {
        if (library.getPlaylistIds() == null) {
            return;
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Integer playlistId : library.getPlaylistIds()) {
                statement.setInt(1, library.getUserId());
                statement.setInt(2, playlistId);
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }
}