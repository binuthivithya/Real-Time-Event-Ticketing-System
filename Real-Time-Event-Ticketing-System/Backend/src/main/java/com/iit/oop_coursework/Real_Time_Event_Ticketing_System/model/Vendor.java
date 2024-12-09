package com.iit.oop_coursework.Real_Time_Event_Ticketing_System.model;

import org.springframework.stereotype.Component;

public class Vendor implements Runnable{
    private final TicketPool ticketPool;
    private final String vendorName;

    public Vendor(TicketPool ticketPool, String vendorName) {
        this.ticketPool = ticketPool;
        this.vendorName = vendorName;
    }

    @Override
    public void run() {
        try{
            while (!Thread.currentThread().isInterrupted()){
                System.out.println("ticket released by " + vendorName);

                boolean isTrue = ticketPool.addTicket();

                if (isTrue){
                    break;
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println(vendorName + " stopped releasing tickets.");
        }

    }
}
