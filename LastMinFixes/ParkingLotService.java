package edu.sdsu.parking_backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ParkingLotService {

    private static final Logger log = LoggerFactory.getLogger(ParkingLotService.class);
    private final ParkingLotRepo repo;

    // Keep usage history in memory (not persisted yet)
    private final Map<Integer, List<UsageRecord>> history = new HashMap<>();

    public ParkingLotService(ParkingLotRepo repo) {
        this.repo = repo;
    }

    /** Return all parking lots from DB */
    public List<ParkingLot> findAll() {
        return repo.findAll();
    }

    /** Find by ID or return null */
    public ParkingLot findById(int id) {
        return repo.findById(id).orElse(null);
    }

    /** Create or update a lot */
    public ParkingLot save(ParkingLot lot) {
        if (lot == null) return null;
        lot.setLastUpdated(String.valueOf(LocalDateTime.now()));
        ParkingLot saved = repo.save(lot);
        log.info("Saved lot {} (capacity={}, occupied={}, status={})",
                saved.getLotID(), saved.getCapacity(), saved.getOccupiedSpaces(), saved.getCurrentStatus());
        return saved;
    }

    /** Update occupied count */
    public boolean updateOccupied(int lotId, int newOccupied) {
        Optional<ParkingLot> opt = repo.findById(lotId);
        if (opt.isEmpty()) {
            log.warn("Update failed: lot {} not found", lotId);
            return false;
        }

        ParkingLot lot = opt.get();
        boolean ok = lot.updateStatus(newOccupied);
        if (!ok) {
            log.warn("Update failed: occupied={} out of bounds (capacity={})",
                    newOccupied, lot.getCapacity());
            return false;
        }

        repo.save(lot);
        recordSnapshot(lotId, lot.getOccupiedSpaces(), lot.getCapacity(), LocalDateTime.now());
        log.info("Lot {} updated → occupied={}, status={}, available={}",
                lotId, lot.getOccupiedSpaces(), lot.getCurrentStatus(), lot.getAvailability());
        return true;
    }

    /** Record analytics snapshot */
    private void recordSnapshot(int lotId, int occupied, int capacity, LocalDateTime at) {
        history.computeIfAbsent(lotId, k -> new ArrayList<>())
                .add(new UsageRecord(lotId, occupied, capacity, at));
    }

    /** Delete a lot */
    public boolean remove(int lotId) {
        if (!repo.existsById(lotId)) {
            log.warn("Remove failed: lot {} not found", lotId);
            return false;
        }
        repo.deleteById(lotId);
        history.remove(lotId);
        log.info("Removed lot {} and its history", lotId);
        return true;
    }

    /** Compute busiest lots and hour of day */
    public BusiestReport generateBusiestReport(int topN) {
        List<ParkingLot> allLots = repo.findAll();
        List<BusiestReport.LotRank> ranks = allLots.stream()
                .map(lot -> {
                    List<UsageRecord> recs = history.getOrDefault(lot.getLotID(), List.of());
                    double avg = recs.stream().mapToDouble(UsageRecord::utilization).average().orElse(0.0);
                    return new BusiestReport.LotRank(lot.getLotID(),
                            lot.getLotName() != null ? lot.getLotName() : "Lot " + lot.getLotID(), avg);
                })
                .sorted(Comparator.comparingDouble(BusiestReport.LotRank::getAvgUtilization).reversed())
                .limit(Math.max(1, topN))
                .collect(Collectors.toList());

        // Calculate busiest hour
        Map<Integer, List<Double>> byHour = new HashMap<>();
        history.values().forEach(list -> list.forEach(r -> {
            int hour = r.timestamp().getHour();
            byHour.computeIfAbsent(hour, k -> new ArrayList<>()).add(r.utilization());
        }));

        int busiestHour = 0;
        double busiestAvg = 0.0;
        for (Map.Entry<Integer, List<Double>> e : byHour.entrySet()) {
            double avg = e.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            if (avg > busiestAvg) {
                busiestHour = e.getKey();
                busiestAvg = avg;
            }
        }

        return new BusiestReport(ranks, busiestHour, busiestAvg);
    }

    // === Helper Data Records ===
    public record UsageRecord(int lotId, int occupied, int capacity, LocalDateTime timestamp) {
        public double utilization() {
            return capacity == 0 ? 0.0 : (double) occupied / capacity;
        }
    }

    public static class BusiestReport {
        public static class LotRank {
            private final int lotId;
            private final String lotName;
            private final double avgUtilization;
            public LotRank(int lotId, String lotName, double avgUtilization) {
                this.lotId = lotId;
                this.lotName = lotName;
                this.avgUtilization = avgUtilization;
            }
            public int getLotId() { return lotId; }
            public String getLotName() { return lotName; }
            public double getAvgUtilization() { return avgUtilization; }
        }

        private final List<LotRank> topLots;
        private final int busiestHour;
        private final double avgUtilization;

        public BusiestReport(List<LotRank> topLots, int busiestHour, double avgUtilization) {
            this.topLots = topLots;
            this.busiestHour = busiestHour;
            this.avgUtilization = avgUtilization;
        }

        public List<LotRank> getTopLots() { return topLots; }
        public int getBusiestHour() { return busiestHour; }
        public double getAvgUtilization() { return avgUtilization; }
    }
}


