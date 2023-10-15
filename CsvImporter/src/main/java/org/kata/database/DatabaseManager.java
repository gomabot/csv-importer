package org.kata.database;

import org.kata.config.DatabaseConfig;
import org.kata.csv.Header;
import org.kata.csv.Order;
import org.kata.utils.ConnectionSupplier;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    private final ConnectionSupplier connectionSupplier;
    private final DatabaseConfig dbConfig;

    public DatabaseManager(ConnectionSupplier connectionSupplier, DatabaseConfig dbConfig) {
        this.connectionSupplier = connectionSupplier;
        this.dbConfig = dbConfig;
    }

    public void createDatabase() {

        try (final Connection connection = connectionSupplier.getConnectionForCreateDatabase();
             final PreparedStatement dropStatement = connection.prepareStatement("DROP DATABASE IF EXISTS " + dbConfig.getDatabaseName());
             final PreparedStatement createStatement = connection.prepareStatement("CREATE DATABASE " + dbConfig.getDatabaseName())) {

            dropStatement.executeUpdate();
            createStatement.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    public void createTable() {

        String createQuery = "CREATE TABLE IF NOT EXISTS orders(" +
                Header.valueOf("ORDERID").getHeaderForDatabase() + " INT, " +
                Header.valueOf("ORDERPRIORITY").getHeaderForDatabase() +" TEXT, " +
                Header.valueOf("ORDERDATE").getHeaderForDatabase() +" DATE, " +
                Header.valueOf("REGION").getHeaderForDatabase() + " TEXT, " +
                Header.valueOf("COUNTRY").getHeaderForDatabase() + " TEXT, " +
                Header.valueOf("ITEMTYPE").getHeaderForDatabase() + " TEXT, " +
                Header.valueOf("SALESCHANNEL").getHeaderForDatabase() + " TEXT, " +
                Header.valueOf("SHIPDATE").getHeaderForDatabase() +  " DATE, " +
                Header.valueOf("UNITSSOLD").getHeaderForDatabase() + " INT, " +
                Header.valueOf("UNITPRICE").getHeaderForDatabase() +  " DECIMAL(10,2), " +
                Header.valueOf("UNITCOST").getHeaderForDatabase() +  " DECIMAL(10,2), " +
                Header.valueOf("TOTALREVENUE").getHeaderForDatabase() +  " DECIMAL(10,2), " +
                Header.valueOf("TOTALCOST").getHeaderForDatabase() +  " DECIMAL(10,2), " +
                Header.valueOf("TOTALPROFIT").getHeaderForDatabase() +  " DECIMAL(10,2))";

        try (final Connection connection = connectionSupplier.getNewConnection();
             final PreparedStatement ps = connection.prepareStatement(createQuery)) {

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error while creating table at database");
            deleteDatabase();
            throw new RuntimeException(e);
        }

    }

    public void insertData(Order order) {

        final String insertQuery = "INSERT INTO orders (" +
                String.join(", ",
                        Header.ORDERID.getHeaderForDatabase(),
                        Header.ORDERPRIORITY.getHeaderForDatabase(),
                        Header.ORDERDATE.getHeaderForDatabase(),
                        Header.REGION.getHeaderForDatabase(),
                        Header.COUNTRY.getHeaderForDatabase(),
                        Header.ITEMTYPE.getHeaderForDatabase(),
                        Header.SALESCHANNEL.getHeaderForDatabase(),
                        Header.SHIPDATE.getHeaderForDatabase(),
                        Header.UNITSSOLD.getHeaderForDatabase(),
                        Header.UNITPRICE.getHeaderForDatabase(),
                        Header.UNITCOST.getHeaderForDatabase(),
                        Header.TOTALREVENUE.getHeaderForDatabase(),
                        Header.TOTALCOST.getHeaderForDatabase(),
                        Header.TOTALPROFIT.getHeaderForDatabase()
                ) + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (final Connection connection = connectionSupplier.getNewConnection();
             final PreparedStatement ps = connection.prepareStatement(insertQuery)) {

            ps.setInt(1, order.getOrderID());
            ps.setString(2, order.getOrderPriority());
            ps.setDate(3, order.getOrderDate());
            ps.setString(4, order.getRegion());
            ps.setString(5, order.getCountry());
            ps.setString(6, order.getItemType());
            ps.setString(7, order.getSalesChannel());
            ps.setDate(8, order.getShipDate());
            ps.setInt(9, order.getUnitsSold());
            ps.setDouble(10, order.getUnitPrice());
            ps.setDouble(11, order.getUnitCost());
            ps.setDouble(12, order.getTotalRevenue());
            ps.setDouble(13, order.getTotalCost());
            ps.setDouble(14, order.getTotalProfit());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error while inserting data at database");
            deleteDatabase();
            throw new RuntimeException(e);
        }

    }

    public void deleteDatabase() {

        try (final Connection connection = connectionSupplier.getNewConnection();
             final PreparedStatement ps = connection.prepareStatement("DROP DATABASE " + dbConfig.getDatabaseName())) {

            ps.executeUpdate();
            System.out.println("database deleted");

        } catch (SQLException e) {
            System.err.println("Error while deleting the database: " + e.getMessage());
        }

    }


    public Map<String, Map<String, Integer>> generateSummary() {

        Map<String, Map<String, Integer>> summary = new HashMap<>();

        String[] columns = new String[]{
                Header.REGION.getHeaderForDatabase(),
                Header.COUNTRY.getHeaderForDatabase(),
                Header.ITEMTYPE.getHeaderForDatabase(),
                Header.SALESCHANNEL.getHeaderForDatabase(),
                Header.ORDERPRIORITY.getHeaderForDatabase()
        };

        for (String column : columns) {
            Map<String, Integer> countMap = getCountByType(column);
            summary.put(column, countMap);
        }

        return summary;

    }

    private Map<String, Integer> getCountByType(String column) {

        Map<String, Integer> countMap = new HashMap<>();

        try (final Connection connection = connectionSupplier.getNewConnection();
             final PreparedStatement ps = connection.prepareStatement("SELECT " + column + ", COUNT(*) AS count FROM orders GROUP BY " + column);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String type = rs.getString(column);
                int count = rs.getInt("count");
                countMap.put(type, count);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return countMap;

    }

}