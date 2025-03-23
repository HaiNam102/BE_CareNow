package com.example.Cap2.NannyNow.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
public class DatabaseInitializer {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public CommandLineRunner initializeDatabase() {
        return args -> {
            String url = "jdbc:mysql://localhost:3306/";
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(driverClassName);
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);

            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {
                
                System.out.println("Creating database if not exists...");
                statement.executeUpdate("CREATE DATABASE IF NOT EXISTS care_now");
                System.out.println("Database created or already exists!");
                
            } catch (SQLException e) {
                System.err.println("Error creating database: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
} 