/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testrest.gps.dao;

import com.testrest.gps.bean.GPSPosition;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *  Repository Of GPSPosition For Interact with the DB
 * @author Alexandre PERROT
 */
public interface PositionRepository extends MongoRepository<GPSPosition, String> {

    public List<GPSPosition> findById(String id);

    public List<GPSPosition> findByTimeEntryBetween(Date from, Date to);
}
