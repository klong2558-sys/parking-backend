package edu.sdsu.parkingbackend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Staff extends User {

    private static final Logger log = LoggerFactory.getLogger(Staff.class);

    private String staffID;
    private final ParkingLotService parkingLotService;

    public Staff(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    public void setStaffID(String staffID) { this.staffID = staffID; }

    public void viewLots() {
        log.info("Staff {} viewing lots…", staffID);
        parkingLotService.findAll().forEach(lot ->
                log.info("Lot {} → {}", lot.lotID, lot.getStatus())
        );
    }

    public boolean updateStatus(int lotId, int newOccupied) {
        if (!isLoggedIn()) {
            log.warn("Staff {} attempted update without login", staffID);
            return false;
        }
        boolean ok = parkingLotService.updateOccupied(lotId, newOccupied);
        if (ok) {
            var lot = parkingLotService.findById(lotId);
            log.info("Staff {} updated Lot {} → occupied={}, status={}, available={}",
                    staffID, lotId, lot.occupiedSpaces, lot.currentStatus, lot.getAvailability());
        }
        return ok;
    }

    public String getStatus(int lotId) {
        var lot = parkingLotService.findById(lotId);
        return (lot == null) ? "Lot " + lotId + " not found." : lot.getStatus();
    }

    public int getAvailability(int lotId) {
        var lot = parkingLotService.findById(lotId);
        return (lot == null) ? -1 : lot.getAvailability();
    }
}
