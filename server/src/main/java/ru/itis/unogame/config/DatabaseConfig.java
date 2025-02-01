package ru.itis.unogame.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class DatabaseConfig {

    @Value("${db.datasource.url}")
    private String url;

    @Value("${db.datasource.username}")
    private String username;

    @Value("${db.datasource.password}")
    private String password;

    @Value("${db.datasource.driverClassName}")
    private String driverClassName;

    @Value("${db.hikari.maximumPoolSize}")
    private int maxPoolSize;

    @Value("${db.liquibase.changelogFile}")
    private String pathToChangelog;

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setMaximumPoolSize(maxPoolSize);
        return new HikariDataSource(hikariConfig);
    }
    @Bean
    public Liquibase liquibase(DataSource dataSource) throws SQLException, LiquibaseException {
        var liquibase = new Liquibase(
                    pathToChangelog,
                    new ClassLoaderResourceAccessor(),
                    new JdbcConnection(dataSource.getConnection()));
        liquibase.update();
        return liquibase;
    }
}
