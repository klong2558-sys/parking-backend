package edu.sdsu.parking_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class ParkingBackendApplication 
{
	private static final Logger log = LoggerFactory.getLogger(ParkingBackendApplication.class);

    public static void main(String[] args) 
	{
        SpringApplication.run(ParkingBackendApplication.class, args);
        log.info("Parking Backend started successfully.");
    }

}
