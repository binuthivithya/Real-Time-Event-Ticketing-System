package com.iit.oop_coursework.Real_Time_Event_Ticketing_System.service.Impl;

import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.controller.ConfigureCli;
import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.model.Configuration;
import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.model.Customer;
import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.model.TicketPool;
import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.model.Vendor;
import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.service.ConfigurationService;
import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SystemServiceImpl implements SystemService {
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private TicketPool ticketPool;
    private final List<Thread> customerThreads = new ArrayList<>();
    private final List<Thread> vendorThreads = new ArrayList<>();
    @Override
    public void start(int vendorCount,int customerCount) {
        Configuration configuration = configurationService.loadConfiguration();

        ticketPool.initializedTicketPool(
                configuration.getMaxTicketCapacity(),
                configuration.getTicketReleasedRate(),
                configuration.getCustomerRetrievalRate(),
                configuration.getTotalTickets()
        );

        System.out.println("Ticketing system started....");

        synchronized (vendorThreads) {

            for (int i = 1; i <= vendorCount; i++) {
                addVendor("Vendor" + i);
            }
        }

        synchronized (customerThreads) {

            for (int i = 0; i < customerCount; i++) {

                addCustomer("Customer" + i);
            }
        }



    }

    @Override
    public void stop() {
        vendorThreads.forEach(Thread::interrupt);
        customerThreads.forEach(Thread::interrupt);

        System.out.println("System has stopped...");
    }

    @Override
    public void addVendor(String vendorName) {
        Vendor vendor = new Vendor(ticketPool,vendorName);
        Thread vendorThread = new Thread(vendor);
        vendorThreads.add(vendorThread);
        vendorThread.start();
    }

    @Override
    public void addCustomer(String customerName) {
        Customer customer = new Customer(ticketPool,customerName);
        Thread customerThread = new Thread(customer);
        customerThreads.add(customerThread);
        customerThread.start();
    }

    @Override
    public void removeVendor(String vendorName) {
        int index = vendorThreads.size()-1;
        Thread vendorThread = vendorThreads.get(index);
        vendorThread.interrupt();
        vendorThreads.remove(vendorThread);
    }

    @Override
    public void removeCustomer(String customerName) {
        int index = customerThreads.size()-1;
        Thread customerThread = customerThreads.get(index);
        customerThread.interrupt();
        customerThreads.remove(customerThread);
    }


}

