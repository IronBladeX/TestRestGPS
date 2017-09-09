/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testrest.gps.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Instant;
import java.util.Date;
import org.springframework.data.annotation.Id;

/**
 * Bean For GPSPosition
 * @author Alexandre PERROT
 */
public class GPSPosition {
    
    /**
     * ID Of Position
     */
    @Id
    public String id;
    
    /**
     * Longitude of Position
     */
    public Double longitude;
    
    /**
     * Latitude Of Position
     */
    public Double latitude;
    
    /**
     * Date of Entry
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Europe/Paris")
    public Date timeEntry;
    
    /**
     * Global Constructor of GPSPosition
     * @param longitude - Longitude
     * @param latitude - Latitude
     * @param timeEntry - Time of Entry
     */
    public GPSPosition(Double longitude, Double latitude, Date timeEntry)
    {        
        this.longitude = longitude;
        this.latitude = latitude;
        this.timeEntry = timeEntry;   
    }
    
    /**
     * Constructor of GPSPosition
     * @param longitude - Longitude
     * @param latitude - Latitude
     */
    public GPSPosition(Double longitude, Double latitude)
    {
        this(longitude, latitude, Date.from(Instant.now()));
    }
    
    public GPSPosition()
    {
        
    }
}
