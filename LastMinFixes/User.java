package edu.sdsu.parking_backend;

import java.util.*;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public abstract class User
{

    @Id
    private int userID;

    private String username;
    private String email;
    private String password;
    private String role;
    private boolean isLoggedIn = false;
    public boolean isLoggedIn() { return isLoggedIn; }
    public void login(String email, String password) {

        if (Objects.equals(this.email, email) && Objects.equals(this.password, password)) {
            isLoggedIn = true;
            System.out.println("Login successful! Welcome, " + email + ".");
        } else {
            System.out.println("Invalid email or password.");
        }
    }

    float getUserID(float userID) {
        return this.userID;
    }


    /**
     * Create a parking status report for this user and persist it via the provided repository.
     * @param repo    the ReportRepo JPA repository
     * @param lotId   the parking lot ID being reported on
     * @param status  the status string (e.g., "FULL", "ALMOST_FULL", "EMPTY")
     * @return the saved Report entity (with generated ID)
     */
    public Report generateReport(ReportRepo repo, int lotId, String status) {
        Report report = new Report(this.userID, lotId, status);
        return repo.save(report);
    }


    void logout() {

        if (isLoggedIn) {
            isLoggedIn = false;
            System.out.println("Logging out");
        } else {
            System.out.println("Not logged in");
        }
    }
}