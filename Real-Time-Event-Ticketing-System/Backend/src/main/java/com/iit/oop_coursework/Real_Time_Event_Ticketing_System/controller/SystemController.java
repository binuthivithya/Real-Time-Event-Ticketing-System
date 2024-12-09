package com.iit.oop_coursework.Real_Time_Event_Ticketing_System.controller;

import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.service.Impl.SystemServiceImpl;
import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemController {
    @Autowired
    private SystemService systemService;

    @PostMapping("/start/{vendorCount}/{customerCount}")
    public String startSystem(@PathVariable int vendorCount, @PathVariable int customerCount){
        systemService.start(vendorCount,customerCount);
        return "System started...";
    }

    @PostMapping("/stop")
    public String stopSystem(){
        systemService.stop();
        return "System stopped...";
    }


}
