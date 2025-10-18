package edu.sdsu.parkingbackend;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class parkingLot {

    protected int lotID;
    protected String lotName;
    protected float lotLocation;
    protected String currentStatus;
    protected String lastUpdated;
    protected int capacity;

    public void updateStatus(String newStatus) {
        if (newStatus == null || (!newStatus.equals("Full") && !newStatus.equals("Not Full"))) {
            System.out.println(" Invalid status. Please enter 'Full' or 'Not Full'.");
            return;
        }

        currentStatus = newStatus.toUpperCase();  // Simpler & consistent
        lastUpdated = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        System.out.println("Status updated: " + lotName + " is now " + currentStatus);
        System.out.println("Last updated: " + lastUpdated);
    }

    public String getStatus() {
        System.out.println(lotName + " is now " + currentStatus );
        return currentStatus;
    }




}