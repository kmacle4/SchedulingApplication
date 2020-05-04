/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.stage.Stage;
import model.User;
import model.Appointment;
import model.Customer;

/**
 * FXML Controller class
 *
 * @author kirkmaclean
 */
public class MainMenuController implements Initializable {

    @FXML
    private Label labelMainMenu;
    @FXML
    private Button buttonCustomers;
    @FXML
    private Button buttonAppointments;
    @FXML
    private Button buttonReports;
    @FXML
    private Button buttonExit;
    Parent scene;
    Stage stage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*Appointment appointment = new Appointment();
        
        try {
            if(appointment.appointmentIn15Min()) {
                Customer customer = new Customer();
                String text = String.format("You have a %s appointment with %s at %s");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Appointment Reminder");
                alert.setHeaderText("Appointment Within 15 Minutes");
                alert.setContentText(text);
                alert.showAndWait();
            }
            else{
                String text2 = String.format("You do not have any appointments within the next 15 minutes");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Appointment Reminder");
                alert.setHeaderText("Appointment Within 15 Minutes");
                alert.setContentText(text2);
                alert.showAndWait();
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
*/
        
        Appointment appointment = AppointmentMainController.appointmentIn15Min();
        if(appointment != null) {
            String text = String.format("You have an appointment",
                appointment.get15Time());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointment Reminder");
            alert.setHeaderText("Appointment Within 15 Minutes");
            alert.setContentText(text);
            alert.showAndWait();
        }
    }    
        
        

    @FXML
    private void onActionCustomersButton(ActionEvent event) throws IOException {
        
        System.out.println("Switch to Customers");
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        
        scene = FXMLLoader.load(getClass().getResource("/view/CustomerMain.fxml"));
        
        stage.setScene(new Scene(scene));
        
        stage.show();
        
    }

    @FXML
    private void onActionAppointmentsButton(ActionEvent event) throws IOException {
        
        System.out.println("Switch to Appointments");
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMain.fxml"));
        
        stage.setScene(new Scene(scene));
        
        stage.show();
    }

    @FXML
    private void onActionReportsButton(ActionEvent event) throws IOException {
        System.out.println("Switch to Reports");
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        
        scene = FXMLLoader.load(getClass().getResource("/view/Reports.fxml"));
        
        stage.setScene(new Scene(scene));
        
        stage.show();
    }

    @FXML
    private void onActionExitButton(ActionEvent event) {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.isPresent() && result.get() == ButtonType.OK){
            System.out.println("Exit");

            System.exit(0);
        }//CLOSES if
    }
    
     
    
}
