package edu.sdsu.parking_backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service layer handling parking lot management and usage analytics.
 * Currently uses in-memory data; replace with a repository for persistence.
 */
@Service
public class ParkingLotService {

    private static final Logger log = LoggerFactory.getLogger(ParkingLotService.class);

    private final Map<Integer, ParkingLot> lots = new ConcurrentHashMap<>();
    private final Map<Integer, List<UsageRecord>> history = new ConcurrentHashMap<>();

    /** Return all lots. */
    public Collection<ParkingLot> findAll() {
        return lots.values();
    }

    /** Return one lot by ID, or null if not found. */
    public ParkingLot findById(int id) {
        return lots.get(id);
    }

    /** Save or update a parking lot. */
    public void save(ParkingLot lot) {
        if (lot == null) return;
        lots.put(lot.getLotID(), lot);
        log.info("Saved lot {} (capacity={}, occupied={}, status={})",
                lot.getLotID(), lot.getCapacity(), lot.getOccupiedSpaces(), lot.getCurrentStatus());
    }

    /**
     * Update the number of occupied spaces in a given lot.
     * @return true if update succeeded.
     */
    public boolean updateOccupied(int lotId, int newOccupied) {
        ParkingLot lot = lots.get(lotId);
        if (lot == null) {
            log.warn("Update failed: lot {} not found", lotId);
            return false;
        }

        boolean ok = lot.updateStatus(newOccupied);
        if (!ok) {
            log.warn("Update failed: occupied={} out of bounds for lot {} (capacity={})",
                    newOccupied, lotId, lot.getCapacity());
            return false;
        }

        recordSnapshot(lotId, lot.getOccupiedSpaces(), lot.getCapacity(), LocalDateTime.now());
        log.info("Lot {} updated → occupied={}, status={}, available={}",
                lotId, lot.getOccupiedSpaces(), lot.getCurrentStatus(), lot.getAvailability());
        return true;
    }

    /** Record a usage snapshot for analytics. */
    public void recordSnapshot(int lotId, int occupied, int capacity, LocalDateTime at) {
        history.computeIfAbsent(lotId, k -> Collections.synchronizedList(new ArrayList<>()))
                .add(new UsageRecord(lotId, occupied, capacity, at));
    }

    /** Remove a lot and its history. */
    public boolean remove(int lotId) {
        ParkingLot removed = lots.remove(lotId);
        if (removed == null) {
            log.warn("Remove failed: lot {} not found", lotId);
            return false;
        }
        history.remove(lotId);
        log.info("Removed lot {} and its history", lotId);
        return true;
    }

    /** Generate a report of busiest lots and times. */
    public BusiestReport generateBusiestReport(int topN) {
        // Compute average utilization per lot
        List<BusiestReport.LotRank> ranks = history.entrySet().stream()
                .filter(e -> !e.getValue().isEmpty())
                .map(e -> {
                    int lotId = e.getKey();
                    List<UsageRecord> recs = e.getValue();
                    double avg = recs.stream()
                            .mapToDouble(UsageRecord::utilization)
                            .average().orElse(0.0);
                    ParkingLot lot = lots.get(lotId);
                    String name = (lot != null && lot.getLotName() != null)
                            ? lot.getLotName()
                            : ("Lot " + lotId);
                    return new BusiestReport.LotRank(lotId, name, avg);
                })
                .sorted(Comparator.comparingDouble(BusiestReport.LotRank::getAvgUtilization).reversed())
                .limit(Math.max(1, topN))
                .collect(Collectors.toList());

        // Compute busiest hour of day
        Map<Integer, List<Double>> byHour = new HashMap<>();
        history.values().forEach(list -> list.forEach(r -> {
            int hour = r.timestamp().getHour();
            byHour.computeIfAbsent(hour, k -> new ArrayList<>()).add(r.utilization());
        }));

        int busiestHour = 0;
        double busiestHourAvg = 0.0;
        for (Map.Entry<Integer, List<Double>> e : byHour.entrySet()) {
            double avg = e.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            if (avg > busiestHourAvg) {
                busiestHour = e.getKey();
                busiestHourAvg = avg;
            }
        }

        BusiestReport report = new BusiestReport(ranks, busiestHour, busiestHourAvg);
        log.info("Generated busiest report: topLots={}, busiestHour={}, avgUtil={}",
                ranks.size(), busiestHour, String.format("%.2f%%", busiestHourAvg * 100));
        return report;
    }

    // --- Simple data holders for demonstration only ---
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



