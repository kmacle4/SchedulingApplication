/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author kirkmaclean
 */
public class DBConnection {
    
    
    //Database Name
    private static final String DB_NAME = "U066r3";
    
    //JDBC URL
    private static final String DB_URL = "jdbc:mysql://3.227.166.251/" + DB_NAME;
    
    //Username
    private static final String USERNAME = "U066r3";
    
    //Password
    private static final String PASSWORD = "53688691523";
    
    //Driver and Connections Interface Reference
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static Connection connect;
    
    //Start Connection Method
    public static Connection startConnection(){
        
        try{
            Class.forName(DRIVER);
            connect = (Connection)DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            System.out.println("Connection was successful");
        }//CLOSES try
        
        catch(ClassNotFoundException | SQLException e){
            System.out.println(e.getMessage());
        }//CLOSES catch
        
        
        return connect;
    }//CLOSES startConnection
    
    public static void closeConnection(){
        
        try{
            connect.close();
            System.out.println("Close Connection");
        }//CLOSES try
        
        catch(SQLException e){
            System.out.println(e.getMessage());
        }//CLOSES catch
        
    }//CLOSES Connection


    
}
