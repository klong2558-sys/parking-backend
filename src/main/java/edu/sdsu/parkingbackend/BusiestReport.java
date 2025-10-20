package edu.sdsu.parkingbackend;

import java.util.List;

public class BusiestReport {

    public final List<LotRank> topLots;

    public final int busiestHourOfDay;

    public final double busiestHourUtilization;

    public BusiestReport(List<LotRank> topLots, int busiestHourOfDay, double busiestHourUtilization) {
        this.topLots = topLots;
        this.busiestHourOfDay = busiestHourOfDay;
        this.busiestHourUtilization = busiestHourUtilization;
    }

    public static class LotRank {
        public final int lotId;
        public final String lotName;
        public final double avgUtilization; // 0.0–1.0

        public LotRank(int lotId, String lotName, double avgUtilization) {
            this.lotId = lotId;
            this.lotName = lotName;
            this.avgUtilization = avgUtilization;
        }
    }
}

