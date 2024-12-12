package com.iit.oop_coursework.Real_Time_Event_Ticketing_System.service;

import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.model.Configuration;
import org.springframework.stereotype.Service;

@Service
public interface SystemService {
    void start(int vendorCount, int customerCount, int vipCount) throws InterruptedException;

    void stop();
    void addVendor(String vendorName) throws InterruptedException;
    void addCustomer(String customerName);
    void addVip(String vipName);
    void removeVendor(String vendorName);
    void removeCustomer(String customerName);
    void removeVip(String vipName);
}
