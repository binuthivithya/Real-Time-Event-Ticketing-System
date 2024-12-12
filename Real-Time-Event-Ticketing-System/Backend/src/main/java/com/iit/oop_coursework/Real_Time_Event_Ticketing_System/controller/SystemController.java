package com.iit.oop_coursework.Real_Time_Event_Ticketing_System.controller;

import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.service.Impl.SystemServiceImpl;
import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/system")
public class SystemController {
    @Autowired
    private SystemService systemService;

    @PostMapping("/start/{vendorCount}/{customerCount}/{vipCount}")
    public String startSystem(@PathVariable int vendorCount, @PathVariable int customerCount, @PathVariable int vipCount) throws InterruptedException {
        systemService.start(vendorCount,customerCount, vipCount);
        return "System started...";
    }

    @PostMapping("/stop")
    public String stopSystem(){
        systemService.stop();
        return "System stopped...";
    }

    @PostMapping("/addVendor")
    public void addVendor(@RequestParam String vendorName) throws InterruptedException {
        systemService.addVendor(vendorName);
    }

    @DeleteMapping("/removeVendor")
    public void removeVendor(@RequestParam String vendorName) {
        systemService.removeVendor(vendorName);
    }

    @PostMapping("/addCustomer")
    public void addCustomer(@RequestParam String customerName) {
        systemService.addCustomer(customerName);
    }

    @DeleteMapping("/removeCustomer")
    public void removeCustomer(@RequestParam String customerName) {
        systemService.removeCustomer(customerName);
    }

    @PostMapping("/addVip")
    public void addVip(@RequestParam String vipName) throws InterruptedException  {
        systemService.addVip(vipName);
    }

    @DeleteMapping("/removeVip")
    public void removeVip(@RequestParam String vipName) {
        systemService.removeVip(vipName);
    }
}
