
package edu.sdsu.parkingbackend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ParkingLotService {

    private static final Logger log = LoggerFactory.getLogger(ParkingLotService.class);

    private final Map<Integer, ParkingLot> lots = new ConcurrentHashMap<>();

    // usage history
    private final Map<Integer, List<UsageRecord>> history = new ConcurrentHashMap<>();

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

    // Record Usage
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
        // Record a snapshot
        recordSnapshot(lotId, lot.occupiedSpaces, lot.capacity, LocalDateTime.now());
        log.info("Lot {} updated → occupied={}, status={}, available={}",
                lotId, lot.occupiedSpaces, lot.currentStatus, lot.getAvailability());
        return true;
    }

    public void recordSnapshot(int lotId, int occupied, int capacity, LocalDateTime at) {
        history.computeIfAbsent(lotId, k -> Collections.synchronizedList(new ArrayList<>()))
                .add(new UsageRecord(lotId, occupied, capacity, at));
    }

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

    //Report generation
    public BusiestReport generateBusiestReport(int topN) {
        // 1) Compute avg utilization per lot
        List<BusiestReport.LotRank> ranks = history.entrySet().stream()
                .filter(e -> !e.getValue().isEmpty())
                .map(e -> {
                    int lotId = e.getKey();
                    List<UsageRecord> recs = e.getValue();
                    double avg = recs.stream().mapToDouble(UsageRecord::utilization).average().orElse(0.0);
                    ParkingLot lot = lots.get(lotId);
                    String name = (lot != null && lot.lotName != null) ? lot.lotName : ("Lot " + lotId);
                    return new BusiestReport.LotRank(lotId, name, avg);
                })
                .sorted(Comparator.comparingDouble((BusiestReport.LotRank r) -> r.avgUtilization).reversed())
                .limit(Math.max(1, topN))
                .collect(Collectors.toList());

        // 2) Compute busiest hour of day (0-23) across ALL records
        Map<Integer, List<Double>> byHour = new HashMap<>();
        history.values().forEach(list -> {
            for (UsageRecord r : list) {
                int hour = r.timestamp.getHour();
                byHour.computeIfAbsent(hour, k -> new ArrayList<>()).add(r.utilization());
            }
        });

        int busiestHour = -1;
        double busiestHourAvg = 0.0;
        for (Map.Entry<Integer, List<Double>> e : byHour.entrySet()) {
            double avg = e.getValue().stream().mapToDouble(d -> d).average().orElse(0.0);
            if (busiestHour == -1 || avg > busiestHourAvg) {
                busiestHour = e.getKey();
                busiestHourAvg = avg;
            }
        }

        // if no data, default hour = 0
        if (busiestHour == -1) {
            busiestHour = 0;
            busiestHourAvg = 0.0;
        }

        BusiestReport report = new BusiestReport(ranks, busiestHour, busiestHourAvg);
        log.info("Generated busiest report: topLots={}, busiestHour={}, avgUtil={}",
                ranks.size(), busiestHour, String.format("%.2f%%", busiestHourAvg * 100));
        return report;
    }
}



