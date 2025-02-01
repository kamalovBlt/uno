package ru.itis.unogame.mapper;

import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class IdRowMapper implements RowMapper<Integer> {
    @Override
    public Optional<Integer> mapRow(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return Optional.of(resultSet.getInt("id"));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Integer> mapRows(ResultSet resultSet) throws SQLException {
        List<Integer> ints = new ArrayList<>();
        Optional<Integer> optional = mapRow(resultSet);
        while (optional.isPresent()) {
            ints.add(optional.get());
            optional = mapRow(resultSet);
        }
        return ints;
    }

}
