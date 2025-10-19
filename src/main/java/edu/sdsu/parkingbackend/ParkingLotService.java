package edu.sdsu.parkingbackend;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ParkingLotService {

    private final Map<Integer, ParkingLot> lots = new ConcurrentHashMap<>();

    public Collection<ParkingLot> findAll() {
        return lots.values();
    }

    public ParkingLot findById(int id) {
        return lots.get(id);
    }

    public void save(ParkingLot lot) {
        lots.put(lot.lotID, lot);
    }

    public boolean updateOccupied(int lotId, int newOccupied) {
        ParkingLot lot = lots.get(lotId);
        return lot != null && lot.updateStatus(newOccupied);
    }
}


