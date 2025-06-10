package com.pluralsight;

import java.sql.*;

public class App {
    public static void main(String[] args) throws ClassNotFoundException {

        if (args.length != 2) {
            System.out.println(
                    "Application needs two arguments to run: " +
                            "java com.pluralsight.UsingDriverManager <username> <password>"
            );
            System.exit(1);
        }

        String username = args[0];
        String password = args[1];


// load the driver
        Class.forName("com.mysql.cj.jdbc.Driver");


        try {

            // 1. open a connection to the database
            // use the database URL to point to the correct database

            //This is like opening mysQL and clicking local host
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/northwind",
                    username,
                    password);

            // create statement
            // the statement is tied to the open connection
            //this is like opening your query window
            PreparedStatement preparedStatement =
                    connection.prepareStatement(
                            "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products");

            // 2. Execute your query
            // this is clicking the lighting bolt
            ResultSet results = preparedStatement.executeQuery();
            // process the results
            // this shows the way to view the results set but java doesn't have a spreadsheet view for us
            System.out.printf("%-4s %-20s %-8s %-6s\n", "Id", "Name", "Price", "Stock");
            System.out.println("--------------------------------------------");

            while (results.next()) {
                int id = results.getInt("ProductID");
                String productName  = results.getString("ProductName");
                double unitPrice = results.getInt("UnitPrice");
                int unitStock = results.getInt("UnitsInStock");
                System.out.printf("%-4d %-20s  %-7.2f %-6d\n", id, productName, unitPrice, unitStock);


            }
            // 3. Close the connection
            connection.close();

        } catch (SQLException e) {
            System.out.println("Something went wrong.");
        }

    }
}