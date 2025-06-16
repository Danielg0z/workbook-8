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
                System.out.println("1. Whats the last name of an Actor that you like?");
                String lastName = scanner.nextLine();
                findActorsByLastName(connection, lastName);

                System.out.println("____________________________");

                System.out.println("2. Films by an Actor");
                System.out.print("Enter an Actor's first Name:");
                String first = scanner.nextLine();
                System.out.print("Enter an Actor's last Name: ");
                String last = scanner.nextLine();

                findFilmsByActor(connection, first, last);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void findActorsByLastName(Connection connection, String lastName) {
        String sql = "SELECT " +
                " actor_id, last_name, first_name " +
                "FROM " +
                " actor " +
                "WHERE " +
                " last_name = ? " +
                "ORDER BY" +
                " first_name;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, lastName);

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

    public static void findFilmsByActor(Connection connection, String firstName, String lastName) {
        //sql query
        String sql = "SELECT f.title " +
                "FROM film f " +
                "JOIN film_actor fa ON f.film_id = fa.film_id " +
                "JOIN actor a ON fa.actor_id = a.actor_id " +
                "WHERE a.first_name = ? AND a.last_name = ? " +
                "ORDER BY f.title";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            //setting parameters for the first and last name the "?"s
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);

            //for output
            String fullName = firstName + " " + lastName;

            //check if there are any results BEFORE printing
            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                //Printing  film title
                if(resultSet.next()) {
                    System.out.println("\n Movies with " + fullName);
                    System.out.println("\n-------------------------");


                    //already on first row, so do/while so do/while ensures we don't skip it
                    do {
                    String title = resultSet.getString("title");
                    System.out.println(title);

                    } while (resultSet.next());
            } else {
                    System.out.println("No movies found with " + fullName + ":");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

