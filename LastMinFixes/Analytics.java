package edu.sdsu.parking_backend;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity                   // Tells database this is a table
@Data
@NoArgsConstructor
public class Analytics
{
    // Attribures
    @Id 
    private int reportID; // marks this as primary key

    @ElementCollection
    private List<String> peakTimes;
    @ElementCollection
    private List<Integer> busiestLots;
    private String dateGen;

    /* Constructor
        Creates a new Analytics report
        @param reportID : The unique ID for this report 
    */
    public Analytics(int reportID)
    {
        this.reportID    = reportID; 
        this.peakTimes   = new ArrayList<>();          // Filled via genUsageStat()
        this.busiestLots = new ArrayList<>();          // Filled via genUsageStat()
        this.dateGen     = LocalDate.now().toString(); // set gen date to curr date
    }

    /*
     * Display summary of generated report to the console
     */
    public void displaySummary()
    {
        System.out.println(
                            "\n-- Summary Report ID: " + reportID + " --" +
                            "\n Report Generated on " + this.dateGen + 
                            "\n Identified " + this.peakTimes.size() + " peak reporting hour(s)." +
                            "\n Identified " + this.busiestLots.size() + " busy lot(s)."
                          );
        
    }
}