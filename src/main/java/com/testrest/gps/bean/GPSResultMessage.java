/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testrest.gps.bean;

/**
 *  Class For Message Result
 * 
 * @author Perrot Alexandre
 */
public class GPSResultMessage {
    
    public String id;
    
    public String message;
    
    public Boolean state;
    
    public GPSResultMessage(String id, String message, Boolean state)
    {
        this.id = id;
        this.message = message;
        this.state = state;
    }
}
