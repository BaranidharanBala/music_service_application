package com.musicservice.repository.DataBase;

import com.musicservice.enums.Genre;
import com.musicservice.model.Album;
import com.musicservice.repository.IAlbumRepository;
import com.musicservice.util.JDBCConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DBAlbumRepository implements IAlbumRepository {

    private static DBAlbumRepository instance;

    private DBAlbumRepository() {
    }

    public static DBAlbumRepository getInstance() {
        if (instance == null) {
            instance = new DBAlbumRepository();
        }
        return instance;
    }

    @Override
    public void save(Album album) {
        String sql = """
                INSERT INTO albums (name, "artistId", genre)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, album.getName());
            statement.setInt(2, album.getArtistId());
            statement.setString(3, album.getGenre().name());
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    album.setId(resultSet.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Album album) {
        String sql = """
                UPDATE albums
                SET name = ?,
                    "artistId" = ?,
                    genre = ?
                WHERE id = ?
                """;

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, album.getName());
            statement.setInt(2, album.getArtistId());
            statement.setString(3, album.getGenre().name());
            statement.setInt(4, album.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = """
                DELETE FROM albums
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
    public Album findById(int id) {
        String sql = """
                SELECT id, name, "artistId", genre
                FROM albums
                WHERE id = ?
                """;

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapAlbum(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Map<Integer, Album> search(String name) {
        String sql = """
                SELECT id, name, "artistId", genre
                FROM albums
                WHERE name ILIKE ?
                """;

        Map<Integer, Album> result = new HashMap<>();

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + name + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Album album = mapAlbum(resultSet);

                    result.put(album.getId(), album);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public Map<Integer, Album> findByArtist(int artistId) {

        String sql = """
                SELECT id, name, "artistId", genre
                FROM albums
                WHERE "artistId" = ?
                """;

        Map<Integer, Album> result = new HashMap<>();

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, artistId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Album album = mapAlbum(resultSet);

                    result.put(album.getId(), album);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public Map<Integer, Album> findByGenre(Genre genre) {

        String sql = """
                SELECT id, name, "artistId", genre
                FROM albums
                WHERE genre = ?
                """;

        Map<Integer, Album> result = new HashMap<>();

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, genre.name());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Album album = mapAlbum(resultSet);

                    result.put(album.getId(), album);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public Map<Integer, Album> findAll() {

        String sql = """
                SELECT id, name, "artistId", genre
                FROM albums
                """;

        Map<Integer, Album> result = new HashMap<>();

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Album album = mapAlbum(resultSet);

                result.put(album.getId(), album);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private Album mapAlbum(ResultSet resultSet) throws SQLException {
        Album album = new Album();

        album.setId(resultSet.getInt("id"));
        album.setName(resultSet.getString("name"));
        album.setArtistId(resultSet.getInt("artistId"));
        album.setGenre(Genre.valueOf(resultSet.getString("genre")));

        return album;
    }

}