package ru.itis.unogame.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.itis.unogame.mapper.RowMapper;
import ru.itis.unogame.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    // language=sql
    private static final String INSERT_USER = "INSERT INTO \"user\" (username, password) VALUES (?, ?) RETURNING id";
    // language=sql
    private static final String FIND_USER_BY_ID = "SELECT id, username, password FROM \"user\" WHERE id = ?";
    // language=sql
    private static final String FIND_USER_BY_USERNAME = "SELECT id, username, password FROM \"user\" WHERE username = ?";

    private final DataSource dataSource;
    private final RowMapper<User> userRowMapper;
    private final RowMapper<Integer> idRowMapper;

    @Override
    public int save(String username, String password) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_USER)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return idRowMapper.mapRow(resultSet).orElseThrow(() -> new IllegalStateException("Query do not return id"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findById(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return userRowMapper.mapRow(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_USERNAME)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            return userRowMapper.mapRow(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
