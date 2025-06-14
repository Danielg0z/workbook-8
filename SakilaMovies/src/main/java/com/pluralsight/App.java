package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class App {
    static Scanner scanner = new Scanner(System.in);
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
            dataSource.setUrl("jdbc:mysql://localhost:3306/sakila");
            dataSource.setUsername(username);
            dataSource.setPassword(password);

            Connection connection = dataSource.getConnection();

            boolean appRunning = true;


            while (appRunning) {
                System.out.println("Whats the last name of an Actor that you like?");
                String lastName = scanner.nextLine();
                findActorsByLastName(connection, lastName);

            }

        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    public static void findActorsByLastName (Connection connection, String lastName){
       String sql = "SELECT " +
               " actor_id, last_name, first_name " +
               "FROM " +
               " actor " +
               "WHERE " +
               " last_name = ? " +
               "ORDER BY" +
               " first_name;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1,lastName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                boolean found = false;
                System.out.printf("%-5s %-15s %-15s\n", "ID", "Last Name", "First Name");
                System.out.println("---------------------------------");

                while (resultSet.next()) {
                    int id = resultSet.getInt("actor_id");
                    String first = resultSet.getString("first_name");
                    String last = resultSet.getString("last_name");
                    System.out.printf("%-5d %-15s %-15s%n", id, first, last);
                    found = true;
                }
                if (!found) {
                    System.out.println("No actors found with that name: " + lastName);
                }

        }

    } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

