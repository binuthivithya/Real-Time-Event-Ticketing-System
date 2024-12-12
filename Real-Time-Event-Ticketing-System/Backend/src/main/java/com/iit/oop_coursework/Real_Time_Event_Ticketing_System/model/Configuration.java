package com.iit.oop_coursework.Real_Time_Event_Ticketing_System.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Configuration class represents the configuration settings for the ticketing system.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {
    private int totalTickets; // Total number of tickets available
    private int ticketReleasedRate; // Rate at which tickets are released
    private int customerRetrievalRate; // Rate at which customers retrieve tickets
    private int maxTicketCapacity; // Maximum ticket capacity

}