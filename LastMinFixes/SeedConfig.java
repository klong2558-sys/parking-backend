package edu.sdsu.parking_backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeedConfig
{

    private static final Logger log = LoggerFactory.getLogger(SeedConfig.class);
    private final UserRepo userRepo;
    private final ParkingLotRepo parkingLotRepo;

    public SeedConfig(UserRepo userRep, ParkingLotRepo parkingLotRepo)
    {
        this.userRepo       = userRep;
        this.parkingLotRepo = parkingLotRepo;
    }

    @Bean
    CommandLineRunner seedLots()
    {
        return args -> {

            if (parkingLotRepo.count() == 0)
            {
                ParkingLot lot1 = new ParkingLot(1, "Parking Lot 1", 120);
                lot1.updateStatus(90);
                ParkingLot lot2 = new ParkingLot(1, "Parking Lot 2", 80);
                lot1.updateStatus(80); // Lot 2 at full capacity
                ParkingLot lot3 = new ParkingLot(1, "Parking Lot 3", 150);
                lot1.updateStatus(120);

                parkingLotRepo.save(lot1);
                parkingLotRepo.save(lot2);
                parkingLotRepo.save(lot3);

                log.info("Seeded {} parking lots successfully.", parkingLotRepo.count());
            }
            else
                log.info("Parking lots already seeded.");
        };
    }
    @Bean
    CommandLineRunner seedUsers() 
    {
        return args -> {
            if (userRepo.count() == 0)
            {
                log.info("Database is empty. Seeding users...");

                // Student 1 aka winston Churchill
                Student student1 = new Student();
                student1.setUserID(101);
                student1.setUsername("winstonChurchill");
                student1.setEmail("winstonChurchill@sdsu.edu");
                student1.setPassword("123"); // Later: hash it
                student1.setRole("STUDENT");
                student1.setStudentID("09011939"); 
                userRepo.save(student1);

                // Student 2 aka Jason Toddd
                Student student2 = new Student();
                student2.setUserID(102);
                student2.setUsername("jasonTodd");
                student2.setEmail("jasonTodd@sdsu.edu");
                student2.setPassword("BatmanSucks");
                student2.setRole("STUDENT");
                student2.setStudentID("04272005");
                userRepo.save(student2);
                
            }
        };
    }
}