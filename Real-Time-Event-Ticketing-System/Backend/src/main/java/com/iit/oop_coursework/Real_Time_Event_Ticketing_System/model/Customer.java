package com.iit.oop_coursework.Real_Time_Event_Ticketing_System.model;

public class Customer implements Runnable{
    private final TicketPool ticketPool;
    private final String customerName;

    public Customer(TicketPool ticketPool, String customerName) {
        this.ticketPool = ticketPool;
        this.customerName = customerName;
    }


    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {

                boolean isTrue = ticketPool.removeTickets();

                if (isTrue){
                    break;
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Vendor has stopped...");
        }

    }
}
