package com.iit.oop_coursework.Real_Time_Event_Ticketing_System.model;

import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.configuration.WebSocketConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TicketPool class represents the pool of tickets in the ticketing system.
 */
@Component
public class TicketPool {
    private List<Integer> tickets = Collections.synchronizedList(new ArrayList<>()); // Tickets in the pool
    private int ticketPoolCapacity; // Maximum capacity of ticket pool
    private int releasedRate; // Number of tickets released by the vendor in a second
    private int retrievalRate; // Number of tickets bought by the customer in a second
    private int totalTickets; // Total number of tickets in the system

    @Autowired
    private WebSocketConfiguration webSocketConfiguration; // WebSocket configuration

    /**
     * Initializes the ticket pool with the given parameters.
     *
     * @param ticketPoolCapacity the maximum capacity of the ticket pool
     * @param releasedRate the number of tickets released by the vendor in a second
     * @param retrievalRate the number of tickets bought by the customer in a second
     * @param totalTickets the total number of tickets in the system
     */
    public synchronized void initializedTicketPool (int ticketPoolCapacity, int releasedRate, int retrievalRate, int totalTickets){
        tickets.clear();
        this.ticketPoolCapacity = ticketPoolCapacity;
        this.releasedRate = releasedRate;
        this.retrievalRate =  retrievalRate;
        this.totalTickets = totalTickets;
        System.out.println("Ticket Pool created with maximum capacity " + ticketPoolCapacity);
        broadcastCurrentNoOfTickets(tickets.size());
    }

    /**
     * Adds tickets to the ticket pool.
     *
     * @return true if all tickets have been released, false otherwise
     * @throws InterruptedException if the thread is interrupted
     */
    public synchronized boolean addTicket() throws InterruptedException {
        broadcastSystemStatus(true);
        if (totalTickets <= 0){
            System.out.println("All the tickets have been released");
            broadcastSystemStatus(true);
            return true;
        }

        while(ticketPoolCapacity < releasedRate + tickets.size()){
            System.out.println("Vendor is waiting. Ticket pool is full!!!");
            wait();
        }

        int ticketsToRelease = Math.min(ticketPoolCapacity, releasedRate);
        for (int i = 0; i < ticketsToRelease; i++) {
            tickets.add(1);
        }
        totalTickets -= ticketsToRelease;
        broadcastCurrentNoOfTickets(tickets.size());

        System.out.println("Tickets released to the Ticket Pool. \nNo of tickets in the pool: " + tickets.size());

        notifyAll();
        broadcastSystemStatus(true);
        return false;

    }

    /**
     * Removes tickets from the ticket pool for a given customer.
     * This method is synchronized to ensure thread safety.
     *
     * @param customerName the name of the customer purchasing tickets
     * @return true if all tickets have been sold out, false otherwise
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public synchronized boolean removeTickets(String customerName) throws InterruptedException{
        broadcastSystemStatus(true);
        System.out.println(customerName + " is purchasing tickets...");

        if (totalTickets <= 0 && tickets.isEmpty()){
            System.out.println("All tickets have been sold out.");
            broadcastSystemStatus(false);
            return true;
        }

        while (totalTickets != 0 && tickets.size() < retrievalRate){
            System.out.println("Not enough tickets to buy.");
            wait();
        }

        int purchasingTickets = Math.min(retrievalRate, tickets.size());

        for (int i = 0; i < purchasingTickets; i++) {
            tickets.remove(0);
        }

        broadcastCurrentNoOfTickets(tickets.size());
        System.out.println("Tickets purchased from the pool. No. of tickets in the pool " + tickets.size());

        notifyAll();
        return false;
        }

        /**
         * Broadcasts the current system status to the WebSocket.
         *
         * @param systemStatus the current status of the system
         */
        private void broadcastSystemStatus(boolean systemStatus) {
        if (webSocketConfiguration != null) {
            webSocketConfiguration.broadcastSystemStatus(systemStatus);
        }
        }

        /**
         * Broadcasts the current number of tickets to the WebSocket.
         *
         * @param numberOfTickets the current number of tickets in the pool
         */
        private void broadcastCurrentNoOfTickets(int numberOfTickets) {
        if (webSocketConfiguration != null) {
            webSocketConfiguration.broadcastingCurrentNoOfTickets(numberOfTickets);
        }
    }
}

