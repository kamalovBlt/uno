package ru.itis.unogame.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {

    private static volatile DatabaseConfig instance;

    private final DataSource dataSource;

    private DatabaseConfig() {

        try {

            Class.forName("org.postgresql.Driver");

            Properties applicationProperties = new Properties();
            applicationProperties.load(new FileInputStream("server/src/main/resources/application.properties"));

            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(applicationProperties.getProperty("db.datasource.url"));
            hikariConfig.setUsername(applicationProperties.getProperty("db.datasource.username"));
            hikariConfig.setPassword(applicationProperties.getProperty("db.datasource.password"));
            hikariConfig.setDriverClassName(applicationProperties.getProperty("db.datasource.driverClassName"));
            hikariConfig.setMaximumPoolSize(Integer.parseInt(applicationProperties.getProperty("db.hikari.maximumPoolSize")));

            dataSource = new HikariDataSource(hikariConfig);

            Liquibase liquibase = new Liquibase(
                    applicationProperties.getProperty("db.liquibase.changelogFile"),
                    new ClassLoaderResourceAccessor(),
                    new JdbcConnection(dataSource.getConnection()));

            liquibase.update();

        } catch (IOException | ClassNotFoundException | SQLException | LiquibaseException e) {
            throw new RuntimeException(e);
        }


    }

    public static DatabaseConfig getInstance() {
        if (instance == null) {
            synchronized (DatabaseConfig.class) {
                if (instance == null) {
                    instance = new DatabaseConfig();
                }
            }
        }
        return instance;
    }

    public DataSource getDataSource() {
        return dataSource;
    }


}
