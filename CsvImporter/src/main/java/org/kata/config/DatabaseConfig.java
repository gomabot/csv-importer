package org.kata.config;

public class DatabaseConfig {
    private final String url;
    private final String databaseName;
    private final String username;
    private final String password;

    public DatabaseConfig() {
        this.url = "";
        this.databaseName = "";
        this.username = "";
        this.password = "";
    }

    public String getUrl() {
        return url;
    }

    public String getDatabaseName(){
        return databaseName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullUrl() {
        return url + "/" + databaseName;
    }


}
