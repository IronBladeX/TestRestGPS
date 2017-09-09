package com.testrest.gps.controller;

import com.testrest.gps.bean.GPSPosition;
import com.testrest.gps.dao.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *  Class Of Controller for Interact with GPSPositions
 * 
 * @author Alexandre PERROT
 */
@RestController
public class EntryGPSController {
    
    // Zone Of Constant Path URL Mapping
    public static final String MAPPING_PATH = "/";
    public static final  String MAPPING_GPSPOSITION_ADD = MAPPING_PATH + "entry";   
    
    
    // Zone Of Constant Parameters
    public static final  String PARAMETER_LONGITUDE = "lo";   
    public static final  String PARAMETER_LATITUDE = "la";  
    
    
    // Zone Of Messages
    public static final  String MESSAGE_ERROR_MISSING_PARAMETER = "Error: %s parameter is missing for type %s"; 
    
    
    /**
     * Repository For DATA Access On MongoDB
     */
    @Autowired
    private PositionRepository repository;
    
    /**
     * Add Entry GPSPosition in DB At this Moment
     * @param longitude
     * @param latitude
     * @return GPSPosition
     */
    @RequestMapping(MAPPING_GPSPOSITION_ADD)
    public GPSPosition entry(@RequestParam(value=PARAMETER_LONGITUDE) double longitude, @RequestParam(value=PARAMETER_LATITUDE) double latitude) {
                
        GPSPosition positionToAdd = new GPSPosition(longitude, latitude);
        
        repository.save(positionToAdd);
        
        return positionToAdd;
    }
    
    /**
     * Error Message of Missing Params
     * @param ex
     * @return message
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        String type = ex.getParameterType();
        
        return String.format(MESSAGE_ERROR_MISSING_PARAMETER, name, type);
    }
}
