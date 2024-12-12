package com.iit.oop_coursework.Real_Time_Event_Ticketing_System.model;

/**
 * VIPCustomer class represents a VIP customer in the ticketing system.
 * Extends the Customer class to inherit common customer properties and behaviors.
 */
public class VIPCustomer extends Customer{
    /**
     * Constructor to initialize Customer with a ticket pool and customer name.
     *
     * @param ticketPool   the pool of tickets
     * @param customerName the name of the customer
     */
    public VIPCustomer(TicketPool ticketPool, String customerName) {
        super(ticketPool, customerName);
    }
}
