/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DBConnection;


/**
 *
 * @author kirkmaclean
 */
public class Customer {
    
    public int customerId;
    private String address;
    private String customerName;
    private Customer custName;
    private int addressId;
    private int active;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    private String city;
    private String phone;
    private String country;
    private String postalCode;

    public Customer(int customerId, String customerName, int addressId, int active, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.addressId = addressId;
        this.active = active;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    public Customer(){
    };

    public Customer(int customerId, String address, String customerName, int addressId, int active, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy, String city, String phone, String country, String postalCode) {
        this.customerId = customerId;
        this.address = address;
        this.customerName = customerName;
        this.addressId = addressId;
        this.active = active;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        this.city = city;
        this.phone = phone;
        this.country = country;
        this.postalCode = postalCode;
    }

   public Customer(int customerId, String customerName){}

    

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
    
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Customer getCustName() {
        return custName;
    }

    public void setCustName(Customer custName) {
        this.custName = custName;
    }
    
    

    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        try {
            PreparedStatement stmt = DBConnection.startConnection().prepareStatement(
                "SELECT  * FROM customer"
                );
            
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer.customerId"));
                customer.setCustomerName(rs.getString("customer.customerName"));
                customers.add(customer);
            }//CLOSES while
        }//CLOSES try
        catch (SQLException e) {
            System.out.println("Issue with SQL");
            e.printStackTrace();
        }//CLOSES catch

        return customers;
    }//CLOSES getAllCustomers
    
    @Override
    public String toString(){
    return customerName;
    }
}