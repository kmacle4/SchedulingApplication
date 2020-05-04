/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195_softwareii;

import controller.CustomersMainController;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import util.DBConnection;
import util.DBQuery;

/**
 *
 * @author kirkmaclean
 */
public class C195_SoftwareII extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LogIn.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        
        //Establish Connection to Database
        Connection conn = DBConnection.startConnection();
        
    /*    
        //Create statement object
        DBQuery.setStatement(conn);
        
        //Get statement reference
        Statement statement = DBQuery.getStatement();
        
    
        //Raw SQL insert statement
        //String insertStatement = "INSERT INTO country(country, createDate, createdBy, lastUpdateBy) VALUES('US', '2020-02-22 00:00:00', 'admin', 'admin')";
        
        
        //Variable Insert
        String countryName = "Jamaica";
        String createDate = "2020-02-22 00:00:00";
        String createdBy = "admin";
        String lastUpdateBy = "admin";
        
        String insertStatement = "INSERT INTO country(country, createDate, createdBy, lastUpdateBy)" + 
                                 "VALUES (" + 
                                 "'" + countryName + "'," + 
                                 "'" + createDate + "'," +
                                 "'" + createdBy + "'," +
                                 "'" + lastUpdateBy + "'" +
                                 ")";

        
        
        
        //RAW Update Statement table first then column
        String updateStatement = "UPDATE country SET country = 'Germany' WHERE country = 'Jamaica'";
        
        
        
        //RAW Delete statement
        String deleteStatement = "DELETE FROM country WHERE country = 'Germany'";
        
        //Execute sql statement
        statement.execute(deleteStatement);
        
        //Confirm rows affected
        if(statement.getUpdateCount() > 0)
            System.out.println(statement.getUpdateCount() + " row(s) affected");
        else
            System.out.println("NO CHANGE");
        
    
        //SELECT Statement
        String selectStatement = "SELECT * FROM country";
        
        try{
            //Execute Statement
            statement.execute(selectStatement);

            //Get ResultSet
            ResultSet rs = statement.getResultSet();

            //Forward Scroll ResultSet
            while(rs.next()){
                int countryId = rs.getInt("countryId");
                String countryName = rs.getString("country");
                LocalDate date = rs.getDate("createDate").toLocalDate();
                LocalTime time = rs.getTime("createDate").toLocalTime();
                String createdBy = rs.getString("createdBy");
                LocalDateTime lastUpdate = rs.getTimestamp("lastUpdate").toLocalDateTime();

                //DISPLAY RECORD
                System.out.println(countryId + " | " + countryName + " | " + date + " " + time + " | " + createdBy + " | " + lastUpdate);
            }//CLOSES while
        }//CLOSES TRY
        catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
        }//CLOSES catch
   
    
        //Prepared Statement examples
        
        //INSERT - ? marks indexing starts at 1
        String insertStatement = "INSERT INTO country(country, createDate, createdBy, lastUpdateBy) VALUES(?, ?, ?, ?)";
    
        //CREATE PREPARED STATEMENT OBJECT
        DBQuery.setPreparedStatement(conn, insertStatement);
        
        PreparedStatement ps = DBQuery.getPreparedStatement();
        
        String countryName;
        String createDate = "2020-03-28 00:00:00";
        String createdBy = "admin";
        String lastUpdateBy = "admin";
        
        //KEYBOARD INPUT
        Scanner keyboard = new Scanner(System.in);
        
        //GENERATES PROMPT
        System.out.print("Enter Country Name");
        //ACCEPTES user input
        countryName = keyboard.nextLine();
        
        //keyvalue mapping
        ps.setString(1, countryName);
        ps.setString(2, createDate);
        ps.setString(3, createdBy);
        ps.setString(4, lastUpdateBy);
        
        //Execute Prepared Statement
        ps.execute();
        
        //Check Rows Affected
        if(ps.getUpdateCount() > 0)
            System.out.println(ps.getUpdateCount() + " row(s) affected");
        else
            System.out.println("NO CHANGE");
         
    
    //INSERT - ? marks indexing starts at 1
        String updateStatement = "UPDATE SET country = ?, createdBy = ? WHERE country = ?";
    
        //CREATE PREPARED STATEMENT OBJECT
        DBQuery.setPreparedStatement(conn, updateStatement);
        
        PreparedStatement ps = DBQuery.getPreparedStatement();
        
        String countryName, newCountry, createdBy;
       // String createDate = "2020-03-28 00:00:00";
        //String createdBy = "admin";
        //String lastUpdateBy = "admin";
        
        //KEYBOARD INPUT
        Scanner keyboard = new Scanner(System.in);
        
        //GENERATES PROMPT
        System.out.print("Enter Country to UPDATE");
        countryName = keyboard.nextLine();
        
        //GENERATES PROMPT
        System.out.print("Enter New Country");
        newCountry = keyboard.nextLine();
        
        //GENERATES PROMPT
        System.out.print("Enter User");
        createdBy = keyboard.nextLine();
        
        
        //keyvalue mapping
        ps.setString(1, newCountry);
        ps.setString(2, createdBy);
        ps.setString(3, countryName);
        
        //Execute Prepared Statement
        ps.execute();
        
        //Check Rows Affected
        if(ps.getUpdateCount() > 0)
            System.out.println(ps.getUpdateCount() + " row(s) affected");
        else
            System.out.println("NO CHANGE");
    
    
        String deleteStatement = "DELETE FROM country WHERE country = ?";
        
        //CREATE PREPARED STATEMENT OBJECT
        DBQuery.setPreparedStatement(conn, deleteStatement);
        
        PreparedStatement ps = DBQuery.getPreparedStatement();
        
        String countryName;
        
        //KEYBOARD INPUT
        Scanner keyboard = new Scanner(System.in);
        
        //GENERATES PROMPT
        System.out.print("Enter Country to DELETE");
        countryName = keyboard.nextLine();
        
        //keyvalue mapping
        ps.setString(1, countryName);
        

        ps.execute();

        String selectStatement = "SELECT * FROM country";
        
        //CREATE PREPARED STATEMENT OBJECT
        DBQuery.setPreparedStatement(conn, selectStatement);
        
        PreparedStatement ps = DBQuery.getPreparedStatement();
        
        ps.execute();
        
        ResultSet rs = ps.getResultSet();
        
         while(rs.next()){
                int countryId = rs.getInt("countryId");
                String countryName = rs.getString("country");
                LocalDate date = rs.getDate("createDate").toLocalDate();
                LocalTime time = rs.getTime("createDate").toLocalTime();
                String createdBy = rs.getString("createdBy");
                LocalDateTime lastUpdate = rs.getTimestamp("lastUpdate").toLocalDateTime();
        
        */
    
        launch(args);
        
        //Closes Connection to Database
        util.DBConnection.closeConnection();
    }
    
}
