package org.kata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kata.config.Config;
import org.kata.csv.CsvProcessor;
import org.kata.database.DatabaseManager;
import org.kata.utils.ConnectionSupplier;
import org.kata.utils.JsonUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        comprobateArgs(args);
        final Config config = getConfig(args);
        final ConnectionSupplier connectionSupplier = new ConnectionSupplier(config.getDatabaseConfig());
        final DatabaseManager databaseManager = new DatabaseManager(connectionSupplier, config.getDatabaseConfig());
        final CsvProcessor csvProcessor = new CsvProcessor(databaseManager, connectionSupplier);
        databaseManager.createDatabase();
        databaseManager.createTable();
        csvProcessor.importCsv(args[1]);
        csvProcessor.exportToCsv(config.getCsvPathConfig());
        generateAndShowSummary(databaseManager);

    }

    private static Config getConfig(final String[] args) {

        final Path path = Paths.get(args[0]);

        if (!Files.isRegularFile(path)) {
            exitWithError("The first parameter was expected to be the config file, but it does not exist (or is not a regular file)");
        }

        try {

            String configFileContent = new String(Files.readAllBytes(path));

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(configFileContent);

            if (!jsonNode.isObject()) {
                exitWithError("The config file does not contain a valid JSON object");
            }

            if (!jsonNode.has("databaseConfig") || !jsonNode.has("csvPathConfig")) {
                exitWithError("The config file is missing the 'databaseConfig' or 'csvPathConfig' field");
            }

            JsonNode databaseConfigNode = jsonNode.get("databaseConfig");
            JsonNode csvPathConfigNode = jsonNode.get("csvPathConfig");

            if (!databaseConfigNode.isObject() || !csvPathConfigNode.isObject()) {
                exitWithError("The 'databaseConfig' or 'csvPathConfig' field is not a valid JSON object");
            }

            if (!databaseConfigNode.has("url") ||
                    !databaseConfigNode.has("username") ||
                    !databaseConfigNode.has("password") ||
                    !databaseConfigNode.has("databaseName")){
                exitWithError("The 'databaseConfig' field is missing 'url', 'username', or 'password' field");
            }

            if (!csvPathConfigNode.has("path")) {
                exitWithError("The 'csvPathConfig' field is missing 'path' field");
            }

            return JsonUtils.parse(Config.class, new String(Files.readAllBytes(path)));

        } catch (JsonProcessingException e) {
            exitWithError("Could not parse config json file: " + e.getMessage());
        } catch (IOException e) {
            exitWithError("Could not read config file: " + e.getMessage());
        }

        throw new IllegalStateException("If failed system should already be halted");

    }

    private static void comprobateArgs(final String[] args){

        if (args.length != 2) {
            exitWithError(String.format("A parameter with the path of the config and another with the CSV file was expected but %d were found", args.length));
        }

        Path configPath = Paths.get(args[0]);
        if (!Files.exists(configPath) || !Files.isRegularFile(configPath) || !configPath.getFileName().toString().toLowerCase().endsWith(".json")){
            exitWithError("The first parameter must be a valid configuration file");
        }

        Path csvPath = Paths.get(args[1]);
        if (!Files.exists(csvPath) || !Files.isRegularFile(csvPath) || !csvPath.getFileName().toString().toLowerCase().endsWith(".csv")) {
            exitWithError("The second parameter must be a valid CSV file");
        }

    }

    private static void exitWithError(final String error) {
        System.err.println(error);
        System.exit(1);
    }

    private static void generateAndShowSummary(DatabaseManager databaseManager) {

        Map<String, Map<String, Integer>> summary = databaseManager.generateSummary();

        for (String column : summary.keySet()) {
            System.out.println("Resumen por columna: " + column);
            Map<String, Integer> countMap = summary.get(column);
            for (String type : countMap.keySet()) {
                int count = countMap.get(type);
                System.out.println(type + ": " + count);
            }
            System.out.println();
        }
    }

}

