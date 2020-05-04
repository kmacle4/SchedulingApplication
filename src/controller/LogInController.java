/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointment;
import model.User;
import util.DBConnection;

/**
 * FXML Controller class
 *
 * @author kirkmaclean
 */
public class LogInController implements Initializable {

    @FXML
    private Label labelLogIn;
    @FXML
    private Label labelUsername;
    @FXML
    private TextField textUsername;
    @FXML
    private Label labelPassword;
    @FXML
    private TextField textPassword;
    @FXML
    private Button buttonLogIn;
    @FXML
    private Button buttonCancel;
    
    public static Connection connect = null;
    Stage stage;
    Parent scene;
    
    ObservableList<Appointment> reminderList = FXCollections.observableArrayList();
    private DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private ZoneId localZoneId = ZoneId.systemDefault();
    private static User currentUser;
    private String errorHeader;
    private String errorTitle;
    private String errorText;
    private String contentText;
    private String title;
    private String languageMessage;
    private String mainMessage;
    private String successMessage;
    private String cancelMessage;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Locale locale = Locale.getDefault();
        rb = ResourceBundle.getBundle("util/login", locale);
        labelLogIn.setText(rb.getString("login"));
        labelUsername.setText(rb.getString("username"));
        labelPassword.setText(rb.getString("password"));
        buttonLogIn.setText(rb.getString("login"));
        buttonCancel.setText(rb.getString("cancel"));
        errorHeader = rb.getString("errorheader");
        errorTitle = rb.getString("errortitle");
        errorText = rb.getString("errortext");
        contentText = rb.getString("contenttext");
        title = rb.getString("title");
        mainMessage= rb.getString("message");
        successMessage = rb.getString("success");
        cancelMessage = rb.getString("cancelMessage");
    }    

    
    @FXML
    void onActionLogInButton(ActionEvent event) throws IOException, SQLException {
        
        System.out.println(textUsername.getText());
        System.out.println(textPassword.getText());
        
        if(textUsername.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(errorHeader);
            alert.setContentText(errorText);
            alert.showAndWait();
            return;
        }//CLOSES if
        
        if(textPassword.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(errorHeader);
            alert.setContentText(errorText);
            alert.showAndWait();
            return;
        }//CLOSES if
        
        String userName = textUsername.getText();
        String password = textPassword.getText();
        
        //DBManager db = new DBManager();
        
        boolean flag = validateLogin(userName, password);
        
        if(!flag){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(errorHeader);
            alert.setContentText(errorHeader);
            alert.showAndWait();
        }//CLOSES if
        
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(successMessage);
            alert.setContentText(successMessage);
            alert.showAndWait();
            System.out.println("Log In Successful");
            
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
            

        }//CLOSES else
 
    }//CLOSES onActionLogInButton
 
    @FXML
    void onActionCancelButton(ActionEvent event) {
        
        System.out.println("Cancel Log In");
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText(cancelMessage);
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.isPresent() && result.get() == ButtonType.OK){
            System.out.println("EXIT APP");
            
            System.exit(0);
        }//CLOSES if

    }//CLOSES onActionCancelButton
    
    
    public boolean validateLogin(String userN, String userP) throws SQLException{
        User user = new User();
        
        try(Connection connect = DBConnection.startConnection();
            PreparedStatement prepStmt = connect.prepareStatement("SELECT * FROM user WHERE userName = ? and password = ?")){
            prepStmt.setString(1, userN);
            prepStmt.setString(2, userP);
            user.setUsername(userN);
            loginLog(userN);
        
            System.out.println("Username" + user.getUsername());
            
            System.out.println(prepStmt);
            
            ResultSet result = prepStmt.executeQuery();
            
            if(result.next()){
                return true;
            }//CLOSES if
        }//CLOSES try
        
        catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
        }//CLOSES catch
        
        return false;
    }//CLOSES validateLogin
    
    //creates a new log file if one doesnt exist and inserts login information for current user
    public void loginLog(String user) {
        try {
            String fileName = "SignInLog";
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.append(LocalDateTime.now() + " " + user + " " + "\n");
            System.out.println("New login recorded in log file.");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public boolean apptIn15Min() throws SQLException {
            Appointment appointment = new Appointment();
            User users = new User();
            LocalDateTime now = LocalDateTime.now();
            ZoneId zid = ZoneId.systemDefault();
            ZonedDateTime zdt = now.atZone(zid);
            LocalDateTime ldt = zdt.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
            LocalDateTime ldt2 = ldt.plusMinutes(15);
            String user = users.getUsername();
            System.out.println(ldt);
            System.out.println(ldt2);
            System.out.println(user);
            try {
                Connection con = DBConnection.startConnection();
                String query = "SELECT * FROM appointment WHERE start BETWEEN ? AND ? ;";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setObject(1,ldt);
                statement.setObject(2,ldt2);
                ResultSet rs = statement.executeQuery();
                    return true;
                }
            catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
            }
        return false;

}
    
    public static User getCurrentUser() {
        return currentUser;
    }
}
