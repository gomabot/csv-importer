package org.kata.csv;

public enum Header {

        ORDERID("Order ID", "order_id"),
        ORDERPRIORITY("Order Priority", "order_priority"),
        ORDERDATE("Order Date", "order_date"),
        REGION("Region","region"),
        COUNTRY("Country", "country"),
        ITEMTYPE("Item Type", "item_type"),
        SALESCHANNEL("Sales Channel", "sales_channel"),
        SHIPDATE("Ship Date", "ship_date"),
        UNITSSOLD("Units Sold", "units_sold"),
        UNITPRICE("Unit Price", "unit_price"),
        UNITCOST("Unit Cost", "unit_cost"),
        TOTALREVENUE("Total Revenue", "total_revenue"),
        TOTALCOST("Total Cost", "total_cost"),
        TOTALPROFIT("Total Profit", "total_profit");

        Header(String headerForCsv, String headerForDatabase){
                this.headerForCsv = headerForCsv;
                this.headerForDatabase = headerForDatabase;
        }

        private final String headerForCsv;
        private final String headerForDatabase;

        public String getHeaderForCsv() {
                return headerForCsv;
        }

        public String getHeaderForDatabase() {
                return headerForDatabase;
        }

}
