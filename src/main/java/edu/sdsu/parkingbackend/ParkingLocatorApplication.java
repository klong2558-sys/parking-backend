package edu.sdsu.parkingbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class ParkingLocatorApplication {
    private static final Logger log = LoggerFactory.getLogger(ParkingLocatorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ParkingLocatorApplication.class, args);
        log.info("Parking Backend started successfully.");
    }



}
