/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import util.DBConnection;

/**
 * FXML Controller class
 *
 * @author kirkmaclean
 */
public class ReportsController implements Initializable {

    
    @FXML
    private Label labelReports;

    @FXML
    private Label labelReportType1;

    @FXML
    private TextArea textAreaForReportType1;

    @FXML
    private Label labelReportType2;

    @FXML
    private TextArea textAreaForReportType2;

    @FXML
    private Label labelReportType3;

    @FXML
    private TextArea textAreaForReportType3;

    @FXML
    private Button buttonBack;
    
    Parent scene;
    Stage stage;

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        monthlyReport();
        userReport();
        customerReport();
    }    
    
    @FXML
    void onActionBackButton(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel?");
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.isPresent() && result.get() == ButtonType.OK){
            
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            
            stage.setScene(new Scene(scene));
            
            stage.show();
        }//CLOSES if

    }
    
    public void monthlyReport() {
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            String query = "SELECT description, MONTHNAME(start) as 'Month', COUNT(*) as 'Total' FROM appointment GROUP BY description, MONTH(start)";
            ResultSet results = statement.executeQuery(query);
            StringBuilder textForReport1 = new StringBuilder();
            textForReport1.append(String.format("%1$-55s %2$-55s %3$s \n", "Month", "Appointment Type", "Total"));
            textForReport1.append(String.join("", Collections.nCopies(110, "-")));
            textForReport1.append("\n");
            while(results.next()) {
                textForReport1.append(String.format("%1$-55s %2$-60s %3$d \n", 
                    results.getString("Month"), results.getString("description"), results.getInt("Total")));
            }//CLOSES while
            statement.close();
            textAreaForReportType1.setText(textForReport1.toString());
        }//CLOSES try
        catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }    
    
    public void userReport() {
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            String query = "SELECT appointment.contact, appointment.description, customer.customerName, start, end " +
                    "FROM appointment JOIN customer ON customer.customerId = appointment.customerId " +
                    "GROUP BY appointment.contact, MONTH(start), start";
            ResultSet results = statement.executeQuery(query);
            StringBuilder textForReport2 = new StringBuilder();
            textForReport2.append(String.format("%1$-25s %2$-25s %3$-25s %4$-25s %5$s \n", 
                    "Consultant", "Appointment", "Customer", "Start", "End"));
            textForReport2.append(String.join("", Collections.nCopies(110, "-")));
            textForReport2.append("\n");
            while(results.next()) {
                textForReport2.append(String.format("%1$-25s %2$-25s %3$-25s %4$-25s %5$s \n", 
                    results.getString("contact"), results.getString("description"), results.getString("customerName"),
                    results.getString("start"), results.getString("end")));
            }
            statement.close();
            textAreaForReportType2.setText(textForReport2.toString());
        }
        catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }
    
    public void customerReport() {
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            String query = "SELECT customer.customerName, COUNT(*) as 'Total' FROM customer JOIN appointment " +
                "ON customer.customerId = appointment.customerId GROUP BY customerName";
            ResultSet results = statement.executeQuery(query);
            StringBuilder textForReport3 = new StringBuilder();
            textForReport3.append(String.format("%1$-65s %2$-65s \n", "Customer", "Total Appointments"));
            textForReport3.append(String.join("", Collections.nCopies(110, "-")));
            textForReport3.append("\n");
            while(results.next()) {
                textForReport3.append(String.format("%1$s %2$65d \n", 
                    results.getString("customerName"), results.getInt("Total")));
            }
            statement.close();
            textAreaForReportType3.setText(textForReport3.toString());
        }
        catch (SQLException ex) {
            System.out.println("SQLExcpetion: " + ex.getMessage());
        }
    }
    
}
