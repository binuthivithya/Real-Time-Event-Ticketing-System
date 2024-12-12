package com.iit.oop_coursework.Real_Time_Event_Ticketing_System.controller;

import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.model.Configuration;
import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling configuration-related requests.
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/configuration")
public class ConfigurationController {
    @Autowired // Injecting the ConfigurationService dependency
    private ConfigurationService configurationService;

    /**
     * Endpoint to save a configuration.
     *
     * @param configuration The configuration object to be saved.
     * @return A string indicating the result of the save operation.
     */
    @PostMapping("/save")
    public String saveConfiguration(@RequestBody Configuration configuration){
        return configurationService.saveConfiguration(configuration);
    }

    /**
     * Endpoint to load a configuration.
     *
     * @return The loaded configuration object.
     */
    @GetMapping("/load")
    public Configuration readConfiguration(){
        return configurationService.loadConfiguration();
    }
}
