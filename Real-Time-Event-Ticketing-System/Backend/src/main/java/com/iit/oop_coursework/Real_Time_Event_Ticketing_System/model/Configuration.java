package com.iit.oop_coursework.Real_Time_Event_Ticketing_System.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Configuration {
    private int totalTickets;
    private int ticketReleasedRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;


}
