package edu.sdsu.parking_backend;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity // This class rep a table in the database
@Data 
@AllArgsConstructor

public class Report
{
    // Attributes / table cols
    @Id                               // marks this as primary key for database table
    private int reportID;             // must be unique for ever row + never be empty (null)

    private int lotID;                // ID of parking lot this report is about
    private int userID;               // ID of the user who submitted the report
    private String statusReported;    // Status of user who reported (i.e. Full, Almost full, empty)
    private LocalDateTime timeStamp;  // date and time of report
    private boolean isVerified;       // flag to track if an admin verfied the report 
    private int ReportedOccupanacy;

    public Report (int reportID, int userID, String statusReported)
    { // gen a new report
        this.reportID       = reportID;
        this.userID         = userID;
        this.statusReported = statusReported;
        this.timeStamp      = LocalDateTime.now(); // uses the current time stamp 
        this.isVerified     = false; 
    }

    public Report() {
        // Required by JPA
    }

    /*
    *   Marks the report as verified
    *   LATER : In a real app this will have more logic, but for an MVP just changing the flag for now
    */
    public void verifyReport()
    {
        this.isVerified = true;
        System.out.println("The report" + this.reportID + " has been verified!");
    }

    /*
     * Returns a formatted string with details of report
     */
    public String getReportDetails()
    {
        return "Report ID: "           + this.reportID       +
                "\nUser "              + this.userID         +
                " reported status is " + this.statusReported +
                " at "                 + this.timeStamp      +
                "\nLot ID: "           + this.lotID          +
                "\nVerified: "         + this.isVerified;
    }
}