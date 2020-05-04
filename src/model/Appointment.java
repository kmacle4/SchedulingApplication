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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DBConnection;


/**
 *
 * @author kirkmaclean
 */
public class Appointment {
    private int appointmentId;//auto generated
    private int customerId;//auto generated
    private int userId;//auto generated    
    private Customer customer;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String url;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private LocalDateTime start;
    private LocalDateTime end;
    private String customerName;    
    private Date createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;

    public Appointment(int appointmentId, int customerId, int userId, String title, String description, String location, String contact, String type, String url, LocalDateTime start, LocalDateTime end, String customerName, Date createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.start = start;
        this.end = end;
        this.customerName = customerName;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Appointment(String customerName, String type, LocalDateTime start, LocalDateTime end, String contact) {
        this.customerName = customerName;
        this.type = type;
        this.start = start;
        this.end = end;
        this.contact = contact;
    }

    
    
    public Appointment(){
    };

    public Appointment(int customerId, String type, String title, LocalDateTime start, LocalDateTime end, String contact) {
        this.customerId = customerId;
        this.type = type;
        this.title = title;
        this.start = start;
        this.end = end;
        this.contact = contact;
    }

    public Appointment(String customerName, String type, String title, LocalDateTime start, LocalDateTime end, String contact) {
        this.customerName = customerName;
        this.type = type;
        this.title = title;
        this.start = start;
        this.end = end;
        this.contact = contact;
    }

    public Appointment(String customerName, String type, LocalDateTime start, LocalDateTime end) {
        this.customerName = customerName;
        this.type = type;
        this.start = start;
        this.end = end;
    }

    public Appointment(String customerName, String title, String description, String location, String type, String url, LocalDateTime start, LocalDateTime end) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.url = url;
        this.start = start;
        this.end = end;
        this.customerName = customerName;

    }

    public Appointment(String customerName, String title, String description, String location, String contact, String type, String url, LocalDateTime start, LocalDateTime end) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.start = start;
        this.end = end;
        this.customerName = customerName;
    }
    
    public Appointment(int appointmentId, String customerName, String title, String description, String location, String contact, String type, String url, LocalDateTime start, LocalDateTime end) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.start = start;
        this.end = end;
        this.customerName = customerName;
    }

    public Appointment(int appointmentId, int customerId, String toString, String toString0, String title, String type, String customerName, String user) {
        
    }

    
    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
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

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        
    }

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    
    public boolean appointmentIn15Min() throws SQLException {
        Appointment appointment;
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        User user = new User();
        LocalDateTime now = LocalDateTime.now();
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime zdt = now.atZone(zid);
        LocalDateTime ldt = zdt.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime ldt2 = ldt.plusMinutes(15);
        LocalDateTime filterStart = LocalDateTime.now();
        LocalDateTime filterEnd = filterStart.plusMinutes(15);
        String users = user.getUsername();
        try {
            PreparedStatement stmt = DBConnection.startConnection().prepareStatement(
                        "SELECT appointment.appointmentId, appointment.customerId, appointment.userId, appointment.title, "
                            + "appointment.description, appointment.start, appointment.end, appointment.createdBy, "
                            + "appointment.location, appointment.contact, appointment.type, appointment.url, appointment.type, appointment.createDate, appointment.createdBy, appointment.lastUpdate,appointment.lastUpdateBy,customer.customerName "
                            + "FROM appointment, customer "
                            + "WHERE appointment.customerId = customer.customerId AND " 
                            + "start >= '" + ldt + "' AND start <= '" + ldt2 + "'");
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
                LocalDateTime start = LocalDateTime.parse(rs.getString("start"));
                LocalDateTime end = LocalDateTime.parse(rs.getString("end"));
                Timestamp createDate = rs.getTimestamp("createDate");
                String createdBy = rs.getString("createdBy");
                Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
                String lastUpdateBy = rs.getString("lastUpdateBy");
                Appointment appt = new Appointment(appointmentId, customerName, title, description, location, contact, type, url, start, end);
                appointments.add(appt);
                return true;
            }
            
        }
            catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }
    
    public static ObservableList<Appointment> getUpcomingAppointments() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        DateTimeFormatter preferredFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        Appointment appointment;
        LocalDateTime filterStart = LocalDateTime.now();
        LocalDateTime filterEnd = LocalDateTime.now().plusMinutes(15);
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
    
    public String get15Time() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"); 
 	LocalDateTime ldt = LocalDateTime.now();
        ZonedDateTime zdt = ldt.atZone(ZoneId.of("UTC"));
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime utcDate = zdt.withZoneSameInstant(zid); 
        DateTimeFormatter tFormatter = DateTimeFormatter.ofPattern("kk:mm"); 
	LocalTime localTime = LocalTime.parse(utcDate.toString().substring(11,16), tFormatter);
        return localTime.toString();
    }
    
   
   
}