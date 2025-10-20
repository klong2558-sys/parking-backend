package edu.sdsu.parkingbackend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ParkingLotService {

    private static final Logger log = LoggerFactory.getLogger(ParkingLotService.class);

    private final Map<Integer, ParkingLot> lots = new ConcurrentHashMap<>();

    public Collection<ParkingLot> findAll() {
        return lots.values();
    }

    public ParkingLot findById(int id) {
        return lots.get(id);
    }

    public void save(ParkingLot lot) {
        lots.put(lot.lotID, lot);
        log.info("Saved lot {} (capacity={}, occupied={}, status={})",
                lot.lotID, lot.capacity, lot.occupiedSpaces, lot.currentStatus);
    }

    public boolean updateOccupied(int lotId, int newOccupied) {
        ParkingLot lot = lots.get(lotId);
        if (lot == null) {
            log.warn("Update failed: lot {} not found", lotId);
            return false;
        }
        boolean ok = lot.updateStatus(newOccupied);
        if (!ok) {
            log.warn("Update failed: occupied={} out of bounds for lot {} (capacity={})",
                    newOccupied, lotId, lot.capacity);
            return false;
        }
        log.info("Lot {} updated → occupied={}, status={}, available={}",
                lotId, lot.occupiedSpaces, lot.currentStatus, lot.getAvailability());
        return true;
    }
}


