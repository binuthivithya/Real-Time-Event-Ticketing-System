package com.iit.oop_coursework.Real_Time_Event_Ticketing_System.service;

import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.model.Configuration;
import org.springframework.stereotype.Service;

@Service
public interface ConfigurationService {
    String saveConfiguration(Configuration configuration);
    Configuration loadConfiguration();

    Configuration setDefaultConfiguration(int totalTickets, int releaseRate, int retrievalRate, int maxCapacity);
}
