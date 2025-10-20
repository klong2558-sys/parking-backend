package edu.sdsu.parkingbackend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Admin extends User {

    private static final Logger log = LoggerFactory.getLogger(Admin.class);

    private String adminID;
    private final ParkingLotService parkingLotService;

    public Admin(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public void viewLots() {
        log.info("Admin {} viewing lots…", adminID);
        parkingLotService.findAll().forEach(lot ->
                log.info("Lot {} → {}", lot.lotID, lot.getStatus())
        );
    }

    public boolean updateStatus(int lotID, int newOccupied, int newCapacity) {
        if (!isLoggedIn()) {
            log.warn("Admin {} update denied (not logged in)", adminID);
            return false;
        }
        ParkingLot lot = parkingLotService.findById(lotID);
        if (lot == null) {
            log.warn("Admin {} update failed: lot {} not found", adminID, lotID);
            return false;
        }
        lot.capacity = newCapacity;
        boolean ok = parkingLotService.updateOccupied(lotID, newOccupied);
        if (ok) log.info("Admin {} updated Lot {} → capacity={}, occupied={}, status={}",
                adminID, lotID, newCapacity, lot.occupiedSpaces, lot.currentStatus);
        return ok;
    }

    public boolean manageUsers(String operation, String targetEmail) {
        if (!isLoggedIn()) {
            log.warn("Admin {} manageUsers denied (not logged in)", adminID);
            return false;
        }
        if (operation == null || targetEmail == null) {
            log.warn("manageUsers invalid args: op={}, email={}", operation, targetEmail);
            return false;
        }

        String op = operation.trim().toUpperCase();

        switch (op) {
            case "DELETE":
                boolean ok = UserService.deleteByEmail(targetEmail);
                if (ok) {
                    log.info("Admin {} deleted user {}", adminID, targetEmail);
                }
                return ok;

            default:
                log.warn("Unsupported manageUsers operation: {}", op);
                return false;
        }
    }
}




