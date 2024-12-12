package com.iit.oop_coursework.Real_Time_Event_Ticketing_System.model;

/**
 * Customer class represents a customer in the ticketing system.
 * Implements Runnable to allow customer actions to be run in a separate thread.
 */
public class Customer implements Runnable {
    private final TicketPool ticketPool; // Pool of tickets
    private final String customerName; // Name of the customer

    /**
     * Constructor to initialize Customer with a ticket pool and customer name.
     *
     * @param ticketPool the pool of tickets
     * @param customerName the name of the customer
     */
    public Customer(TicketPool ticketPool, String customerName) {
        this.ticketPool = ticketPool;
        this.customerName = customerName;
    }

    /**
     * The run method is executed when the thread is started.
     * It simulates the customer trying to retrieve tickets from the ticket pool.
     */
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("ticket released by " + customerName);

                boolean isTrue = ticketPool.removeTickets(customerName);

                if (isTrue) {
                    break;
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println(customerName + " has stopped...");
        }
    }
}