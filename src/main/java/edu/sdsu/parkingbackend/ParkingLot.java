package edu.sdsu.parkingbackend;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ParkingLot {

    protected int lotID;
    protected String lotName;
    protected String currentStatus;
    protected String lastUpdated;
    protected int capacity;
    protected int occupiedSpaces;

    public boolean updateStatus(int newOccupied) {
        if (newOccupied < 0 || newOccupied > capacity) {
            System.out.println("Invalid number. Must be between 0 and " + capacity + ".");
            return false;
        }

        occupiedSpaces = newOccupied;

        // Determine current status
        if (occupiedSpaces == capacity) {
            currentStatus = "FULL";
        } else {
            currentStatus = "NOT FULL";
        }

        // Record time
        lastUpdated = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return true;
    }

    public String getStatus() {
        System.out.println(lotName + " is now " + currentStatus );
        return currentStatus;
    }


}