package org.kata.config;

public class Config {
    private DatabaseConfig databaseConfig;
    private CsvPathConfig csvPathConfig;

    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

    public CsvPathConfig getCsvPathConfig() {
        return csvPathConfig;
    }
}
