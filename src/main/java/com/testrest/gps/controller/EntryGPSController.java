package com.testrest.gps.controller;

import com.testrest.gps.bean.GPSPosition;
import com.testrest.gps.dao.PositionRepository;
import com.testrest.gps.utils.Utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        
    /** Format Date For Parameters REST */
    public static String DATE_FORMAT = "yyyy-MM-dd HH:mm";
    
    // Zone Of Constant Path URL Mapping
    public static final String MAPPING_PATH = "/";
    public static final  String MAPPING_GPSPOSITION_ADD = MAPPING_PATH + "entry";    
    public static final  String MAPPING_GPSPOSITION_VALUES = MAPPING_PATH + "values";  
    public static final  String MAPPING_GPSPOSITION_DISTANCE = MAPPING_PATH + "distance";
    public static final  String MAPPING_GPSPOSITION_DELETE = MAPPING_PATH + "delete";
    public static final  String MAPPING_GPSPOSITION_GET = MAPPING_PATH + "get";
    
    
    // Zone Of Constant Parameters
    public static final  String PARAMETER_LONGITUDE = "longitude";   
    public static final  String PARAMETER_LATITUDE = "latitude";  
    public static final  String PARAMETER_DATE_BEGIN = "begin"; 
    public static final  String PARAMETER_DATE_END = "end";  
    public static final  String PARAMETER_ID = "id";  
    
    
    // Zone Of Messages
    public static final  String MESSAGE_ERROR_MISSING_PARAMETER = "Error: %s parameter is missing for type %s"; 
    public static final  String MESSAGE_ERROR_NOT_FOUND = "not Found"; 
    public static final  String MESSAGE_DELETED = "deleted"; 
    
    
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
     * Get All GPSPosition between
     * @param dateBegin
     * @param dateEnd
     * @return 
     */
    @RequestMapping(MAPPING_GPSPOSITION_VALUES)
    public List<GPSPosition> values(@RequestParam(PARAMETER_DATE_BEGIN) String dateBegin, @RequestParam(PARAMETER_DATE_END) String dateEnd)
    {
        List<GPSPosition> positions = new ArrayList<>();
        
        //Parse Date For sort
        SimpleDateFormat ft = new SimpleDateFormat(DATE_FORMAT);
        
        Date begin; 
        Date end;
        
        try {
            begin = ft.parse(dateBegin);
            end = ft.parse(dateEnd);
        } catch(ParseException e) {
            return positions;
        }
        
        //Sort with data in DB
        positions = repository.findByTimeEntryBetween(begin, end);
                
        return positions;
    }
    
    /**
     * Calculate Distance Of All GPSPosition between
     * @param dateBegin
     * @param dateEnd
     * @return distance
     */
    @RequestMapping(MAPPING_GPSPOSITION_DISTANCE)
    public double distance(@RequestParam(PARAMETER_DATE_BEGIN) String dateBegin, @RequestParam(PARAMETER_DATE_END) String dateEnd)
    {
        List<GPSPosition> positions;
        
        //Parse Date For sort
        SimpleDateFormat ft = new SimpleDateFormat(DATE_FORMAT);
        
        Date begin; 
        Date end; 
        
        try {
            begin = ft.parse(dateBegin);
            end = ft.parse(dateEnd);
        } catch(ParseException e) {
            return 0;
        }
        
        //Sort with data in DB
        positions = repository.findByTimeEntryBetween(begin, end);
                
        //Calculate Distance between GPSPositions
        double distance = Utils.CalculateDistance(positions);
        
        return distance;
    }
    
    /**
     * Delete GPSPosition in DB with
     * @param id
     * @return deleted
     */
    @RequestMapping(MAPPING_GPSPOSITION_DELETE)
    public String delete(@RequestParam(PARAMETER_ID) String id)
    {
        repository.deleteById(id);
        return MESSAGE_DELETED;
    }
    
    /**
     * Get Position By 
     * @param id
     * @return  GPSPosition or Error Message
     */
    @RequestMapping(MAPPING_GPSPOSITION_GET)
    public Object getGPSPosition(@RequestParam(PARAMETER_ID) String id)
    {
        List<GPSPosition> positions = repository.findById(id);
        
        if (positions.isEmpty())
            return MESSAGE_ERROR_NOT_FOUND;
        
        return positions.get(0);
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
