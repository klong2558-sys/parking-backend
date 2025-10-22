package edu.sdsu.parking_backend;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

// Creates the public API endpoints that admin can use
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController
{
    // Attributes
    private final AnalyticsService analyticsService;
    private final AnalyticsRepo    analyticsRepo;
    
    public AnalyticsController(AnalyticsService analyticsService, AnalyticsRepo analyticsRepo)
    {
        this.analyticsRepo = analyticsRepo;
        this.analyticsService = analyticsService;
    }

    /*
     * Endpoint to gen a new report
     * Post req to /api/analytics/generate will run the analysis
     */
    @PostMapping("/generate")
    public Analytics genNewReport()
    {return analyticsService.genUsageStat();}

    /*
     * Endpoint to export report details to a CSV file named "analyticsReport_{ID}.csv"
     * Get req to /api/analytics/1/export will donwload the file 
     * IF there are no statistics that have been gen, it will create a file with only the header row
     */
    @GetMapping("/{id}/export")
    public void exportReport(@PathVariable int id, HttpServletResponse response) throws IOException
    {
        Optional<Analytics> reportOpt = analyticsRepo.findById(id);
        if (reportOpt.isEmpty())
        {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Analytics report = reportOpt.get();
        String fileN = "analyticsReport_" + id + ".csv";

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileN + "\"");

        PrintWriter writer = response.getWriter();

        writer.println("Category,Value");
        writer.println("Report ID, "      + report.getReportID());
        writer.println("Date Generated, " + report.getDateGen());

        // Loop through + Print each peak time
        for (String time : report.getPeakTimes())
        {writer.println("Peak Time: " + time  );}

        for (Integer lotID : report.getBusiestLots())
        {writer.println("Busiest Lot ID:" + lotID);}
    }
}