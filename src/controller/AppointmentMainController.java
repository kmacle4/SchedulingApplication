/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import model.Customer;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import model.Appointment;
import util.DBConnection;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.User;
import util.DBQuery;

/**
 * FXML Controller class
 *
 * @author kirkmaclean
 */
public class AppointmentMainController implements Initializable {

    @FXML
    private Label labelAppointment;

    @FXML
    private Button buttonWeekly;

    @FXML
    private Button buttonMonthly;

    @FXML
    private Button buttonBack;
    
    @FXML
    private TableView<Appointment> tableViewAppointments;
    
    @FXML
    private TableColumn<Appointment, String> tableColumnCustomer;
    
    @FXML
    private TableColumn<Appointment, String> tableColumnType;
   
    @FXML
    private TableColumn<Appointment, String> tableColumnStartTime;
    
    @FXML
    private TableColumn<Appointment, String> tableColumnEndTime;
    
    @FXML
    private Label labelCustomer;

    @FXML
    private Label labelTitle;

    @FXML
    private Label labelDescription;

    @FXML
    private Label labelLocation;

    @FXML
    private Label labelContact;

    @FXML
    private Label labelType;

    @FXML
    private Label labelURL;

    @FXML
    private Label labelStartTime;

    @FXML
    private Label labelEndTime;

    @FXML
    private Button buttoneSave;

    @FXML
    private Button buttonCancel;

    @FXML
    private TextField textFieldTitle;

    @FXML
    private TextField textFieldDescription;

    @FXML
    private TextField textFieldContact;

    @FXML
    private TextField textFieldType;

    @FXML
    private TextField textFieldURL;

    @FXML
    private DatePicker datePickerStart;

    @FXML
    private ComboBox<String> comboBoxStartTime;

    @FXML
    private DatePicker datePickerEndTime;

    @FXML
    private ComboBox<String> comboBoxEndTime;

    @FXML
    private ComboBox<Customer> comboBoxCustomer;

    @FXML
    private ComboBox<String> comboBoxLocation;

    @FXML
    private Button buttonAdd;

    @FXML
    private Button buttonUpdate;

    @FXML
    private Button buttonDelete;
    
    @FXML
    private Button buttonAllAppointments;
    
    Parent scene;
    Stage stage;
    
    //OL
    ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    ObservableList<Customer> customers = Customer.getAllCustomers();
    ObservableList<User> users = User.getAllUsers();
    ObservableList<String> locations = FXCollections.observableArrayList();
    ObservableList<String> startTimes = FXCollections.observableArrayList();
    ObservableList<String> endTimes = FXCollections.observableArrayList();
    ObservableList<Appointment> apptTimeList;
    
    //TIMES
    private final ZoneId localZoneID = ZoneId.systemDefault(); 
    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final DateTimeFormatter dateDTF = DateTimeFormatter.ofPattern("yyyy-mm-dd");
    private final DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ZoneId utcZoneID = ZoneId.of("UTC");
    
   
    private Appointment selectedApp = new Appointment();
    private User currentUser;
    
    private boolean isWeekly;
    private boolean updatingApp = false;
    private boolean addingApp = false;
    
    
    
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        CustomersMainController custController = new CustomersMainController();
        
        tableColumnCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tableColumnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tableColumnStartTime.setCellValueFactory(new PropertyValueFactory<>("start"));
        tableColumnEndTime.setCellValueFactory(new PropertyValueFactory<>("end"));
        
        tableViewAppointments.setItems(allAppointments());
        
        datePickerStart.setValue(LocalDate.now());
        datePickerEndTime.setValue(LocalDate.now());
        
        disableFields();
        
        try{
           popCustomer();
        }//CLOSES try
        catch(SQLException e){
            System.out.println("SQLException for popCity : " + e);
        }//CLOSES catch
        
        try{
           popLocation();
        }//CLOSES try
        catch(SQLException e){
            System.out.println("SQLException for popCity : " + e);
        }//CLOSES catch
        
        popTimes(); 
        
