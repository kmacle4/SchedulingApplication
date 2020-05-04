/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Address;
import model.Customer;
import model.User;
import util.DBConnection;
import util.DBQuery;

/**
 * FXML Controller class
 *
 * @author kirkmaclean
 */
public class CustomersMainController implements Initializable {

    @FXML
    private Button buttonMainMenu;

    @FXML
    private Button buttonExit;

    @FXML
    private Button buttonDelete;

    @FXML
    private Button buttonAdd;

    @FXML
    private Button buttonUpdate;

    @FXML
    private Pane labelCustomers;

    @FXML
    private TableView<Customer> tableviewCustomers;

    @FXML
    private TableColumn<Customer, String> tableColCustomerName;

    @FXML
    private TableColumn<Address, String> tableColCustomerAddress;

    @FXML
    private TableColumn<Address, String> tableColumnCustomerPhoneNumber;

    @FXML
    private TextField textName;

    @FXML
    private TextField textAddress1;

    @FXML
    private TextField textPostalCode;

    @FXML
    private TextField textPhoneNumber;

    @FXML
    private Label labelName;

    @FXML
    private Label labelAddress1;

    @FXML
    private Label labelCity;

    @FXML
    private Label labelPostalCode;

    @FXML
    private Label labelCountry;

    @FXML
    private Label labelPhoneNumber;

    @FXML
    private ComboBox<String> comboBoxCity;

    @FXML
    private ComboBox<String> comboBoxCountry;

    @FXML
    private Label labelInstructions;

    @FXML
    private Button buttonSave;

    @FXML
    private Button buttonCancel;
    
    private Customer selectedCustomer = new Customer();
    
    Parent scene;
    Stage stage;


    ObservableList<Customer> customerList = FXCollections.observableArrayList();
    ObservableList<String> cities = FXCollections.observableArrayList();
    ObservableList<String> countries = FXCollections.observableArrayList();
    
    private boolean updatingCustomer = false;
    private boolean addingCustomer = false;
    
    
    //Initializes the controller class.
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
       
        //Populate Table
        
        tableColCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tableColCustomerAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tableColumnCustomerPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phone"));
        
        tableviewCustomers.setItems(getAllCustomers());
        
        disableFields();
        
 
        try{
           popCity();
        }//CLOSES try
        catch(SQLException e){
            System.out.println("SQLException for popCity : " + e);
        }//CLOSES catch
        
        try{
           popCountry();
        }//CLOSES try
        catch(SQLException e){
            System.out.println("SQLException for popCountry: " + e);
        }//CLOSES catch
        
