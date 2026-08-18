package com.musicservice.repository.DataBase;

import com.musicservice.enums.Genre;
import com.musicservice.enums.PlaybackStatus;
import com.musicservice.model.Track;
import com.musicservice.repository.ITrackRepository;
import com.musicservice.util.JDBCConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DBTrackRepository implements ITrackRepository {

    private static DBTrackRepository instance;

    private DBTrackRepository() {
    }

    public static DBTrackRepository getInstance() {
        if (instance == null) {
            instance = new DBTrackRepository();
        }
        return instance;
    }

    @Override
    public void save(Track track) {
        String sql = """
                INSERT INTO tracks
                (name, path, "artistId", "albumId", genre, duration, "playbackStatus")
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, track.getName());
            statement.setString(2, track.getPath());
            statement.setInt(3, track.getArtistId());

            if (track.getAlbumId() == null) {
                statement.setNull(4, Types.INTEGER);
            } else {
                statement.setInt(4, track.getAlbumId());
            }

            statement.setString(5, track.getGenre().name());
            statement.setDouble(6, track.getDuration());
            statement.setString(7, track.getPlaybackStatus().name());
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    track.setId(resultSet.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Track track) {
        String sql = """
                UPDATE tracks
                SET name = ?,
                    path = ?,
                    "artistId" = ?,
                    "albumId" = ?,
                    genre = ?,
                    duration = ?,
                    "playbackStatus" = ?
                WHERE id = ?
                """;

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, track.getName());
            statement.setString(2, track.getPath());
            statement.setInt(3, track.getArtistId());

            if (track.getAlbumId() == null) {
                statement.setNull(4, Types.INTEGER);
            } else {
                statement.setInt(4, track.getAlbumId());
            }

            statement.setString(5, track.getGenre().name());
            statement.setDouble(6, track.getDuration());
            statement.setString(7, track.getPlaybackStatus().name());
            statement.setInt(8, track.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = """
                DELETE FROM tracks
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
    public Track findById(int id) {
        String sql = """
                SELECT id,
                       name,
                       path,
                       "artistId",
                       "albumId",
                       genre,
                       duration,
                       "playbackStatus"
                FROM tracks
                WHERE id = ?
                """;

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapTrack(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Map<Integer, Track> findByArtist(int artistId) {
        String sql = """
                SELECT id,
                       name,
                       path,
                       "artistId",
                       "albumId",
                       genre,
                       duration,
                       "playbackStatus"
                FROM tracks
                WHERE "artistId" = ?
                """;

        return findTracks(sql, artistId);
    }

    @Override
    public Map<Integer, Track> findByAlbum(int albumId) {
        String sql = """
                SELECT id,
                       name,
                       path,
                       "artistId",
                       "albumId",
                       genre,
                       duration,
                       "playbackStatus"
                FROM tracks
                WHERE "albumId" = ?
                """;

        return findTracks(sql, albumId);
    }

    @Override
    public Map<Integer, Track> findByGenre(Genre genre) {
        String sql = """
                SELECT id,
                       name,
                       path,
                       "artistId",
                       "albumId",
                       genre,
                       duration,
                       "playbackStatus"
                FROM tracks
                WHERE genre = ?
                """;

        Map<Integer, Track> result = new HashMap<>();

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, genre.name());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Track track = mapTrack(resultSet);

                    result.put(track.getId(), track);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public Map<Integer, Track> findAll() {
        String sql = """
                SELECT id,
                       name,
                       path,
                       "artistId",
                       "albumId",
                       genre,
                       duration,
                       "playbackStatus"
                FROM tracks
                """;

        Map<Integer, Track> result = new HashMap<>();

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Track track = mapTrack(resultSet);

                result.put(track.getId(), track);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public Map<Integer, Track> search(String name) {
        String sql = """
                SELECT id,
                       name,
                       path,
                       "artistId",
                       "albumId",
                       genre,
                       duration,
                       "playbackStatus"
                FROM tracks
                WHERE name ILIKE ?
                """;

        Map<Integer, Track> result = new HashMap<>();

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + name + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Track track = mapTrack(resultSet);

                    result.put(track.getId(), track);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private Map<Integer, Track> findTracks(String sql, int id) {

        Map<Integer, Track> result = new HashMap<>();

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Track track = mapTrack(resultSet);

                    result.put(track.getId(), track);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private Track mapTrack(ResultSet resultSet) throws SQLException {

        Track track = new Track();

        track.setId(resultSet.getInt("id"));
        track.setName(resultSet.getString("name"));
        track.setPath(resultSet.getString("path"));
        track.setArtistId(resultSet.getInt("artistId"));

        int albumId = resultSet.getInt("albumId");

        if (resultSet.wasNull()) {
            track.setAlbumId(null);
        } else {
            track.setAlbumId(albumId);
        }

        track.setGenre(Genre.valueOf(resultSet.getString("genre")));
        track.setDuration(resultSet.getDouble("duration"));
        track.setPlaybackStatus(PlaybackStatus.valueOf(resultSet.getString("playbackStatus")));

        return track;
    }

}