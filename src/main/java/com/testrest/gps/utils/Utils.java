/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testrest.gps.utils;

import com.testrest.gps.bean.GPSPosition;
import java.util.List;


/**
 *  Utils Class
 * @author alexa
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
}