        //Lamda Expression - Acts as a listener to populate fields based on selection
        tableViewAppointments.setOnMouseClicked((MouseEvent event) -> {
            if(event.getClickCount() >= 1) {
                displayData();
            }//CLOSES if
        });
       
 
    }//CLOSES initialize    

    @FXML
    private void onActionWeeklyButton(ActionEvent event) throws SQLException {
        System.out.println("Weekly Button Selected");
        isWeekly = true;
        tableViewAppointments.setItems(getWeeklyAppoinments());
    }//CLOSES onActionWeeklyButton
    
    @FXML
    void onActionAllAppointmentsButton(ActionEvent event) {
        System.out.println("All Appts. Button Selected");
        tableViewAppointments.setItems(allAppointments());
    }//CLOSES onActionAllAppointmentsButton

    @FXML
    private void onActionMonthlyButton(ActionEvent event) throws SQLException {
        System.out.println("Monthyl Button Selected");
        isWeekly = false;
        tableViewAppointments.setItems(getMonthlyAppointments());
    }//CLOSES onActionMonthlyButton
    
    public void updateAppointmentTable() throws SQLException {
        appointmentList.clear();
        tableViewAppointments.setItems(allAppointments());
    }//CLOSES setTable
    
    
    
    
    @FXML
    void onActionAddButton(ActionEvent event) throws IOException {
       System.out.println("Add Button Clicked");
        clearFields();
        addingApp = true;
        updatingApp = false;
        allowFields();
    }//CLOSES onActionAddButton

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
    }//CLOSES onActionBackButton

    @FXML
    void onActionCancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Required");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {

            updatingApp = false;
            addingApp = false;
            clearFields();
            disableFields();
        }//CLOSES if
        
        else {
            System.out.println("Cancel canceled.");
        }//CLOSES else
    }//CLOSES onActionCancel

    @FXML
    void onActionDatePickerEndTime(ActionEvent event) {
        LocalDate dateEnd = datePickerStart.getValue();
    }

    @FXML
    void onActionDatePickerStart(ActionEvent event) {
        LocalDate dateStart = datePickerStart.getValue();
    }

    @FXML
    private void onActionDeleteButton(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "The selected Item will be deleted. Are you sure you want to delete?");
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.isPresent() && result.get() == ButtonType.OK){
            
            selectedApp = tableViewAppointments.getSelectionModel().getSelectedItem();
       
            deleteApp(selectedApp.getAppointmentId());
            
            System.out.println(selectedApp.getAppointmentId());
            
            tableViewAppointments.setItems(allAppointments());

            System.out.println("Delete Appointment");
    
        }//CLOSES if
    }//CLOSES onActionDeleteButton

    //Delete Customer
    public void deleteApp(int appointmentId) throws SQLException{
        //Establish Connection to Database
        Connection conn = DBConnection.startConnection();
        
        //Create statement object
        DBQuery.setStatement(conn);
        
        //Get statement reference
        Statement statement = DBQuery.getStatement();
        
        
        //RAW Delete statementgetCountry
        String deleteStatement = "DELETE FROM appointment WHERE appointmentId = '" + appointmentId + "';";
        
        //Execute sql statement
        statement.execute(deleteStatement);
        
        //REMOVE FROM TABLE
        appointmentList.remove(selectedApp);
        
        //Confirm rows affected
        if(statement.getUpdateCount() > 0)
            System.out.println(statement.getUpdateCount() + " row(s) affected");
        else
            System.out.println("NO CHANGE");
        
        //Closes Connection to Database
        util.DBConnection.closeConnection();

    }//CLOSES deleteCustomer


    @FXML
    void onActionSave(ActionEvent event) throws Exception {
        
        String date = datePickerStart.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String startTime = (String) comboBoxStartTime.getValue().toString();
        String endTime = (String) comboBoxEndTime.getValue().toString();
            
        if(addingApp || updatingApp){
            if(!isNotOverlapping(startTime, endTime, date)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Appointment Overlap");
                alert.setContentText("Appointments Overlap");
                Optional<ButtonType> result = alert.showAndWait();
            }
            else{
                if(validation()){
                    if(addingApp){
                        addAppointment();
                        updateAppointmentTable();
                        clearFields();               
                    }//CLOSES if for adding customer
                    else if(updatingApp){
                        updateAppointment();                  
                        updateAppointmentTable();
                        clearFields();
                    }//CLOSING else if
                }//CLOSING if
            }//CLOSES else
        }//CLOSES if
        else{
            System.out.println("DID NOT SAVE");
        }
    }

    @FXML
    void onActionUpdateButton(ActionEvent event) {
        System.out.println("Update Button Clicked");
        addingApp = false;
        updatingApp = true;
        allowFields();
    }

    //Keeps user from entering data into text fields
    public void disableFields(){
        comboBoxCustomer.setDisable(true);
        comboBoxLocation.setDisable(true);
        comboBoxStartTime.setDisable(true);
        comboBoxEndTime.setDisable(true);
        datePickerStart.setDisable(true);
        datePickerEndTime.setDisable(true);
        textFieldContact.setDisable(true);
        textFieldDescription.setDisable(true);
        textFieldTitle.setDisable(true);
        textFieldType.setDisable(true);
        textFieldURL.setDisable(true);
        
    }//CLOSES disableFields
    
    //Allows user to enter data into text fields
    public void allowFields(){
        comboBoxCustomer.setDisable(false);
        comboBoxLocation.setDisable(false);
        comboBoxStartTime.setDisable(false);
        comboBoxEndTime.setDisable(false);
        datePickerStart.setDisable(false);
        datePickerEndTime.setDisable(false);
        textFieldContact.setDisable(false);
        textFieldDescription.setDisable(false);
        textFieldTitle.setDisable(false);
        textFieldType.setDisable(false);
        textFieldURL.setDisable(false);
    }//CLOSES allowFields
    
    //Clears textfields
    public void clearFields(){
        comboBoxCustomer.setValue(null);
        comboBoxLocation.setValue("");
        comboBoxStartTime.setValue("");
        comboBoxEndTime.setValue("");
        datePickerStart.setValue(null);
        datePickerEndTime.setValue(null);
        textFieldContact.setText("");
        textFieldDescription.setText("");
        textFieldTitle.setText("");
        textFieldType.setText("");
        textFieldURL.setText("");
    }//CLOSES clearFields
    
    //Validates Appointment
    private boolean validation(){
        Customer customer = comboBoxCustomer.getValue();
        String location = comboBoxLocation.getValue();
        String start = comboBoxStartTime.getValue();
        String end = comboBoxEndTime.getValue();
        String startDate = datePickerStart.toString();
        String endDate = datePickerEndTime.toString();
        String contact = textFieldContact.getText();
        String description = textFieldDescription.getText();
        String title = textFieldTitle.getText();
        String type = textFieldType.getText();
        String url = textFieldURL.getText();
        
        String errors= "";
        
        if (customer == null){
            errors += "Missing Customer. \n";
        }//CLOSES if
        
        if (location == null){
            errors += "Missing Location. \n";
        }//CLOSES if
        
        if (start.length() == 0){
            errors += "Missing Start Time. \n";
        }//CLOSES if
        
        if (end.length() == 0){
            errors += "Missing End Time. \n";
        }//CLOSES if
        
        if (startDate.length() == 0){
            errors += "Missing Start Date. \n";
        }//CLOSES if
        
        if (endDate.length() == 0){
            errors += "Missing Postal Code. \n";
        }//CLOSES if
        
        
        if (contact.length() == 0){
            errors += "Missing Phone Number. \n";
        }//CLOSES if
        
        if (description.length() == 0){
            errors += "Missing Phone Number. \n";
        }//CLOSES if
        
        if (title.length() == 0){
            errors += "Missing Phone Number. \n";
        }//CLOSES if
        
        if (type.length() == 0){
            errors += "Missing Phone Number. \n";
        }//CLOSES if
        
        if (url.length() == 0){
            errors += "Missing Phone Number. \n";
        }//CLOSES if
        
        
        if(errors.length() == 0){
            return true;
        }//CLOSES if
        

        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERRORS");
            alert.setContentText(errors);
            alert.showAndWait();
            
            return false;
        }//CLOSES else
    }//CLOSES validation
    
    //Populate city
    public void popLocation() throws SQLException{
        
        comboBoxLocation.setPromptText("Select Location");
        
        String sqlStatement = "SELECT city FROM city";
        
        PreparedStatement stmt = DBConnection.startConnection().prepareStatement(sqlStatement);
        
        ResultSet results = stmt.executeQuery(sqlStatement);
        
        while (results.next()){
            Customer customer = new Customer();
            customer.setCity(results.getString("city"));
            locations.add(customer.getCity());
            comboBoxLocation.setItems(locations);
        }//CLOSES while
        stmt.close();
        results.close();
    }//CLOSES popCity
    
    private void popTimes() {
        //Sets Business Hours ( 8am - 6pm )
        
        LocalTime time = LocalTime.of(8, 0, 0);
        do {
            startTimes.add(time.format(timeDTF));
            endTimes.add(time.format(timeDTF));
            time = time.plusMinutes(10);
        } while (!time.equals(LocalTime.of(18, 10, 0)));
        startTimes.remove(startTimes.size() - 1);
        endTimes.remove(0);
        
        comboBoxStartTime.setItems(startTimes);
        comboBoxEndTime.setItems(endTimes);
        comboBoxStartTime.getSelectionModel().select(LocalTime.of(8, 0, 0).format(timeDTF));
        comboBoxEndTime.getSelectionModel().select(LocalTime.of(8, 10, 0).format(timeDTF));
    }//CLOSES popTimes
    
    //Populate city
    public void popCustomer() throws SQLException{
        
        comboBoxCustomer.setPromptText("Select Customer");
        
        comboBoxCustomer.setItems(customers);

    }//CLOSES popCity
    
    //ADDS NEW CUSTOMER TO DB
    private void addAppointment() throws Exception {
        
        System.out.println("Add Button was Clicked");
        
      
        LocalDate dateStart = datePickerStart.getValue();
        LocalDate dateEnd= datePickerEndTime.getValue();//returns date value without time
        LocalTime localStartTime = LocalTime.parse(comboBoxStartTime.getSelectionModel().getSelectedItem(), timeDTF);
        LocalTime localEndTime = LocalTime.parse(comboBoxEndTime.getSelectionModel().getSelectedItem(), timeDTF);

       
        LocalDateTime startDateTime = LocalDateTime.of(dateStart, localStartTime);
        LocalDateTime endDateTime = LocalDateTime.of(dateEnd, localEndTime);
        System.out.println("localStartDateTime is: " + startDateTime);
        System.out.println("localEndDateTime is: " + endDateTime);


        ZonedDateTime startTimeUTC = startDateTime.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endTimeUTC = endDateTime.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC"));
        System.out.println("startUTC is: " + startTimeUTC);
        System.out.println("endUTC is: " + endTimeUTC);


        Timestamp sqlStartTS = Timestamp.valueOf(startTimeUTC.toLocalDateTime());
        Timestamp sqlEndTS = Timestamp.valueOf(endTimeUTC.toLocalDateTime());
        System.out.println("sqlStart timestamp is: " + sqlStartTS);
        System.out.println("sqlEnd timestamp is:" + sqlEndTS);

        Customer cust = comboBoxCustomer.getValue();
        User user = new User();
        int newCustId = cust.getCustomerId();
        int userId = user.getUserID();
        String title = textFieldTitle.getText();
        String description = textFieldDescription.getText();
        String location = comboBoxLocation.getValue();
        String contact = textFieldContact.getText();
        String type = textFieldType.getText();
        String url = textFieldURL.getText();
        Timestamp createDate = Timestamp.valueOf(LocalDateTime.now());
        String createdBy = user.getUsername();
        Timestamp lastUpdate = Timestamp.valueOf(LocalDateTime.now());
        String lastUpdateBy = user.getUsername();
        
        
  
        try {
            int newAppointmentID = -1;
            //Insert new address into DB
            PreparedStatement ps = DBConnection.startConnection().prepareStatement("INSERT INTO appointment (customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)", Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, newCustId);
            System.out.println("CustomerID: " + newCustId);
            ps.setInt(2, userId);
            ps.setString(3, title);
            ps.setString(4, description);
            ps.setString(5, location);
            ps.setString(6, contact);
            ps.setString(7, type);
            ps.setString(8, url);
            ps.setTimestamp(9, sqlStartTS);
            ps.setTimestamp(10, sqlEndTS);
            ps.setString(11, createdBy);
            ps.setString(12, lastUpdateBy);
            
            System.out.println("PS: " + ps);
            
            ps.executeUpdate();
            
            
           ResultSet rs = null;
            rs = ps.getGeneratedKeys();
            if(rs != null && rs.next()){
                newAppointmentID = rs.getInt(1);
                System.out.println("Generated AppointmentID: " + newAppointmentID);
            }
            
            //Check Rows Affected
            if(ps.getUpdateCount() > 0)
                System.out.println(ps.getUpdateCount() + " row(s) affected on appointment table");
            else
                System.out.println("NO CHANGE");
        
        }
        catch (SQLException e) {
            System.out.println("SQL " + e.getMessage());
        }
        
        clearFields();
        disableFields();
        updateAppointmentTable();
        addingApp = false;
        updatingApp = false;
        System.out.println("SAVED CUSTOMER COMPLETE");

}//CLOSES insertCustomer

    
    //Displays selected appointment data in text fields
    private void displayData(){
        if(tableViewAppointments.getSelectionModel().getSelectedItem() != null){
            
            Appointment chosen = tableViewAppointments.getSelectionModel().getSelectedItem();
            
            comboBoxCustomer.setValue(chosen.getCustomer());
            comboBoxLocation.setValue(chosen.getLocation());
            comboBoxStartTime.setUserData(chosen.getStart());
            comboBoxEndTime.setUserData(chosen.getEnd());
            datePickerStart.setValue(chosen.getDateStart());
            datePickerEndTime.setValue(chosen.getDateEnd());
            textFieldContact.setText(chosen.getContact());
            textFieldDescription.setText(chosen.getDescription());
            textFieldTitle.setText(chosen.getTitle());
            textFieldType.setText(chosen.getType());
            textFieldURL.setText(chosen.getUrl());
            
        }//CLOSES if
      
    }//CLOSES displayData
    
    private void updateAppointment() throws SQLException{
        
        Appointment tempApp = tableViewAppointments.getSelectionModel().getSelectedItem();
        
        System.out.println("Update Button was Clicked");
        
        LocalDate dateStart = datePickerStart.getValue();
        LocalDate dateEnd= datePickerEndTime.getValue();//returns date value without time
        LocalTime localStartTime = LocalTime.parse(comboBoxStartTime.getSelectionModel().getSelectedItem(), timeDTF);
        LocalTime localEndTime = LocalTime.parse(comboBoxEndTime.getSelectionModel().getSelectedItem(), timeDTF);

       
        LocalDateTime startDateTime = LocalDateTime.of(dateStart, localStartTime);
        LocalDateTime endDateTime = LocalDateTime.of(dateEnd, localEndTime);
        System.out.println("localStartDateTime is: " + startDateTime);
        System.out.println("localEndDateTime is: " + endDateTime);


        ZonedDateTime startTimeUTC = startDateTime.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endTimeUTC = endDateTime.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC"));
        System.out.println("startUTC is: " + startTimeUTC);
        System.out.println("endUTC is: " + endTimeUTC);


        Timestamp sqlStartTS = Timestamp.valueOf(startTimeUTC.toLocalDateTime());
        Timestamp sqlEndTS = Timestamp.valueOf(endTimeUTC.toLocalDateTime());
        System.out.println("sqlStart timestamp is: " + sqlStartTS);
        System.out.println("sqlEnd timestamp is:" + sqlEndTS);

        Customer cust = comboBoxCustomer.getValue();
        User user = new User();
        int appId = tempApp.getAppointmentId();
        int newCustId = cust.getCustomerId();
        int userId = user.getUserID();
        String title = textFieldTitle.getText();
        String description = textFieldDescription.getText();
        String location = comboBoxLocation.getValue();
        String contact = textFieldContact.getText();
        String type = textFieldType.getText();
        String url = textFieldURL.getText();
        Timestamp createDate = Timestamp.valueOf(LocalDateTime.now());
        String createdBy = user.getUsername();
        Timestamp lastUpdate = Timestamp.valueOf(LocalDateTime.now());
        String lastUpdateBy = user.getUsername();
        
        try{
            String query1 = ("UPDATE appointment "
                    + "SET customerId = ?, userId = ?, title = ?, description = ?, " +
                    "location = ?, contact = ?, type = ?, url = ?, start = ?, end = ?, createDate = ?, createdBy = ?, lastUpdate = ?, lastUpdateBy = ?"
                    + "WHERE appointmentId = ?");
                    
            PreparedStatement ps = DBConnection.startConnection().prepareStatement(query1);
            
            ps.setInt(1, newCustId);
            ps.setInt(2, userId);
            ps.setString(3, title);
            ps.setString(4, description);
            ps.setString(5, location);
            ps.setString(6, contact);
            ps.setString(7, type);
            ps.setString(8, url);
            ps.setTimestamp(9, sqlStartTS);
            ps.setTimestamp(10, sqlEndTS);
            ps.setTimestamp(11, createDate);
            ps.setString(12, createdBy);
            ps.setTimestamp(13, lastUpdate);
            ps.setString(14, lastUpdateBy);
            ps.setInt(15, tempApp.getAppointmentId());
            
            System.out.println("PS: " + ps);
            
            ps.executeUpdate();
            
            
            //Check Rows Affected
            if(ps.getUpdateCount() > 0)
                System.out.println(ps.getUpdateCount() + " row(s) affected on address table");
            else
                System.out.println("NO CHANGE");
            
            
        }//CLOSES try
        
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        clearFields();
        disableFields();
        addingApp = false;
        updatingApp = false;
    }//CLOSES updateAppointment
    
    public static ObservableList<Appointment> getMonthlyAppointments () {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        DateTimeFormatter preferredFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        Appointment appointment;
        LocalDate filterStart = LocalDate.now();
        LocalDate filterEnd = LocalDate.now().plusMonths(1);
        try {
            PreparedStatement stmt = DBConnection.startConnection().prepareStatement(
                        "SELECT appointment.appointmentId, appointment.customerId, appointment.userId, appointment.title, "
                            + "appointment.description, appointment.start, appointment.end, appointment.createdBy, "
                            + "appointment.location, appointment.contact, appointment.type, appointment.url, appointment.type, appointment.createDate, appointment.createdBy, appointment.lastUpdate,appointment.lastUpdateBy,customer.customerName "
                            + "FROM appointment, customer "
                            + "WHERE appointment.customerId = customer.customerId AND " 
                            + "start >= '" + filterStart + "' AND start <= '" + filterEnd + "'");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                int appointmentId = rs.getInt("appointmentId");
                int customerId = rs.getInt("customerId");
                int userId = rs.getInt("userId");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String location = rs.getString("location");
                String contact = rs.getString("contact");
                String type = rs.getString("type");
                String url = rs.getString("url");
                String customerName = rs.getString("customerName");
                LocalDateTime start = LocalDateTime.parse(rs.getString("start"), preferredFormat);
                LocalDateTime end = LocalDateTime.parse(rs.getString("end"), preferredFormat);
                Timestamp createDate = rs.getTimestamp("createDate");
                String createdBy = rs.getString("createdBy");
                Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
                String lastUpdateBy = rs.getString("lastUpdateBy");
                Appointment appt = new Appointment(appointmentId, customerName, title, description, location, contact, type, url, start, end);
                appointments.add(appt);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
        return appointments;
    }
    
    public static ObservableList<Appointment> getWeeklyAppoinments() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        DateTimeFormatter preferredFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        Appointment appointment;
        LocalDate filterStart = LocalDate.now();
        LocalDate filterEnd = LocalDate.now().plusWeeks(1);
        try {
            PreparedStatement stmt = DBConnection.startConnection().prepareStatement(
                        "SELECT appointment.appointmentId, appointment.customerId, appointment.userId, appointment.title, "
                            + "appointment.description, appointment.start, appointment.end, appointment.createdBy, "
                            + "appointment.location, appointment.contact, appointment.type, appointment.url, appointment.type, appointment.createDate, appointment.createdBy, appointment.lastUpdate,appointment.lastUpdateBy,customer.customerName "
                            + "FROM appointment, customer "
                            + "WHERE appointment.customerId = customer.customerId AND " 
                            + "start >= '" + filterStart + "' AND start <= '" + filterEnd + "'");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                int appointmentId = rs.getInt("appointmentId");
                int customerId = rs.getInt("customerId");
                int userId = rs.getInt("userId");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String location = rs.getString("location");
                String contact = rs.getString("contact");
                String type = rs.getString("type");
                String url = rs.getString("url");
                String customerName = rs.getString("customerName");
                LocalDateTime start = LocalDateTime.parse(rs.getString("start"), preferredFormat);
                LocalDateTime end = LocalDateTime.parse(rs.getString("end"), preferredFormat);
                Timestamp createDate = rs.getTimestamp("createDate");
                String createdBy = rs.getString("createdBy");
                Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
                String lastUpdateBy = rs.getString("lastUpdateBy");
                Appointment appt = new Appointment(appointmentId, customerName, title, description, location, contact, type, url, start, end);
                appointments.add(appt);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
        return appointments;
    }
    
    public static ObservableList<Appointment> allAppointments() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        DateTimeFormatter preferredFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        
        try {
            PreparedStatement stmt = DBConnection.startConnection().prepareStatement(
                    
                    "SELECT appointment.appointmentId, appointment.customerId, appointment.userId, appointment.title, "
                            + "appointment.description, appointment.start, appointment.end, appointment.createdBy, "
                            + "appointment.location, appointment.contact, appointment.type, appointment.url, appointment.type, appointment.createDate, appointment.createdBy, appointment.lastUpdate,appointment.lastUpdateBy,customer.customerName "
                            + "FROM appointment, customer "
                            + "WHERE appointment.customerId = customer.customerId "
                    
                    //"SELECT customer.customerName, appointment.type, appointment.title, appointment.start, appointment.end, FROM customer, appointment WHERE appointment.customerId = customer.customerId"
            );
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                
                int appointmentId = rs.getInt("appointmentId");
                int customerId = rs.getInt("customerId");
                int userId = rs.getInt("userId");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String location = rs.getString("location");
                String contact = rs.getString("contact");
                String type = rs.getString("type");
                String url = rs.getString("url");
                String customerName = rs.getString("customerName");
                LocalDateTime start = LocalDateTime.parse(rs.getString("start"), preferredFormat);
                LocalDateTime end = LocalDateTime.parse(rs.getString("end"), preferredFormat);
                Timestamp createDate = rs.getTimestamp("createDate");
                String createdBy = rs.getString("createdBy");
                Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
                String lastUpdateBy = rs.getString("lastUpdateBy");
                Appointment appt = new Appointment(appointmentId, customerName, title, description, location, contact, type, url, start, end);
                appointments.add(appt);
            }//CLOSES while
        }//CLOSES try
        
        catch (SQLException e) {
            System.out.println("Issue with SQL");
            e.printStackTrace();
        }//CLOSES catch
        
        return appointments;
    }//CLOSES allAppointments

    public static Appointment appointmentIn15Min() {
        Appointment appointment;
        LocalDateTime now = LocalDateTime.now();
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime zdt = now.atZone(zid);
        LocalDateTime ldt = zdt.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime ldt2 = ldt.plusMinutes(15);
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            String query = "SELECT * FROM appointment WHERE start BETWEEN '" + ldt + "' AND '" + ldt2 + "'";
            ResultSet results = statement.executeQuery(query);
            if(results.next()) {
                appointment = new Appointment(results.getInt("appointmentId"), results.getInt("customerId"), results.getString("start"),
                    results.getString("end"), results.getString("title"), results.getString("description"),
                    results.getString("location"), results.getString("contact"));
                return appointment;
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return null;
    }
    
    public static Appointment overLappingAppt() {
        Appointment appointment;
        LocalDateTime now = LocalDateTime.now();
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime zdt = now.atZone(zid);
        LocalDateTime ldt = zdt.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime ldt2 = ldt.plusMinutes(15);
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            String query = "SELECT * FROM appointment WHERE start BETWEEN '" + ldt + "' AND '" + ldt2 + "'";
            ResultSet results = statement.executeQuery(query);
            if(results.next()) {
                appointment = new Appointment(results.getInt("appointmentId"), results.getInt("customerId"), results.getString("start"),
                    results.getString("end"), results.getString("title"), results.getString("description"),
                    results.getString("location"), results.getString("contact"));
                return appointment;
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return null;
    }
    
    
    public static LocalDateTime dateTime(String time, String date) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt =  LocalDateTime.parse(date + " " + time, format).atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        return ldt;
    }
    
    //Check for overlapping
    public static boolean isNotOverlapping(String startTime, String endTime, String date) {
            try {

                LocalDateTime localStart = dateTime(startTime, date);
                LocalDateTime localEnd = dateTime(endTime, date);
                String UTCstart = localStart.toString();
                String UTCend = localEnd.toString();

                
                ResultSet overlap = DBConnection.startConnection().createStatement().executeQuery(String.format(
                           "SELECT start, end, customerName FROM appointment a INNER JOIN customer c ON a.customerId=c.customerId " +
                           "WHERE ('%s' >= start AND '%s' <= end) " +
                           "OR ('%s' <= start AND '%s' >= end) " +
                           "OR ('%s' <= start AND '%s' >= start) " +
                           "OR ('%s' <= end AND '%s' >= end)",
                           UTCstart, UTCstart, UTCend, UTCend, UTCstart, UTCend, UTCstart, UTCend));
                overlap.next();
                System.out.println("APPOINTMENT OVERLAPS AT CUSTOMER: " + overlap.getString("customerName"));
                return false;
            } catch (SQLException e) {
                System.out.println("DOES NOT OVERLAP");
                return true;
            }
    }
}
