package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.Scanner;

public class App {
    static Scanner scanner = new Scanner(System.in);
    static String formatter;


    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println(
                    "Application needs two arguments to run: " +
                            "java com.pluralsight.UsingDriverManager <username> <password>"
            );
            System.exit(1);
        }

        //gets the username and password
        String username = args[0];
        String password = args[1];

        try (BasicDataSource dataSource = new BasicDataSource()) {

            // Configure the dataSource
            dataSource.setUrl("jdbc:mysql://localhost:3306/northwind");
            dataSource.setUsername(username);
            dataSource.setPassword(password);

            Connection connection = dataSource.getConnection();

            boolean appRunning = true;

            while (appRunning) {

                System.out.println("1) Display All Products");
                System.out.println("2) View All customers");
                System.out.println("3) Display all categories");
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
                    case 3:
                        categoryMenu(connection, scanner);
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

                    default:
                        System.out.println("Invalid Choice");

                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void viewProducts(Connection connection) {

        try (
                // create prepared statement
                //this is like opening your query window
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT " +
                                "ProductID, " +
                                "ProductName, " +
                                "UnitPrice, " +
                                "UnitsInStock " +
                                "FROM " +
                                "products " +
                                " ORDER BY " +
                                "ProductName");

                ResultSet resultSet = preparedStatement.executeQuery();
        ) {

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
        }

    }

    public static void viewCustomers(Connection connection) {

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT " +
                                " ContactName, " +
                                " CompanyName, " +
                                " City, " +
                                "Country, " +
                                " Phone " +
                                "FROM" +
                                " Customers " +
                                "ORDER BY " +
                                "Country");

                ResultSet resultSet = preparedStatement.executeQuery();
        ) {

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
        }
    }


    public static void categoryMenu(Connection connection, Scanner scanner) {

        //ask them if they want to see products for a specific category
        System.out.println("Do you want to see the products in a category?");
        System.out.println("\t1) Yes");
        System.out.println("\t2) No");
        System.out.print("Select an option: ");

        switch (scanner.nextInt()) {
            case 1:

                System.out.print("What category id would you like to view products for?: ");
                int catID = scanner.nextInt();
                viewAllCategories(connection, catID);
                break;
            case 2:
                return;
            default:
                System.out.println("Ok, back to the main menu then!");
        }

    }

    public static void viewAllCategories(Connection connection, int catID) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT " +
                        " CategoryID, " +
                        " CategoryName " +
                        "FROM " +
                        " Categories " +
                        "ORDER BY " +
                        " CategoryId");

             ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            formatter = System.out.printf("%-4s %-20s \n", "Category ID", "Categories").toString();
            // process the results
            // this shows the way to view the results set but java doesn't have a spreadsheet view for us
            System.out.println(formatter);
            System.out.println("--------------------------------------------");

            // result loop for view
            while (resultSet.next()) {
                int categoryID = resultSet.getInt("CategoryID");
                String categoryName = resultSet.getString("CategoryName");
                System.out.printf("%-4s %-20s \n", categoryID, categoryName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewProductByCategory(Connection connection, int catID) {

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT " +
                                " ProductID, " +
                                " ProductName " +
                                " UnitPrice " +
                                " UnitsInStock " +
                                "FROM " +
                                " Products " +
                                "WHERE" +
                                " categoryID = ? " +
                                "ORDER BY " +
                                " ProductName");

        ) {

            // allows for user to input category
            //Answers the "?"
            preparedStatement.setInt(1, catID);

            //execute query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
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
            }

            } catch (SQLException e) {
                e.printStackTrace();
            }


    }
}

