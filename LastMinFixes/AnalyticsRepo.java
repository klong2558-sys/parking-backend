package edu.sdsu.parking_backend;

import org.springframework.data.jpa.repository.JpaRepository;

// Handles + saves retrieving Analytics reports from database
public interface AnalyticsRepo extends JpaRepository<Analytics, Integer> {}
