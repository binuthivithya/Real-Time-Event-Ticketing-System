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
        File configurationFile = new File(C_FILE);
        if (!configurationFile.exists()){
            System.out.println("Configuration file can't be found.\nDefault configuration is loading...");
            return setDefaultConfiguration(10000,100,40,200);
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(new File(C_FILE), Configuration.class);

        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Configuration setDefaultConfiguration(int totalTickets, int releaseRate, int retrievalRate, int maxCapacity)
    {
        return new Configuration(totalTickets,releaseRate,retrievalRate,maxCapacity);
    }
}
