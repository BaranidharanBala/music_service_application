package com.musicservice.repository.DataBase;

import com.musicservice.model.Playlist;
import com.musicservice.repository.IPlaylistRepository;
import com.musicservice.util.JDBCConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBPlaylistRepository implements IPlaylistRepository {

    private static DBPlaylistRepository instance;

    private DBPlaylistRepository() {
    }

    public static DBPlaylistRepository getInstance() {
        if (instance == null) {
            instance = new DBPlaylistRepository();
        }
        return instance;
    }

    @Override
    public void save(Playlist playlist) {
        String playlistSql = """
                INSERT INTO playlists (name, "userId")
                VALUES (?, ?)
                """;

        String trackSql = """
                INSERT INTO playlist_tracks ("playlistId", "trackId")
                VALUES (?, ?)
                """;

        try (Connection connection = JDBCConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement playlistStatement = connection.prepareStatement(playlistSql, Statement.RETURN_GENERATED_KEYS)) {
                playlistStatement.setString(1, playlist.getName());
                playlistStatement.setInt(2, playlist.getUserId());
                playlistStatement.executeUpdate();

                try (ResultSet resultSet = playlistStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        playlist.setId(resultSet.getInt(1));
                    }
                }
            }

            try (PreparedStatement trackStatement = connection.prepareStatement(trackSql)) {
                for (Integer trackId : playlist.getTrackIds()) {
                    trackStatement.setInt(1, playlist.getId());
                    trackStatement.setInt(2, trackId);
                    trackStatement.addBatch();
                }
                trackStatement.executeBatch();
            }
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Playlist playlist) {
        String playlistSql = """
                UPDATE playlists
                SET name = ?,
                    "userId" = ?
                WHERE id = ?
                """;

        String deleteTracksSql = """
                DELETE FROM playlist_tracks
                WHERE "playlistId" = ?
                """;

        String insertTrackSql = """
                INSERT INTO playlist_tracks ("playlistId", "trackId")
                VALUES (?, ?)
                """;

        try (Connection connection = JDBCConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(playlistSql)) {
                statement.setString(1, playlist.getName());
                statement.setInt(2, playlist.getUserId());
                statement.setInt(3, playlist.getId());
                statement.executeUpdate();
            }
            /*
             * Remove the old track relationships.
             */
            try (PreparedStatement statement = connection.prepareStatement(deleteTracksSql)) {
                statement.setInt(1, playlist.getId());
                statement.executeUpdate();
            }
            /*
             * Insert the current track relationships.
             */
            try (PreparedStatement statement = connection.prepareStatement(insertTrackSql)) {
                for (Integer trackId : playlist.getTrackIds()) {
                    statement.setInt(1, playlist.getId());
                    statement.setInt(2, trackId);
                    statement.addBatch();
                }
                statement.executeBatch();
            }
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = """
                DELETE FROM playlists
                WHERE id = ?
                """;

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Playlist findById(int id) {
        String playlistSql = """
                SELECT id, name, "userId"
                FROM playlists
                WHERE id = ?
                """;

        String trackSql = """
                SELECT "trackId"
                FROM playlist_tracks
                WHERE "playlistId" = ?
                """;

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement playlistStatement = connection.prepareStatement(playlistSql)) {
            playlistStatement.setInt(1, id);

            try (ResultSet resultSet = playlistStatement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }

                Playlist playlist = new Playlist(resultSet.getString("name"), resultSet.getInt("userId"));
                playlist.setId(resultSet.getInt("id"));

                try (PreparedStatement trackStatement = connection.prepareStatement(trackSql)) {
                    trackStatement.setInt(1, id);

                    try (ResultSet trackResultSet = trackStatement.executeQuery()) {
                        List<Integer> trackIds = new ArrayList<>();

                        while (trackResultSet.next()) {
                            trackIds.add(trackResultSet.getInt("trackId"));
                        }
                        playlist.setTrackIds(trackIds);
                    }
                }

                return playlist;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<Integer, Playlist> findByName(String name) {
        String sql = """
                SELECT id, name, "userId"
                FROM playlists
                WHERE name ILIKE ?
                """;

        Map<Integer, Playlist> result = new HashMap<>();

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + name + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Playlist playlist = mapPlaylist(connection, resultSet);

                    result.put(playlist.getId(), playlist);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public Map<Integer, Playlist> findByUserId(int userId) {
        String sql = """
                SELECT id, name, "userId"
                FROM playlists
                WHERE "userId" = ?
                """;

        Map<Integer, Playlist> result = new HashMap<>();

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Playlist playlist = mapPlaylist(connection, resultSet);

                    result.put(playlist.getId(), playlist);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public Map<Integer, Playlist> findAll() {
        String sql = """
                SELECT id, name, "userId"
                FROM playlists
                """;

        Map<Integer, Playlist> result = new HashMap<>();

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Playlist playlist = mapPlaylist(connection, resultSet);

                result.put(playlist.getId(), playlist);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private Playlist mapPlaylist(Connection connection, ResultSet resultSet) throws SQLException {
        Playlist playlist = new Playlist(resultSet.getString("name"), resultSet.getInt("userId"));
        playlist.setId(resultSet.getInt("id"));

        String sql = """
                SELECT "trackId"
                FROM playlist_tracks
                WHERE "playlistId" = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, playlist.getId());

            try (ResultSet trackResultSet = statement.executeQuery()) {
                List<Integer> trackIds = new ArrayList<>();

                while (trackResultSet.next()) {
                    trackIds.add(trackResultSet.getInt("trackId"));
                }
                playlist.setTrackIds(trackIds);
            }
        }

        return playlist;
    }
}