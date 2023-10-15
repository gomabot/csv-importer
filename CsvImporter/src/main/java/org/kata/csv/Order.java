package org.kata.csv;

import java.sql.Date;

public class Order {
    private int orderID;
    private String orderPriority;
    private Date orderDate;
    private String region;
    private String country;
    private String itemType;
    private String salesChannel;
    private Date shipDate;
    private int unitsSold;
    private double unitPrice;
    private double unitCost;
    private double totalRevenue;
    private double totalCost;
    private double totalProfit;

    public int getOrderID() {
        return orderID;
    }

    public String getOrderPriority() {
        return orderPriority;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }

    public String getItemType() {
        return itemType;
    }

    public String getSalesChannel() {
        return salesChannel;
    }

    public Date getShipDate() {
        return shipDate;
    }

    public int getUnitsSold() {
        return unitsSold;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public void setOrderPriority(String orderPriority) {
        this.orderPriority = orderPriority;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public void setSalesChannel(String salesChannel) {
        this.salesChannel = salesChannel;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }

    public void setUnitsSold(int unitsSold) {
        this.unitsSold = unitsSold;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }

}


