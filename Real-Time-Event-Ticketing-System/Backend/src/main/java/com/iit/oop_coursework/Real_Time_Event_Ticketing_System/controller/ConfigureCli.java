package com.iit.oop_coursework.Real_Time_Event_Ticketing_System.controller;

import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.model.Configuration;
import com.iit.oop_coursework.Real_Time_Event_Ticketing_System.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class ConfigureCli implements CommandLineRunner{
    @Autowired
    private ConfigurationService configurationService;

    @Override
    public void run(String... args) throws Exception {
        Scanner input = new Scanner(System.in);

        System.out.println("System configuration.");

        while(true) {
            String choice;
            while (true) {
                System.out.println("Enter A to configure the system or B to use saved configuration.");

                choice = input.next();
                if (choice.equalsIgnoreCase("A") || choice.equalsIgnoreCase("B")) {
                    break;
                } else {
                    System.out.println("Invalid input.Enter A or B");
                }
            }

            if (choice.equalsIgnoreCase("A")) {
                int totalTickets;
                int maxTicketCapacity;
                int ticketReleaseRate;
                int customerRetrievalRate;

                while (true) {
                    System.out.println("Enter the Total Number of Tickets in the system: ");
                    try {
                        totalTickets = input.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input.Enter a positive integer.");
                        input.next();
                        continue;
                    }

                    if (totalTickets > 0) {
                        break;
                    } else {
                        System.out.println("Total should be a positive value.");
                    }
                }

                while (true) {
                    System.out.println("Enter the Maximum Ticket Capacity of the ticket pool: ");
                    try {
                        maxTicketCapacity = input.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Enter a positive integer.");
                        input.next();
                        continue;
                    }

                    if (maxTicketCapacity > 0 && maxTicketCapacity <= totalTickets) {
                        break;
                    } else {
                        System.out.println("Maximum Ticket Capacity should be less than or equal to Total Tickets");
                    }
                }

                while (true) {
                    System.out.println("Enter the Ticket Release Rate: ");
                    try {
                        ticketReleaseRate = input.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Enter a positive integer.");
                        input.next();
                        continue;
                    }

                    if (ticketReleaseRate > 0 && ticketReleaseRate <= maxTicketCapacity) {
                        break;
                    } else {
                        System.out.println("Ticket Release Rate should be less than or equal Maximum Ticket capacity.");
                    }
                }

                while (true) {
                    System.out.println("Enter the Customer Retrieval Rate: ");
                    try {
                        customerRetrievalRate = input.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Enter a positive integer.");
                        input.next();
                        continue;
                    }

                    if (customerRetrievalRate > 0 && customerRetrievalRate <= maxTicketCapacity) {
                        break;
                    } else {
                        System.out.println("Customer Retrieval Rate should be less than or equal Maximum Ticket capacity.");
                    }
                }

                Configuration configuration = new Configuration(
                        totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);

                configurationService.saveConfiguration(configuration);
                System.out.println("Configuration saving successful!");
                return;
            } else {
                try {
                    Configuration configuration = configurationService.loadConfiguration();
                    System.out.println("Total Tickets in the System: " + configuration.getTotalTickets());
                    System.out.println("Ticket releasing rate: " + configuration.getTicketReleasedRate());
                    System.out.println("Customer Retrieval Rate: " + configuration.getCustomerRetrievalRate());
                    System.out.println("Maximum Ticket Capacity: " + configuration.getMaxTicketCapacity());
                    return;
                } catch (Exception e) {
                    System.out.println("Couldn't load the Configuration File");
                }
            }
        }
    }
}
