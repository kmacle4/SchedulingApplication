/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author kirkmaclean
 */
public class DBQuery {
    
    //Statement reference
    private static Statement statement;
    private static PreparedStatement ps;
    
    //Create Statment Object
    public static void setStatement(Connection conn) throws SQLException{
        statement = conn.createStatement();
    }//CLOSES setStatement
    
    //Return Statement Object
    public static Statement getStatement(){
        return statement;
    }//CLOSES getStatement
    
    //Create Statment Object For PreparedStatment
    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException{
        statement = conn.prepareStatement(sqlStatement);
    }//CLOSES setStatement
    
    //Return PrepatedStatement Object
    public static PreparedStatement getPreparedStatement(){
        return ps;
    }//CLOSES getStatement
    
}//CLOSES class DBQuery
