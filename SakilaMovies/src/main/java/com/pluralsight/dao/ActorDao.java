package com.pluralsight.dao;

import com.pluralsight.models.Actor;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;

import java.time.LocalDateTime;
import java.util.List;

public class ActorDao {
    //create our Basic DataSource
    private BasicDataSource dataSource;

    public ActorDao(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }
    /*These methods down here follow the C.R.U.D pattern
    C(Create), R(Read), U(Update), D(Delete)
    */



    public List<String> getAllActors(){

        List<String> actors = new ArrayList<>();

        //query for prepared statement
        String sql = "SELECT actor_id, " + "first_name, " + "last_name " +
                "FROM actor;";

        //try with resources
        try(
                //generate the connection from the datasource for this query
                Connection connection = dataSource.getConnection();
                //create our prepared statement
                PreparedStatement preparedStatement  = connection.prepareStatement(sql);
                //execute our statement to get results
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            //loop over the results and create actor objects and add the objects to the array list
            while(resultSet.next()) {
                int actorId = resultSet.getInt("actor_id");
                String firstName = resultSet.getString("first_name");
                String lastName  = resultSet.getString("last_name");
                //this Timestamp value so
                Timestamp timestamp = resultSet.getTimestamp("last_update");

                Actor actor = new Actor(actorId, firstName, lastName, timestamp);
                actors.add(actorId + ": " + firstName + " " + lastName);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }



    }

    // add actors, |C|.R.U.D - Create
    public void addActor(String firstName, String lastName){
        String sql = "INSERT INTO  actor" + "actor" +
                "(first_name, last_name, last_update) +\n" +
                "VALUES (?, ?, CURRENT_TIMESTAMP)";

        // INSERT INTO adds a new row to the table
        try(
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " actor added.");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    // delete actors, C.|R|.U.D - Remove
    public void deleteActorsByID(int actorId){

        String sql = "DELETE FROM Actors" +
                "WHERE actor_id >= ?";

        // DELETE removes rows that match the condition
        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            //sets the parameters to ? (user input)
            preparedStatement.setInt(1, actorId);

            int rowsAffected = preparedStatement.executeUpdate();
            //return ArrayList of Actors
            System.out.println(rowsAffected + " actor deleted.");

        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void updateA

}
