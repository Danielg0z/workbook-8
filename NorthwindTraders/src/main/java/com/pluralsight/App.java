package com.pluralsight;

import java.sql.*;

public class App {
    public static void main(String[] args) {
        // load the MySQL Driver
        //Class.forName("com.mysql.cj.jdbc.Driver");

        try {
            // 1. open a connection to the database
            // use the database URL to point to the correct database

            //This is like opening mysQL and clicking local host
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/northwind",
                    "root",
                    "yearup");

            // create statement
            // the statement is tied to the open connection
            //this is like opening youre query window
            Statement statement = connection.createStatement();

            // define your query
            //this is like typing your query
            String query = "SELECT ProductName FROM Products;";

            // 2. Execute your query
            // this is clicking the lighting bolt
            ResultSet results = statement.executeQuery(query);
            // process the results
            // this shows the way to view the results set but java doesn't have a spreadsheet view for us
            while (results.next()) {
                String product = results.getString(1);
                System.out.println(product);
            }
            // 3. Close the connection
            connection.close();

        } catch (SQLException e) {
            System.out.println("Something went wrong.");
        }

    }
}