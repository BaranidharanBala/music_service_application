package com.musicservice.repository.DataBase;

import com.musicservice.model.Artist;
import com.musicservice.repository.IArtistRepository;
import com.musicservice.util.JDBCConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DBArtistRepository implements IArtistRepository {

    private static DBArtistRepository instance;

    private DBArtistRepository() {
    }

    public static DBArtistRepository getInstance() {
        if (instance == null) {
            instance = new DBArtistRepository();
        }
        return instance;
    }

    @Override
    public void save(Artist artist) {
        String sql = """
                INSERT INTO artists ("userId", name, bio)
                VALUES ( ?, ?, ?)
                """;

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, artist.getUserId());
            statement.setString(2, artist.getName());
            statement.setString(3, artist.getBio());
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    artist.setId(resultSet.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Artist artist) {
        String sql = """
                UPDATE artists
                SET name = ?, bio =?
                WHERE id = ?
                """;

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, artist.getName());
            statement.setString(2, artist.getBio());
            statement.setInt(3, artist.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = """
                DELETE FROM artists
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
    public Artist findById(int id) {
        String sql = """
                SELECT id, "userId", name, bio
                FROM artists
                WHERE id = ?
                """;

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Artist artist = new Artist();

                    artist.setId(resultSet.getInt("id"));
                    artist.setUserId(resultSet.getInt("userId"));
                    artist.setName(resultSet.getString("name"));
                    artist.setBio(resultSet.getString("bio"));

                    return artist;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Artist findByUserId(int userId) {
        String sql = """
                SELECT id, "userId", name, bio
                FROM artists
                WHERE "userId" = ?
                """;

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Artist artist = new Artist();

                    artist.setId(resultSet.getInt("id"));
                    artist.setUserId(resultSet.getInt("userId"));
                    artist.setName(resultSet.getString("name"));
                    artist.setBio(resultSet.getString("bio"));

                    return artist;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Map<Integer, Artist> search(String name) {
        String sql = """
                SELECT id, "userId", name, bio
                FROM artists
                WHERE name ILIKE ?
                """;

        Map<Integer, Artist> artists = new HashMap<>();

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + name + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Artist artist = new Artist();

                    artist.setId(resultSet.getInt("id"));
                    artist.setUserId(resultSet.getInt("userId"));
                    artist.setName(resultSet.getString("name"));
                    artist.setBio(resultSet.getString("bio"));

                    artists.put(artist.getId(), artist);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return artists;
    }

    @Override
    public Map<Integer, Artist> findAll() {
        String sql = """
                SELECT id, "userId", name, bio
                FROM artists
                """;

        Map<Integer, Artist> artists = new HashMap<>();

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Artist artist = new Artist();

                artist.setId(resultSet.getInt("id"));
                artist.setUserId(resultSet.getInt("userId"));
                artist.setName(resultSet.getString("name"));
                artist.setBio(resultSet.getString("bio"));

                artists.put(artist.getId(), artist);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return artists;
    }

}