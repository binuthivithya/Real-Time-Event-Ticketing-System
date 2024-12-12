package com.iit.oop_coursework.Real_Time_Event_Ticketing_System.service.Impl;

import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.configuration.WebSocketConfiguration;
import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.model.*;
import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.service.ConfigurationService;
import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SystemServiceImpl implements SystemService {
    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private TicketPool ticketPool;

    @Autowired
    private WebSocketConfiguration webSocketConfiguration;


    private final List<Thread> customerThreads = Collections.synchronizedList(new ArrayList<>());
    private final List<Thread> vendorThreads = Collections.synchronizedList(new ArrayList<>());
    private final List<Thread> vipThreads = Collections.synchronizedList(new ArrayList<>());

    private final List<Thread> customerThreadsBeforeStart = Collections.synchronizedList(new ArrayList<>());
    private final List<Thread> vendorThreadsThreadsBeforeStart = Collections.synchronizedList(new ArrayList<>());
    private final List<Thread> vipThreadsThreadsBeforeStart = Collections.synchronizedList(new ArrayList<>());

    boolean isRunning = false;
    @Override
    public void start(int vendorCount, int customerCount, int vipCount) throws InterruptedException {
        clearThreads();
        for (Thread vendor : vendorThreadsThreadsBeforeStart) {
            vendorThreads.add(vendor);
            vendor.start();
            vendorThreadsThreadsBeforeStart.remove(vendor);
        }

        for (Thread customer : customerThreadsBeforeStart) {
            customerThreads.add(customer);
            customer.start();
            vendorThreadsThreadsBeforeStart.remove(customer);
        }

        if (vipCount > 0) {
            for (Thread vip : vipThreadsThreadsBeforeStart) {
                vipThreads.add(vip);
                vip.start();
                vipThreadsThreadsBeforeStart.remove(vip);
            }
        }

        isRunning = true;
        broadcastSystemStatus(true);
        Configuration configuration = configurationService.loadConfiguration();

        ticketPool.initializedTicketPool(
                configuration.getMaxTicketCapacity(),
                configuration.getTicketReleasedRate(),
                configuration.getCustomerRetrievalRate(),
                configuration.getTotalTickets()
        );

        if (!vendorThreads.isEmpty()) {
            for (Thread thread : vendorThreads) {
                thread.start();
            }
        }

        if (!customerThreads.isEmpty()) {
            for (Thread thread : customerThreads) {
                thread.start();
            }
        }

        System.out.println("Ticketing system started....");

        synchronized (vendorThreads) {

            for (int i = 1; i <= vendorCount; i++) {
                addVendor("Vendor " + i);
            }
        }

        synchronized (customerThreads) {
            for (int i = 1; i <= customerCount; i++) {
                addCustomer("Customer " + i);
            }
        }
        broadcastSystemStatus(false);
    }

    @Override
    public void stop() {
        isRunning = false;
        vendorThreads.forEach(Thread::interrupt);
        customerThreads.forEach(Thread::interrupt);
        vipThreads.forEach(Thread::interrupt);
        broadcastSystemStatus(false);
        System.out.println("System has stopped...");

    }

    @Override
    public synchronized void addVendor(String vendorName) throws InterruptedException {
        Vendor vendor = new Vendor(ticketPool,vendorName);
        Thread vendorThread = new Thread(vendor);
        if (!isRunning) {
            vendorThreadsThreadsBeforeStart.add(vendorThread);
        } else {
            synchronized (vendorThreads) {
                vendorThreads.add(vendorThread);
            }
            vendorThread.start();
        }
        System.out.println(vendorName + " added.");
    }

    @Override
    public synchronized void addCustomer(String customerName) {
        Customer customer = new Customer(ticketPool,customerName);
        Thread customerThread = new Thread(customer);
        if (!isRunning) {
            customerThreadsBeforeStart.add(customerThread);
        } else {
            synchronized (customerThreads) {
                customerThreads.add(customerThread);
            }
            customerThread.start();
        }
        System.out.println(customerName + " added.");
    }

    @Override
    public synchronized void addVip(String vipName) {
        VIPCustomer vipCustomer = new VIPCustomer(ticketPool,vipName);
        Thread vipThread = new Thread(vipCustomer);
        if (!isRunning) {
            vipThreadsThreadsBeforeStart.add(vipThread);
        } else {
            synchronized (vipThreads) {
                vipThreads.add(vipThread);
            }
            vipThread.start();
        }
        System.out.println(vipName + " added.");
    }

    @Override
    public synchronized void removeVendor(String vendorName) {
        if (!isRunning) {
            vendorThreadsThreadsBeforeStart.remove(vendorThreadsThreadsBeforeStart.size()-1);
        } else {
            synchronized (vendorThreads) {
                int index = vendorThreads.size() - 1;
                Thread vendorThread = vendorThreads.get(index);
                vendorThread.interrupt();
                vendorThreads.remove(vendorThread);
            }
        }
        System.out.println(vendorName + " removed.");
    }

    @Override
    public synchronized void removeCustomer(String customerName) {
        if (!isRunning) {
            customerThreadsBeforeStart.remove(customerThreadsBeforeStart.size()-1);
        } else {
            synchronized (customerThreads) {
                int index = customerThreads.size() - 1;
                Thread customerThread = customerThreads.get(index);
                customerThread.interrupt();
                customerThreads.remove(customerThread);
            }
        }
        System.out.println(customerName + " removed");
    }

    @Override
    public synchronized void removeVip(String vipName) {
        if (!isRunning) {
            vipThreadsThreadsBeforeStart.remove(vipThreadsThreadsBeforeStart.size()-1);
        } else {
            synchronized (vipThreads) {
                int index = vipThreads.size() - 1;
                Thread vipThread = vipThreads.get(index);
                vipThread.interrupt();
                vipThreads.remove(vipThread);
            }
        }
        System.out.println(vipName + " removed");
    }

    private void broadcastSystemStatus(boolean systemStatus) {
        if (webSocketConfiguration != null) {
            webSocketConfiguration.broadcastSystemStatus(systemStatus);
        }
    }

    public void clearThreads() {
        vendorThreads.clear();
        customerThreads.clear();
        vipThreads.clear();
    }
}

