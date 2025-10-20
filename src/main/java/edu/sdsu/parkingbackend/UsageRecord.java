package edu.sdsu.parkingbackend;

import java.time.LocalDateTime;

public class UsageRecord {

    public final int lotId;
    public final int occupied;
    public final int capacityAtSample; // capture capacity at time of record
    public final LocalDateTime timestamp;

    public UsageRecord(int lotId, int occupied, int capacityAtSample, LocalDateTime timestamp) {
        this.lotId = lotId;
        this.occupied = occupied;
        this.capacityAtSample = capacityAtSample;
        this.timestamp = timestamp;
    }

    public double utilization() {
        return capacityAtSample <= 0 ? 0.0 : (occupied * 1.0) / capacityAtSample;
    }
}