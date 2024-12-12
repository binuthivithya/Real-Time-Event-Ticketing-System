package com.iit.oop_coursework.Real_Time_Event_Ticketing_System.model;

import org.springframework.stereotype.Component;

/**
 * Vendor class represents a vendor in the ticketing system.
 * Implements Runnable to allow vendor actions to be run in a separate thread.
 */
public class Vendor implements Runnable {
    private final TicketPool ticketPool; // Pool of tickets
    private final String vendorName; // Name of the vendor

    /**
     * Constructor to initialize Vendor with a ticket pool and vendor name.
     *
     * @param ticketPool the pool of tickets
     * @param vendorName the name of the vendor
     */
    public Vendor(TicketPool ticketPool, String vendorName) {
        this.ticketPool = ticketPool;
        this.vendorName = vendorName;
    }

    /**
     * The run method is executed when the thread is started.
     * It simulates the vendor releasing tickets to the ticket pool.
     */
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("ticket released by " + vendorName);

                boolean isTrue = ticketPool.addTicket();

                if (isTrue) {
                    break;
                }
                Thread.sleep(1000);
            }
            Thread.currentThread().interrupt();
        } catch (InterruptedException e) {
            System.out.println(vendorName + " stopped releasing tickets.");
        }
    }
}