/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testrest.gps.utils;

import com.testrest.gps.bean.GPSPosition;
import static com.testrest.gps.controller.EntryGPSController.DATE_FORMAT;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *  Utils Class
 * @author Alexandre Perrot
 */
public class Utils {
    
    final static int RADIUS_EARTH = 6371;             
    
    /**
     * Calculate the Distance Between two GPSPosition 
     * @param p1 - GPSPosition 1
     * @param p2 - GPSPosition 2
     * @return Distance
     */
    public static double CalculateDistance(GPSPosition p1, GPSPosition p2)
    {        
        double latDistance = Math.toRadians(p1.latitude - p2.latitude);        
        double lonDistance = Math.toRadians(p1.longitude - p2.longitude);
        
        double a =
        Math.pow(Math.sin(latDistance / 2.0), 2)
            + Math.cos(Math.toRadians(p2.latitude))
            * Math.cos(Math.toRadians(p1.latitude))
            * Math.pow(Math.sin(lonDistance / 2.0), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = RADIUS_EARTH * c;

        return distance;
    }
    
    /**
     * Calculate of List of GPSPosition
     * @param positions - List Of GPSPositions
     * @return distance
     */
    public static double CalculateDistance(List<GPSPosition> positions)
    {
        double distance = 0;
        
        if (positions.size() < 2)
            return 0;
        
        GPSPosition lastPos = positions.get(0);
        positions.remove(0);
        
        for (GPSPosition pos : positions)
        {
            distance += CalculateDistance(lastPos, pos);
            lastPos = pos;
        }
        
        return distance;
    }
    
    public static List<GPSPosition> GetBetween(List<GPSPosition> positions, String timeBegin, String timeEnd) throws ParseException
    {
        SimpleDateFormat ft = new SimpleDateFormat(DATE_FORMAT);
                
        Date begin = ft.parse(timeBegin); 
        Date end = ft.parse(timeEnd);
        
        List<GPSPosition> result = new ArrayList<>();
        
        for(GPSPosition p : positions)
        {
            if (p.timeEntry.after(begin) && p.timeEntry.before(end))
            {
                result.add(p);
            }
        }
        
        return result;
    }
}
