package edu.sdsu.parking_backend;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

// Databse annotations for Spring Data JPA
@Data // desgined to work on private fields. Autocreates getLotID, setLotID, etc.
@Entity
@NoArgsConstructor
public class ParkingLot {

    @Id
    private int lotID;

    private String lotName;
    private String currentStatus;
    private String lastUpdated;
    private int capacity;
    private int occupiedSpaces;

    public ParkingLot(int lotID, String lotName, int capacity)
    {// ADDED: constructor for SeedConfig
        this.lotID    = lotID;
        this.lotName  = lotName;
        this.capacity = capacity;
        this.updateStatus(0);
    }

    public boolean updateStatus(int newOccupied) 
    {
        if (newOccupied < 0 || newOccupied > capacity) 
        {
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
    public int getAvailability() {
        return Math.max(0, capacity - occupiedSpaces);
    }


}