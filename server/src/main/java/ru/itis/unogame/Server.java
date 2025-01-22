package ru.itis.unogame;

import ru.itis.unogame.config.DatabaseConfig;

import java.sql.SQLException;

public class Server {
    public static void main(String[] args) throws SQLException {

        DatabaseConfig databaseConfig = DatabaseConfig.getInstance();
        databaseConfig.getDataSource().getConnection();

    }
}
