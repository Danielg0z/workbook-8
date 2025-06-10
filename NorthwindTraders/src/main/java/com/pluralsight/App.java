package com.pluralsight;

import java.sql.*;
import java.util.Scanner;

public class App {
    static Scanner scanner = new Scanner(System.in);
    static Connection connection = null;
    static PreparedStatement preparedStatement = null;
    static ResultSet resultSet = null;
    static String formatter;


    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println(
                    "Application needs two arguments to run: " +
                            "java com.pluralsight.UsingDriverManager <username> <password>"
            );
            System.exit(1);
        }

        String username = args[0];
        String password = args[1];

        boolean appRunning = true;

        while (appRunning) {

            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind",
                        username,
                        password);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            System.out.println("1) Display All Products");
            System.out.println("2) View All customers");
            System.out.println("0) Exit");
            System.out.println("Select an option: ");

            int subOption = scanner.nextInt();

            switch (subOption) {
                case 1:
                    viewProducts(connection);
                    break;
                case 2:
                    viewCustomers(connection);
                    break;
                case 0:
                    System.out.println("All good Boss.");
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    appRunning = false;
                    break;

            }


        }
    }

    public static void viewProducts(Connection connection) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // create prepared statement
            //this is like opening your query window
            preparedStatement = connection.prepareStatement(
                    "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products");

            resultSet = preparedStatement.executeQuery();

            formatter = System.out.printf("%-4s %-20s %-8s %-6s\n", "Id", "Name", "Price", "Stock").toString();

            // process the results
            // this shows the way to view the results set but java doesn't have a spreadsheet view for us
            System.out.println(formatter);
            System.out.println("--------------------------------------------");

            // result loop for view
            while (resultSet.next()) {
                int id = resultSet.getInt("ProductID");
                String productName = resultSet.getString("ProductName");
                double unitPrice = resultSet.getInt("UnitPrice");
                int unitStock = resultSet.getInt("UnitsInStock");
                System.out.printf("%-4d %-20s  %-7.2f %-6d\n", id, productName, unitPrice, unitStock);


            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // close the resources
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    public static void viewCustomers(Connection connection) {

        PreparedStatement preparedStatement1 = null;
        ResultSet resultSet1 = null;

        try {

            preparedStatement = connection.prepareStatement(
                    "SELECT " + " ContactName, " + " CompanyName, " +
                            " City, " + "Country, " + " Phone " + "FROM" + " Customers " + "ORDER BY " +
                            "Country");



            resultSet = preparedStatement.executeQuery();

            formatter = System.out.printf("%-4s %-20s %-8s %-6s %-2s\n", "Contact Name", "Company Name", "City", "Country", "Phone").toString();

            // process the results
            // this shows the way to view the results set but java doesn't have a spreadsheet view for us
            System.out.println(formatter);
            System.out.println("--------------------------------------------");

            // result loop for view
            while (resultSet.next()) {
                String contactName = resultSet.getString("ContactName");
                String companyName = resultSet.getString("CompanyName");
                String city = resultSet.getString("City");
                String country = resultSet.getString("Country");
                String phoneNumber = resultSet.getString("Phone");
                System.out.printf("%-4s %-20s %-8s %-6s %-2s\n", contactName, companyName, city, country, phoneNumber);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // close the resources
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}


