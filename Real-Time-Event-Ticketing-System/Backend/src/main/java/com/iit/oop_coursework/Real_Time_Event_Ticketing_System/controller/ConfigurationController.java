package com.iit.oop_coursework.Real_Time_Event_Ticketing_System.controller;

import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.model.Configuration;
import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/configuration")
public class ConfigurationController {
    @Autowired
    private ConfigurationService configurationService;

    @PostMapping
    public String saveConfiguration(@RequestBody Configuration configuration){
        return configurationService.saveConfiguration(configuration);
    }
     @GetMapping
    public Configuration readConfiguration(){
        return configurationService.loadConfiguration();
     }
}
