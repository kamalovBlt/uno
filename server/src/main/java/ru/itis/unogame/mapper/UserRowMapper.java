package ru.itis.unogame.mapper;

import org.springframework.stereotype.Component;
import ru.itis.unogame.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public Optional<User> mapRow(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return Optional.of(
                    new User(resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password")));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<User> mapRows(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();
        Optional<User> optional = mapRow(resultSet);
        while (optional.isPresent()) {
            users.add(optional.get());
            optional = mapRow(resultSet);
        }
        return users;
    }

}