        //Lamda Expression - Acts as a listener to populate fields based on selection
        tableviewCustomers.setOnMouseClicked((MouseEvent event) -> {
            if(event.getClickCount() >= 1) {
                displayData();
            }//CLOSES if
        });
        
        
    }//CLOSES initialize    

    /*
    //
    //HEADER BUTTONS - MAIN MENU and EXIT
    //
    */
    
    //RETURNS TO MAIN MENU
    @FXML
    void onActionMainMenu(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel?");
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.isPresent() && result.get() == ButtonType.OK){
            
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            
            stage.setScene(new Scene(scene));
            
            stage.show();
        }//CLOSES if
    }//CLOSES onActionMainMenu
    
    //Exits Application
    @FXML
    private void onActionExitButton(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.isPresent() && result.get() == ButtonType.OK){
            System.out.println("Exit");

            System.exit(0);
        }//CLOSES if
    }//CLOSES onActionExitButton

    /*
    //ADDING - UPDATING - SAVING CUSTOMER
    */
    
 
    //ADD CUSTOMER BUTTON
    @FXML
    private void onActionAddButton(ActionEvent event) throws IOException {
        System.out.println("Add Button Clicked");
        clearFields();
        addingCustomer = true;
        updatingCustomer = false;
        allowFields();
    }//CLOSES onActionAddButton
    
    //ADDS NEW CUSTOMER TO DB
    private void insertCustomer() throws Exception {
        System.out.println("Try to insert customer");
        User user = new User();
        int newAddId = -1;
        String address = textAddress1.getText();
        String address2 = "";
        int cityId = getCityID();
        int countryId = getCountryID();
        String postalCode = textPostalCode.getText();
        String phone = textPhoneNumber.getText();
        Timestamp timeStamp = Timestamp.valueOf(LocalDateTime.now());
        String createdBy = user.getUsername();
        String lastUpdateBy = user.getUsername();
        String customerName = textName.getText();
        int active = 1;
     
        //Establish Connection to Database
        Connection conn = DBConnection.startConnection();
        
        String sqlONE = "INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)" ;
        
        PreparedStatement ps = conn.prepareStatement(sqlONE, Statement.RETURN_GENERATED_KEYS);
            
        ps.setString(1, address);
        ps.setString(2, address2);
        ps.setInt(3, cityId);
        ps.setString(4, postalCode);
        ps.setString(5, phone);
        ps.setTimestamp(6, timeStamp);
        ps.setString(7, createdBy);
        ps.setTimestamp(8, timeStamp);
        ps.setString(9, lastUpdateBy);
        
       //Execute Prepared Statement
        ps.executeUpdate();
        
        ResultSet rs = null;
        rs = ps.getGeneratedKeys();
            if(rs != null && rs.next()){
                newAddId = rs.getInt(1);
                System.out.println("Address Id : "+ rs.getInt(1));
            }
  
        //Check Rows Affected
        if(ps.getUpdateCount() > 0)
            System.out.println(ps.getUpdateCount() + " row(s) affected on address table");
        else
            System.out.println("NO CHANGE");
        
       
        
        String sqlTWO = "INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        
        PreparedStatement ps2 = conn.prepareStatement(sqlTWO);
        
        ps2.setString(1, customerName);
        ps2.setInt(2, newAddId);
        ps2.setInt(3, active);
        ps2.setTimestamp(4, timeStamp);
        ps2.setString(5, createdBy);
        ps2.setTimestamp(6, timeStamp);
        ps2.setString(7, lastUpdateBy);
        
        //Execute Prepared Statement
        ps2.executeUpdate();
        
        //Check Rows Affected
        if(ps2.getUpdateCount() > 0)
            System.out.println(ps2.getUpdateCount() + " row(s) affected on customer table");
        else
            System.out.println("NO CHANGE");
        
        clearFields();
        disableFields();
        updateCustomerTable();
        addingCustomer = false;
        updatingCustomer = false;
        System.out.println("SAVED CUSTOMER COMPLETE");

}//CLOSES insertCustomer
    

    //UPDATE CUSTOMER BUTTON
    @FXML
    private void onActionUpdateButton(ActionEvent event) {
        System.out.println("Update Button Clicked");
        addingCustomer = false;
        updatingCustomer = true;
        allowFields();
    }//CLOSES onActionUpdateButton
    
    
    // SAVE CUSTOMER BUTTON
    @FXML
    void onActionSave(ActionEvent event) throws Exception {
        if(addingCustomer || updatingCustomer){
            if(validation()){
                if(addingCustomer){
                    insertCustomer();
                    updateCustomerTable();
                    clearFields();               
                }//CLOSES if for adding customer
                else if(updatingCustomer){
                    updateCustomer();                  
                    updateCustomerTable();
                    clearFields();
                }//CLOSING updating customer
            }//CLOSING if
        }//CLOSES if
        
        else{
            System.out.println("DID NOT SAVE");
        }//CLOSES else
    }//CLOSES onActionSave
    
    
    //Deletes Customer Button
    @FXML
    private void onActionDeleteButton(ActionEvent event) throws SQLException, Exception {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "The selected Customer will be deleted along with all Appointments associated with this customer. Are you sure you want to delete?");
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.isPresent() && result.get() == ButtonType.OK){
            
            selectedCustomer = tableviewCustomers.getSelectionModel().getSelectedItem();
       
            deleteCustomer(selectedCustomer.getCustomerId());
            
            tableviewCustomers.setItems(getAllCustomers());

            System.out.println("Delete Customer");
        }//CLOSES if 
    }//CLOSES button
    
   //Delete Customer
    public void deleteCustomer(int customerId) throws SQLException{
        
        
        //Establish Connection to Database
        Connection conn = DBConnection.startConnection();
        
        //Create statement object
        DBQuery.setStatement(conn);
        
        //Get statement reference
        Statement statement = DBQuery.getStatement();
        
        
        //RAW Delete statementgetCountry
        String deleteStatement1 = "DELETE FROM appointment WHERE customerId = '" + customerId + "';";
  
        //Execute sql statement
        statement.execute(deleteStatement1);
        
        //RAW Delete statementgetCountry
        String deleteStatement2 = "DELETE FROM customer WHERE customerId = '" + customerId + "';";
  
        //Execute sql statement
        statement.execute(deleteStatement2);
        
        
        //REMOVE FROM TABLE
        customerList.remove(selectedCustomer);
        
        //Confirm rows affected
        if(statement.getUpdateCount() > 0)
            System.out.println(statement.getUpdateCount() + " row(s) affected");
        else
            System.out.println("NO CHANGE");
        
        //Closes Connection to Database
        util.DBConnection.closeConnection();

    }//CLOSES deleteCustomer

    //UPDATES CUSTOMER
    public void updateCustomerTable() throws SQLException {
        
        customerList.clear();
        
        //create statement object
        Statement stmt = DBConnection.startConnection().createStatement();
        
        String sqlStatement = "SELECT customerId, customerName, address.addressId, address, phone, address.cityId, city, address.postalCode, country FROM customer, address, city, country " +
                                "WHERE customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId";
        
        ResultSet result = stmt.executeQuery(sqlStatement);
        

        //get all records from resultset object
        while (result.next()) {
            Customer cust = new Customer();
            
            cust.setCustomerId(result.getInt("customerId"));
            cust.setCustomerName(result.getString("customerName"));
            cust.setAddress(result.getString("address"));
            cust.setCity(result.getString("city"));
            cust.setCountry(result.getString("country"));
            cust.setPostalCode(result.getString("postalCode"));
            cust.setPhone(result.getString("phone"));
            customerList.addAll(cust);
        }//CLOSES while
        tableviewCustomers.setItems(customerList);

    }//CLOSES updateCustomerTable
    
    //Displays selected customers data in text fields
    private void displayData(){
        if(tableviewCustomers.getSelectionModel().getSelectedItem() != null){
            Customer chosen = tableviewCustomers.getSelectionModel().getSelectedItem();
            textName.setText(chosen.getCustomerName());
            textAddress1.setText(chosen.getAddress());
            comboBoxCity.setValue(chosen.getCity());
            comboBoxCountry.setValue(chosen.getCountry());
            textPostalCode.setText(chosen.getPostalCode());
            textPhoneNumber.setText(chosen.getPhone());
            
        }//CLOSES if
      
    }//CLOSES displayData

    //Clears textfields
    public void clearFields(){
        textName.setText("");
        textAddress1.setText("");
        comboBoxCity.setValue("");
        comboBoxCountry.setValue("");
        textPostalCode.setText("");
        textPhoneNumber.setText("");
    }//CLOSES clearFields
    
    //Allows user to enter data into text fields
    public void allowFields(){
        textName.setDisable(false);
        textAddress1.setDisable(false);
        comboBoxCity.setDisable(false);
        comboBoxCountry.setDisable(false);
        textPostalCode.setDisable(false);
        textPhoneNumber.setDisable(false);
    }//CLOSES allowFields
    
    //Keeps user from entering data into text fields
    public void disableFields(){
        textName.setDisable(true);
        textAddress1.setDisable(true);
        comboBoxCity.setDisable(true);
        comboBoxCountry.setDisable(true);
        textPostalCode.setDisable(true);
        textPhoneNumber.setDisable(true);
    }//CLOSES disableFields
    
    
    
    //Validates Customer
    private boolean validation(){
        String name = textName.getText();
        String address = textAddress1.getText();
        String city = comboBoxCity.getValue();
        String country = comboBoxCountry.getValue();
        String postalCode = textPostalCode.getText();
        String phoneNumber = textPhoneNumber.getText();
        
        String errors= "";
        
        if (name.length() == 0 || !name.matches("[a-zA-Z_]+")){
            errors += "Missing or Invalid Name. \n";
        }//CLOSES if
        
        if (address.length() == 0){
            errors += "Missing Address. \n";
        }//CLOSES if
        
        if (city == null){
            errors += "Missing City. \n";
        }//CLOSES if
        
        if (country == null){
            errors += "Missing Country. \n";
        }//CLOSES if
        
        if (postalCode.length() == 0 || !postalCode.matches("[0-9]+")){
            errors += "Missing or Invalid Postal Code. \n";
        }//CLOSES if
        
        
        if (phoneNumber.length() == 0 || !phoneNumber.matches("[0-9]+")){
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
    
    public void updateCustomer() throws SQLException, Exception {
        
        Customer tempCustomer = tableviewCustomers.getSelectionModel().getSelectedItem();
        User user = new User();
        String address = textAddress1.getText();
        String address2 = "";
        int cityId = getCityID();
        int countryId = getCountryID();
        String postalCode = textPostalCode.getText();
        String phone = textPhoneNumber.getText();
        Timestamp timeStamp = Timestamp.valueOf(LocalDateTime.now());
        String createdBy = user.getUsername();
        String lastUpdateBy = user.getUsername();
        String customerName = textName.getText();
        
        try {

            String query1 = ("UPDATE address, customer, city, country "
                    + "SET address = ?, address2 = ?, address.cityId = ?, postalCode = ?, phone = ?, " +
                    "address.lastUpdate = CURRENT_TIMESTAMP, address.lastUpdateBy = ? "
                    + "WHERE customer.customerId = ? AND customer.addressId = address.addressId AND address.cityId = city.cityId " +
                    "AND city.countryId = country.countryId");

            PreparedStatement ps = DBConnection.startConnection().prepareStatement(query1);
            ps.setString(1, address);
            ps.setString(2, address2);
            ps.setInt(3, cityId);
            ps.setString(4, postalCode);
            ps.setString(5, phone);
            ps.setString(6, user.getUsername());
            ps.setInt(7, tempCustomer.getCustomerId());
            ps.executeUpdate();
            
            //Used to check addressId
            System.out.println("Address table updated at addressId: " + tempCustomer.getAddressId());
                
            //Check Rows Affected
            if(ps.getUpdateCount() > 0)
                System.out.println(ps.getUpdateCount() + " row(s) affected on address table");
            else
                System.out.println("NO CHANGE");

            String query2 = ("UPDATE customer, address, city "
                    + "SET customerName = ?, customer.lastUpdate = CURRENT_TIMESTAMP, customer.lastUpdateBy = ? "
                    + "WHERE customer.customerId = ? AND customer.addressId = address.addressId AND address.cityId = city.cityId");


            PreparedStatement psc = DBConnection.startConnection().prepareStatement(query2);
            psc.setString(1, customerName);
            psc.setString(2, user.getUsername());
            psc.setInt(3, tempCustomer.getCustomerId());
            psc.executeUpdate();

            //Used to check addressId
            System.out.println("Customer table updated at customerId: " + tempCustomer.getCustomerId());
            
            if(psc.getUpdateCount() > 0)
                System.out.println(psc.getUpdateCount() + " row(s) affected on customer table");
            else
                System.out.println("NO CHANGE");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        clearFields();
        disableFields();
        addingCustomer = false;
        updatingCustomer = false;
    }
    
    
    //Populate city
    public void popCity() throws SQLException{
        String sqlStatement = "SELECT city FROM city";
        
        PreparedStatement stmt = DBConnection.startConnection().prepareStatement(sqlStatement);
        
        ResultSet results = stmt.executeQuery(sqlStatement);
        
        while (results.next()){
            Customer customer = new Customer();
            customer.setCity(results.getString("city"));
            cities.add(customer.getCity());
            comboBoxCity.setItems(cities);
        }//CLOSES while
        stmt.close();
        results.close();
    }//CLOSES popCity
    
    //Populate city
    public void popCountry() throws SQLException{
        String sqlStatement = "SELECT country FROM country";
        
        PreparedStatement stmt = DBConnection.startConnection().prepareStatement(sqlStatement);
        
        ResultSet results = stmt.executeQuery(sqlStatement);
        
        while (results.next()){
            Customer customer = new Customer();
            customer.setCountry(results.getString("country"));
            countries.add(customer.getCountry());
            comboBoxCountry.setItems(countries);
        }//CLOSES while
        
        stmt.close();
        results.close();
    }//CLOSES popCity
    
    
    
    
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        try {
            PreparedStatement stmt = DBConnection.startConnection().prepareStatement(
                "SELECT  * "
                    + "FROM customer, address, city, country "
                    + "WHERE customer.addressId = address.addressId "
                    + "AND address.cityId = city.cityId AND city.countryId = country.countryId"
                );
            
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer.customerId"));
                customer.setCustomerName(rs.getString("customer.customerName"));
                customer.setAddress(rs.getString("address.address"));
                customer.setCity(rs.getString("city.city"));
                customer.setCountry(rs.getString("country.country"));
                customer.setPostalCode(rs.getString("address.postalCode"));
                customer.setPhone(rs.getString("address.phone"));
                customers.add(customer);
            }//CLOSES while
        }//CLOSES try
        catch (SQLException e) {
            System.out.println("Issue with SQL");
            e.printStackTrace();
        }//CLOSES catch

        return customers;
    }//CLOSES getAllCustomers
   
    
    @FXML
    void onActionCancel(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Required");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {

            updatingCustomer = false;
            addingCustomer = false;
            clearFields();
            disableFields();
        }//CLOSES if
        
        else {
            System.out.println("Cancel canceled.");
        }//CLOSES else
    }//CLOSES onActionCancel
    

    private int getCityID() throws SQLException {
        
        int cityID = -1;

        //create statement object
        Statement statement = DBConnection.startConnection().createStatement();

        //write SQL statement
        String sqlStatement = "SELECT cityID FROM city WHERE city.city ='" + comboBoxCity.getValue() + "'";

        //create resultset object
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()) {
            cityID = result.getInt("cityId");
        }
        return cityID;
    }
    
    private int getCustID() throws SQLException {
        
        int customerId = -1;

        //create statement object
        Statement statement = DBConnection.startConnection().createStatement();

        //write SQL statement
        String sqlStatement = "SELECT customerId FROM customer WHERE customer.customerId ='" + customerId + "'";

        //create resultset object
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()) {
            customerId = result.getInt("customerId");
        }
        return customerId;
    }
    
    public int getCountryID() throws SQLException, Exception {
        
        String countryString = comboBoxCountry.getValue();
        int countryValue = 0;
        
        if("US".equals(countryString)){
            countryValue = 1;
        }
        
        if("Canada".equals(countryString)){
            countryValue = 2;
        }
        
        if("Norway".equals(countryString)){
            countryValue = 3;
        }
        
        
        return countryValue;
    }
    
    public int newAddressId() throws SQLException{
        int count = 0;
        
        
        //Establish Connection to Database
        Connection conn = DBConnection.startConnection();
          
        //Create statement object
        DBQuery.setStatement(conn);
        
        //Get statement reference
        Statement statement = DBQuery.getStatement();
        
        String sql = "SELECT COUNT(address) FROM address";
        
        statement.execute(sql);
        
        //Get ResultSet
        ResultSet rs = statement.getResultSet();

        //Forward Scroll ResultSet
        while(rs.next()){
            count++;
        }
        
        return count;
    }
    
    
}
