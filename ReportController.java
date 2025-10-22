package edu.sdsu.parking_backend;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/reports")

public class ReportController {

    private final ReportRepo reportRepo;

    @Autowired
    public ReportController(ReportRepo reportRepo) {
        this.reportRepo = reportRepo;
    }

    public static record CreateReportRequest(int userId, int lotId, String status) {}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Report create(@RequestBody CreateReportRequest body) {
        if (body == null || body.status() == null || body.status().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "status is required");
        }
        Report report = new Report(body.userId(), body.lotId(), body.status().trim());
        return reportRepo.save(report);
    }

    /** Get a report by id. */
    @GetMapping("/{id}")
    public Report getOne(@PathVariable Integer id) {
        return reportRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found"));
    }

    /** Get all reports. */
    @GetMapping
    public List<Report> getAll() {
        return reportRepo.findAll();
    }

    /** Mark a report as verified. */
    @PatchMapping("/{id}/verify")
    public Report verify(@PathVariable Integer id) {
        Report report = reportRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found"));
        report.verifyReport();
        return reportRepo.save(report);
    }
}

