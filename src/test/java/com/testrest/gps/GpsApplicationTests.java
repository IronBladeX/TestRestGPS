package com.testrest.gps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testrest.gps.bean.GPSPosition;
import com.testrest.gps.controller.EntryGPSController;
import com.testrest.gps.dao.PositionRepository;
import com.testrest.gps.utils.Utils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Class Of UnitTest
 * @author Alexandre PERROT
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GpsApplicationTests {

    
    @Autowired
    private MockMvc mockMvc;
    
    /**
     * Repository of GPSPosition
     */
    @Autowired
    private PositionRepository repository;    
    
    
    /**
     * Test AddGPSPosition with params and check if the result is Good
     * @throws Exception 
     */
    @Test
    public void entryAddGPSPosition() throws Exception
    {
        GPSPosition position = new GPSPosition(41.0, 45.0);
        
        MvcResult result = this.mockMvc.perform(
            post(EntryGPSController.MAPPING_GPSPOSITION_ADD)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(position)))
            .andExpect(status().isOk()).andReturn();
        
        JSONObject json = new JSONObject(result.getResponse().getContentAsString());      
       
        String id = (String)json.get("id");
        
        //Execute And Check if Entry is Added
        this.mockMvc.perform(get(EntryGPSController.MAPPING_GPSPOSITION_GET, id))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$." + EntryGPSController.PARAMETER_LONGITUDE).value(41.0))
                .andExpect(jsonPath("$." + EntryGPSController.PARAMETER_LATITUDE).value(45.0));
    }
    
    /**
     * Test Values error if noParams in query
     * @throws Exception 
     */
    @Test
    public void noParamsValues() throws Exception
    {
        this.mockMvc.perform(get(EntryGPSController.MAPPING_GPSPOSITION_VALUES)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(String.format(EntryGPSController.MESSAGE_ERROR_MISSING_PARAMETER, EntryGPSController.PARAMETER_DATE_BEGIN, "String")));
    }
    
    /**
     * Test Values Between Two Dates
     * @throws Exception 
     */
    @Test
    public void valuesGPSPosition() throws Exception
    {
        //Add Data Sets
        createDataSets();
        List<GPSPosition> positions = repository.findAll();
        
        String timeBegin = "2017-09-01 10:30";
        String timeEnd = "2017-09-01 15:30";
        
        String firstID = Utils.GetBetween(positions, timeBegin, timeEnd).get(0).id;
        
        //¨Params for Values Dates
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        
        params.set(EntryGPSController.PARAMETER_DATE_BEGIN, timeBegin);
        params.set(EntryGPSController.PARAMETER_DATE_END, timeEnd);
        
        //Execute and check Result
        this.mockMvc.perform(get(EntryGPSController.MAPPING_GPSPOSITION_VALUES).params(params))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]" + EntryGPSController.PARAMETER_ID).value(firstID));
        
        ClearDB();
    }
    
    /**
     * Test Distance error if noParams in query
     * @throws Exception 
     */
    @Test
    public void noParamsDistance() throws Exception
    {
        this.mockMvc.perform(get(EntryGPSController.MAPPING_GPSPOSITION_DISTANCE)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(String.format(EntryGPSController.MESSAGE_ERROR_MISSING_PARAMETER, EntryGPSController.PARAMETER_DATE_BEGIN, "String")));
    }
    
    /**
     * Test Distance Between Two Dates
     * @throws Exception 
     */
    @Test
    public void distanceGPSPosition() throws Exception
    {
        //Add Data Sets
        createDataSets();
        List<GPSPosition> positions = repository.findAll();
        
        String timeBegin = "2017-09-01 10:30";
        String timeEnd = "2017-09-01 15:30";
        
        Double distance = Utils.CalculateDistance(Utils.GetBetween(positions, timeBegin, timeEnd));
        
        //Params for Distance Dates
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        
        params.set(EntryGPSController.PARAMETER_DATE_BEGIN, timeBegin);
        params.set(EntryGPSController.PARAMETER_DATE_END, timeEnd);
         
        //Execute and check Result
        this.mockMvc.perform(get(EntryGPSController.MAPPING_GPSPOSITION_DISTANCE).params(params))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(distance.toString()));
        
        ClearDB();
    }
    
    /**
     * Test Delete GPSPosition
     * @throws Exception 
     */
    @Test
    public void deleteGPSPosition() throws Exception
    {
        GPSPosition position = new GPSPosition(41.0, 45.0);
        
        MvcResult result = this.mockMvc.perform(
            post(EntryGPSController.MAPPING_GPSPOSITION_ADD)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(position)))
            .andExpect(status().isOk()).andReturn();
        
        JSONObject json = new JSONObject(result.getResponse().getContentAsString());      
       
        String id = (String)json.get("id");
        
        //Delete Position
        this.mockMvc.perform(delete(EntryGPSController.MAPPING_GPSPOSITION_DELETE, id))
                .andDo(print()).andExpect(status().isAccepted());
        
        
        //Check If Delete
        this.mockMvc.perform(get(EntryGPSController.MAPPING_GPSPOSITION_GET, id))
                .andDo(print()).andExpect(status().isNotFound());
    }

    /**
     * Create Data Sets for UnitTest
     * @throws Exception 
     */
    private void createDataSets() throws Exception
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat (EntryGPSController.DATE_FORMAT);
        
        List<GPSPosition> positions = new ArrayList<>();
        positions.add(new GPSPosition(46.0, 2.0, dateFormat.parse("2017-09-01 10:10")));
        positions.add(new GPSPosition(46.0, 2.5, dateFormat.parse("2017-09-01 11:00")));
        positions.add(new GPSPosition(46.001, 2.2, dateFormat.parse("2017-09-01 12:00")));
        positions.add(new GPSPosition(46.001, 2.3, dateFormat.parse("2017-09-01 13:00")));
        positions.add(new GPSPosition(46.002, 2.2, dateFormat.parse("2017-09-01 15:00")));
        positions.add(new GPSPosition(46.003, 2.1, dateFormat.parse("2017-09-01 19:00")));
        
        // Clear DB
        ClearDB();    
        
        //Add All Sets
        positions.forEach((pos) -> {
            repository.save(pos);
        });
    }
    
    /**
     * Clear All Data Sets
     */
    private void ClearDB()
    {
        repository.deleteAll();  
    }
    
    public static String asJsonString(final Object obj) throws Exception
    {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }  
}
