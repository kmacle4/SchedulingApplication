/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Date;
import java.text.DateFormat;

/**
 *
 * @author kirkmaclean
 */
public class City {

    public static int getCityId;
    private int cityId;
    private String city;
    private int countryId;
    private DateFormat createDate;
    private String createdBy;
    private DateFormat lastUpdate;
    private String lastUpdateBy;
    
    City(int cityId, String city, int countryId, Date createDate, String createdBy, Date lastUpdate, String lastUpdateBy){
        this.cityId = cityId;
        this.city = city;
        this.countryId = countryId;
        //this.createDate = new DateFormat(createDate) {};
        this.createdBy = createdBy;
        this.lastUpdateBy = lastUpdateBy;
        
    }

    public City() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getCityId() {
        return cityId;
    }
    
    
}
