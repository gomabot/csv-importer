package org.kata.csv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class CsvValidator {

   public static boolean isValidCsvFile(String csvFile) {

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String headerLine = reader.readLine();
            String[] headers = headerLine.split(",");

            Set<String> expectedHeaders = new HashSet<>();
            for (Header enumHeader : Header.values()) {
                expectedHeaders.add(enumHeader.getHeaderForCsv());
            }

            Set<String> actualHeaders = new HashSet<>();
            for (String header : headers) {
                actualHeaders.add(header.trim());
            }

            return actualHeaders.containsAll(expectedHeaders);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

}
