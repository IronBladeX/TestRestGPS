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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    public static final String MAPPING_GPSPOSITION = "positions";
    public static final  String MAPPING_GPSPOSITION_ADD = MAPPING_PATH + MAPPING_GPSPOSITION;    
    public static final  String MAPPING_GPSPOSITION_VALUES = MAPPING_PATH + MAPPING_GPSPOSITION;  
    public static final  String MAPPING_GPSPOSITION_DISTANCE = MAPPING_PATH + MAPPING_GPSPOSITION + "/distance";
    public static final  String MAPPING_GPSPOSITION_DELETE = MAPPING_PATH + MAPPING_GPSPOSITION + "/{id}";
    public static final  String MAPPING_GPSPOSITION_GET = MAPPING_PATH + MAPPING_GPSPOSITION + "/{id}";
    
    
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
     * @param positionToAdd
     * @return GPSPosition
     */
    @RequestMapping(value = MAPPING_GPSPOSITION_ADD, method = RequestMethod.POST)
    public GPSPosition entry(@RequestBody GPSPosition positionToAdd) {
        
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
    @RequestMapping(value = MAPPING_GPSPOSITION_DELETE, method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable(PARAMETER_ID) String id)
    {
        if (repository.findById(id).isEmpty())
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        
        repository.deleteById(id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
    
    /**
     * Get Position By 
     * @param id
     * @return  GPSPosition or Error Message
     */
    @RequestMapping(value = MAPPING_GPSPOSITION_GET, method = RequestMethod.GET)
    public Object getGPSPosition(@PathVariable(PARAMETER_ID) String id)
    {
        List<GPSPosition> positions = repository.findById(id);
        
        if (positions.isEmpty())
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        
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
