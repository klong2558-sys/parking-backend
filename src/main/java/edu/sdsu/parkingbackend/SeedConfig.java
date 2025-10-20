package edu.sdsu.parkingbackend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeedConfig {

    private static final Logger log = LoggerFactory.getLogger(SeedConfig.class);

    @Bean
    CommandLineRunner seedLots(ParkingLotService service) {
        return args -> {
            ParkingLot lot1 = new ParkingLot();
            lot1.lotID = 1;
            lot1.lotName = "Lot 1";
            lot1.capacity = 120;
            lot1.updateStatus(90);

            ParkingLot lot2 = new ParkingLot();
            lot2.lotID = 2;
            lot2.lotName = "Lot 2";
            lot2.capacity = 80;
            lot2.updateStatus(80);

            ParkingLot lot3 = new ParkingLot();
            lot3.lotID = 3;
            lot3.lotName = "Lot 3";
            lot3.capacity = 150;
            lot3.updateStatus(120);

            service.save(lot1);
            service.save(lot2);
            service.save(lot3);

            log.info("Seeded {} parking lots successfully.", service.findAll().size());
        };
    }
}