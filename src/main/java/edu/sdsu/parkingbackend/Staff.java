package edu.sdsu.parkingbackend;

import org.springframework.stereotype.Component;
@Component

public class Staff extends User{

    private String staffID;
    private String carInfo;
    private final ParkingLotService parkingLotService;

    public Staff(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    public void setStaffInfo(String staffID, String carInfo) {
        this.staffID = staffID;
        this.carInfo = carInfo;
    }

    public void viewLots() {
        System.out.println("Parking Availability for Staff " + staffID + ":");
        parkingLotService.findAll().forEach(lot -> {
            System.out.println("Lot " + lot.lotID + " → " + lot.getStatus());
        });
    }

    public boolean updateStatus(int lotId, int newOccupied) {
        boolean success = parkingLotService.updateOccupied(lotId, newOccupied);

        if (success) {
            ParkingLot updatedLot = parkingLotService.findById(lotId);
            System.out.println("Staff " + staffID + " updated Lot " + lotId + ":");
            System.out.println("   Status: " + updatedLot.currentStatus);
            System.out.println("   Available: " + (updatedLot.capacity - updatedLot.occupiedSpaces));
        } else {
            System.out.println("Update failed. Invalid lot ID or invalid occupied value.");
        }

        return success;
    }
}
