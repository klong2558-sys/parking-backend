package edu.sdsu.parking_backend;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Logic for genUsageStat method
@Service
public class AnalyticsService 
{
    private final AnalyticsRepo analyticsRepo;
    private final ReportRepo reportRepo;
    private final ParkingLotRepo parkingLotRepo;

    public AnalyticsService(AnalyticsRepo analyticsRepo, ReportRepo reportRepo, ParkingLotRepo parkingLotRepo)
    {
        this.analyticsRepo = analyticsRepo;
        this.reportRepo    = reportRepo;
        this.parkingLotRepo = parkingLotRepo;
    }

    /*
     * Gen usage stats based on a list of user reports +  parking lots
     */
   
    public Analytics genUsageStat()
    {
        // Gather the data 
        List<Report> allReports = reportRepo.findAll();
        List<ParkingLot> allLots = parkingLotRepo.findAll();

        int newReportID = (int) (analyticsRepo.count()+1);
        Analytics newReport = new Analytics(newReportID);
        
        // Find busiest lots
        // Busiest Lot : Counts how many times each parking lot ID  appears in the reports
        Map<Integer, Long> lotIDcounts = allReports.stream()
            .collect(Collectors.groupingBy(Report::getLotID, Collectors.counting()));
        long maxReports             = lotIDcounts.values().stream().max(Long::compareTo).orElse(0L); // Finds biggest #
        List<Integer> busiestLotIDs = lotIDcounts.entrySet().stream()                                      // (lot1, 10), (lot2, 5), etc.
                                      .filter(entry -> entry.getValue() == maxReports)                     // keep pairs where  value = our max value
                                      .map(Map.Entry::getKey)                                              // grab the key only (lotID)
                                      .collect(Collectors.toList());                                       // into the new list...Result: busiestLotIds = [1, 3]
        newReport.setBusiestLots(busiestLotIDs); 

        // Peak Times : countrs reports by hour they were submitted.
        Map<Integer, Long> reportsByHour = allReports.stream() // Result ex: { hour9 3 reports, hour 14: 12 reports}
                                           .collect(Collectors.groupingBy(report -> report.getTimeStamp().getHour(), Collectors.counting()));

        long maxReportsInHour            = reportsByHour.values().stream().max(Long::compareTo).orElse(0L); // Find highest # of reports in a single hr
        List<String> peakTimes           = reportsByHour.entrySet().stream()                                      // get list of all hrs that had max # of reports
                                          .filter(entry -> entry.getValue() == maxReportsInHour)                  // only keeps the hours with max hr value 
                                          .map(entry -> entry.getKey() + ":00 - " + (entry.getKey() + 1) + ":00") // transform hr (ex. 15) into a string ("15:00-16:00")
                                          .collect(Collectors.toList()); 
        newReport.setPeakTimes(peakTimes);
        return analyticsRepo.save(newReport);
    }

}
