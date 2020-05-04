/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DBConnection;

/**
 *
 * @author kirkmaclean
 */
public class User {
    public static int userID; //auto incremented in database
    public static String username;
    public String password;

    
    //constructor
    public User(int userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    public User() {
        
    }

    //getters
    public int getUserID() {
        return userID;
    }
    
    public String getUsername() {
        return username;
    }
     
    public String getPassword() {
        return this.password;
    }

    //setters

    public void setUserID(int userID) {
        this.userID = userID;
    }
    

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public static ObservableList<User> getAllUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        try {
            PreparedStatement stmt = DBConnection.startConnection().prepareStatement(
                "SELECT * FROM user"
                );
            
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("userId"));
                user.setUsername(rs.getString("userName"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }//CLOSES while
        }//CLOSES try
        catch (SQLException e) {
            System.out.println("Issue with SQL");
            e.printStackTrace();
        }//CLOSES catch

        return users;
    }//CLOSES getAllUsers
    
    
}
