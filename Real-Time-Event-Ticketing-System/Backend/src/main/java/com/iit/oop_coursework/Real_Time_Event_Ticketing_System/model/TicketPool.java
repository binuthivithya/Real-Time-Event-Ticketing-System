package com.iit.oop_coursework.Real_Time_Event_Ticketing_System.model;

import org.springframework.stereotype.Component;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class TicketPool {
    //tickets in the pool
    private List<Integer> tickets = Collections.synchronizedList(new ArrayList<>());
    // maximum capacity of ticket pool
    private int ticketPoolCapacity;
    // number of tickets released by the vendor in a second
    private int releasedRate;
    //number of tickets bought by the customer in a second
    private int retrievalRate;
    //total number of tickets in the System
    private int totalTickets;


    public synchronized void initializedTicketPool (int ticketPoolCapacity, int releasedRate, int retrievalRate, int totalTickets){
        tickets.clear();
        this.ticketPoolCapacity = ticketPoolCapacity;
        this.releasedRate = releasedRate;
        this.retrievalRate =  retrievalRate;
        this.totalTickets = totalTickets;
        System.out.println("Ticket Pool created with maximum capacity " + ticketPoolCapacity);
    }

    public synchronized boolean addTicket() throws InterruptedException {

        if (totalTickets == 0){
            System.out.println("All the tickets have been released");
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
        totalTickets -= releasedRate;

        System.out.println("Tickets released to the Ticket Pool. \nNo of tickets in the pool: " + tickets.size());

        notifyAll();
        return false;

    }

    public synchronized boolean removeTickets() throws InterruptedException{
        if (totalTickets == 0 && tickets.isEmpty()){
            System.out.println("All tickets have been sold out.");
            return false;
        }

        while (tickets.size() > retrievalRate){
            System.out.println("Not enough tickets to buy.");
        }

        int purchasingTickets = Math.min(retrievalRate, tickets.size());

        for (int i = 0; i < purchasingTickets; i++) {
            tickets.remove(0);
        }
        System.out.println("Tickets purchased from the pool.");

        notifyAll();
        return true;
    }


}

