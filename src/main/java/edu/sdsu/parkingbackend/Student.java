package edu.sdsu.parkingbackend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Student extends User {

    private static final Logger log = LoggerFactory.getLogger(Student.class);

    private String studentID;
    private String carInfo;
    private final ParkingLotService parkingLotService;

    public Student(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    public void setStudentInfo(String studentID, String carInfo) {
        this.studentID = studentID;
        this.carInfo = carInfo;
    }

    public void viewLots() {
        log.info("Student {} viewing lots…", studentID);
        parkingLotService.findAll().forEach(lot ->
                log.info("Lot {} → {}", lot.lotID, lot.getStatus())
        );
    }

    public boolean updateStatus(int lotId, int newOccupied) {
        if (!isLoggedIn()) {
            log.warn("Student {} attempted update without login", studentID);
            return false;
        }
        boolean ok = parkingLotService.updateOccupied(lotId, newOccupied);
        if (!ok) return false;
        ParkingLot lot = parkingLotService.findById(lotId);
        log.info("Student {} updated Lot {} → occupied={}, status={}, available={}",
                studentID, lotId, lot.occupiedSpaces, lot.currentStatus, lot.getAvailability());
        return true;
    }
}

