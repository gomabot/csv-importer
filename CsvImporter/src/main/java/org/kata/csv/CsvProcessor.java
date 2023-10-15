package org.kata.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.kata.config.CsvPathConfig;
import org.kata.database.DatabaseManager;
import org.kata.utils.ConnectionSupplier;

import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CsvProcessor {

    private final DatabaseManager databaseManager;
    private final ConnectionSupplier connectionSupplier;

    public CsvProcessor(DatabaseManager databaseManager, ConnectionSupplier connectionSupplier) {
        this.databaseManager = databaseManager;
        this.connectionSupplier = connectionSupplier;
    }

    public void importCsv(String csvFile) {

        if (!CsvValidator.isValidCsvFile(csvFile)) {
            System.err.println("Error: The CSV file is not valid.");
            System.exit(1);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            CSVFormat format = CSVFormat.Builder.create().setHeader().build();
            CSVParser records = new CSVParser(reader, format);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

            for (CSVRecord record : records) {
                int orderID = Integer.parseInt(record.get(Header.ORDERID.getHeaderForCsv()));
                String orderPriority = record.get(Header.ORDERPRIORITY.getHeaderForCsv());
                Date orderDate = new Date(dateFormat.parse(record.get(Header.ORDERDATE.getHeaderForCsv())).getTime());
                String region = record.get(Header.REGION.getHeaderForCsv());
                String country = record.get(Header.COUNTRY.getHeaderForCsv());
                String itemType = record.get(Header.ITEMTYPE.getHeaderForCsv());
                String salesChannel = record.get(Header.SALESCHANNEL.getHeaderForCsv());
                Date shipDate = new Date(dateFormat.parse(record.get(Header.SHIPDATE.getHeaderForCsv())).getTime());
                int unitsSold = Integer.parseInt(record.get(Header.UNITSSOLD.getHeaderForCsv()));
                double unitPrice = Double.parseDouble(record.get(Header.UNITPRICE.getHeaderForCsv()));
                double unitCost = Double.parseDouble(record.get(Header.UNITCOST.getHeaderForCsv()));
                double totalRevenue = Double.parseDouble(record.get(Header.TOTALREVENUE.getHeaderForCsv()));
                double totalCost = Double.parseDouble(record.get(Header.TOTALCOST.getHeaderForCsv()));
                double totalProfit = Double.parseDouble(record.get(Header.TOTALPROFIT.getHeaderForCsv()));

                Order order = new Order();
                order.setOrderID(orderID);
                order.setOrderPriority(orderPriority);
                order.setOrderDate(orderDate);
                order.setRegion(region);
                order.setCountry(country);
                order.setItemType(itemType);
                order.setSalesChannel(salesChannel);
                order.setShipDate(shipDate);
                order.setUnitsSold(unitsSold);
                order.setUnitPrice(unitPrice);
                order.setUnitCost(unitCost);
                order.setTotalRevenue(totalRevenue);
                order.setTotalCost(totalCost);
                order.setTotalProfit(totalProfit);

                databaseManager.insertData(order);
            }

        } catch (IOException e) {
            System.err.println("Error while reading the CSV file: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Error while parsing numeric values: " + e.getMessage());
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Error while parsing the date: " + e.getMessage());
            e.printStackTrace();
        }

    }


    public void exportToCsv(CsvPathConfig csvPath){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        final String csvExportPath = csvPath.getPath() + "/" +LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".csv";

        try (Connection connection = connectionSupplier.getNewConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM orders ORDER BY order_id");
             BufferedWriter writer = new BufferedWriter(new FileWriter(csvExportPath));
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
             ResultSet rs = ps.executeQuery()){

            for (Header header : Header.values()) {
                csvPrinter.print(header.getHeaderForCsv());
            }

            csvPrinter.println();

            while (rs.next()) {

                List<Object> rowData = new ArrayList<>();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    try {
                        Object value = rs.getObject(i);
                        if (value instanceof Date) {
                            value = dateFormat.format(value);
                        }
                        rowData.add(value);

                    } catch (SQLException ex) {
                        throw new RuntimeException("Error retrieving column data", ex);
                    }

                }
                csvPrinter.printRecord(rowData);

            }
            System.out.println("CSV file created successfully.");

        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

}

