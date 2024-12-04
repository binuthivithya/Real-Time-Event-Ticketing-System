package com.iit.oop_coursework.Real_Time_Event_Ticketing_System.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.model.Configuration;
import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.service.ConfigurationService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class ConfigurationServiceImpl implements ConfigurationService{

    private final String C_FILE = "configure.json";

    @Override
    public String saveConfiguration(Configuration configuration) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(C_FILE), configuration);
            return "Configuration saved successfully";
        }catch(IOException e){
            return "Error saving configuration"+e.getMessage();
        }
    }


    @Override
    public Configuration loadConfiguration() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(new File(C_FILE), Configuration.class);

        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
