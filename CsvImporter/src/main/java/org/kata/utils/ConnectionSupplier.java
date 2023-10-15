package org.kata.utils;

import org.kata.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSupplier {

    private final String url;
    private final String fullUrl;
    private final String username;
    private final String password;

    public ConnectionSupplier(DatabaseConfig databaseConfig){
        this.url = databaseConfig.getUrl();
        this.fullUrl = databaseConfig.getFullUrl();
        this.username = databaseConfig.getUsername();
        this.password = databaseConfig.getPassword();
    }


    public Connection getNewConnection(){
        try{
            return DriverManager.getConnection(fullUrl, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnectionForCreateDatabase(){
        try{
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


