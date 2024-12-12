package com.iit.oop_coursework.Real_Time_Event_Ticketing_System.service;

import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.model.Configuration;
import org.springframework.stereotype.Service;

/**
 * Service interface for managing configurations.
 */
@Service
public interface ConfigurationService {

    /**
     * Saves the given configuration.
     *
     * @param configuration the configuration to save
     * @return a message indicating the result of the save operation
     */
    String saveConfiguration(Configuration configuration);

    /**
     * Loads the current configuration.
     *
     * @return the current configuration
     */
    Configuration loadConfiguration();

    /**
     * Sets the default configuration with the given parameters.
     *
     * @param totalTickets the total number of tickets
     * @param releaseRate the rate at which tickets are released
     * @param retrievalRate the rate at which tickets are retrieved
     * @param maxCapacity the maximum capacity
     * @return the default configuration
     */
    Configuration setDefaultConfiguration(int totalTickets, int releaseRate, int retrievalRate, int maxCapacity);
}
