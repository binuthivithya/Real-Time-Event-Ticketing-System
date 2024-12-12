package com.iit.oop_coursework.Real_Time_Event_Ticketing_System.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.model.Configuration;
import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.service.ConfigurationService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * Service implementation for managing configuration settings.
 */
@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    // Path to the configuration file
    private final String C_FILE = "configure.json";

    /**
     * Saves the given configuration to a file.
     *
     * @param configuration the configuration to save
     * @return a message indicating success or failure
     */
    @Override
    public String saveConfiguration(Configuration configuration) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(C_FILE), configuration);
            return "Configuration saved successfully";
        } catch (IOException e) {
            return "Error saving configuration" + e.getMessage();
        }
    }

    /**
     * Loads the configuration from a file.
     *
     * @return the loaded configuration, or a default configuration if the file does not exist
     */
    @Override
    public Configuration loadConfiguration() {
        File configurationFile = new File(C_FILE);
        if (!configurationFile.exists()) {
            System.out.println("Configuration file can't be found.\nDefault configuration is loading...");
            return setDefaultConfiguration(10000, 100, 40, 200);
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(new File(C_FILE), Configuration.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sets the default configuration.
     *
     * @param totalTickets   the total number of tickets
     * @param releaseRate    the rate at which tickets are released
     * @param retrievalRate  the rate at which tickets are retrieved
     * @param maxCapacity    the maximum capacity of tickets
     * @return the default configuration
     */
    @Override
    public Configuration setDefaultConfiguration(int totalTickets, int releaseRate, int retrievalRate, int maxCapacity) {
        return new Configuration(totalTickets, releaseRate, retrievalRate, maxCapacity);
    }
}
