package service;

import domain.TravelAgency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.TravelAgencyRepository;

import java.util.List;

public class MemoryTravelAgencyService implements TravelAgencyService {
    private static final Logger logger = LoggerFactory.getLogger(MemoryTravelAgencyService.class);

    private final TravelAgencyRepository travelAgencyRepository;
    public MemoryTravelAgencyService(TravelAgencyRepository travelAgencyRepository) {
        this.travelAgencyRepository = travelAgencyRepository;
    }

    public TravelAgency createTravelAgency(int id, String name, int numberOfEmployees, String flightsBooked){
        logger.info("Create TravelAgency");
        if(name == null || name.isEmpty()){
            logger.error("Name is null or empty");
            throw new IllegalArgumentException("Name is null or empty");
        }
        TravelAgency existing = travelAgencyRepository.findById(id);
        if(existing != null){
            logger.warn("TravelAgency already exists");
            throw new IllegalArgumentException("TravelAgency already exists");
        }
        TravelAgency newTravelAgency = new TravelAgency(id, name, numberOfEmployees, flightsBooked);
        TravelAgency savedTravelAgency = travelAgencyRepository.save(newTravelAgency);
        logger.info("TravelAgency created");
        return savedTravelAgency;
    }

    public TravelAgency findById(int id){
        logger.info("Find TravelAgency by id");
        TravelAgency travelAgency = travelAgencyRepository.findById(id);
        if(travelAgency == null){
            logger.error("TravelAgency not found");
        }else{
            logger.info("TravelAgency found");
        }
        return travelAgency;
    }

    public List<TravelAgency> findAll(){
        logger.info("Find all TravelAgencies");
        return travelAgencyRepository.findAll();
    }

    public TravelAgency updateTravelAgency(int id, String newName, int newNumberOfEmployees, String newFlightsBooked){
        logger.info("Update TravelAgency");
        TravelAgency travelAgency = travelAgencyRepository.findById(id);
        if(travelAgency == null){
            logger.error("TravelAgency not found");
            return null;
        }
        travelAgency.setName(newName);
        travelAgency.setNumberOfEmployees(newNumberOfEmployees);
        travelAgency.setFlightsBooked(newFlightsBooked);
        TravelAgency savedTravelAgency = travelAgencyRepository.save(travelAgency);
        logger.info("TravelAgency updated");
        return savedTravelAgency;
    }

    public TravelAgency deleteTravelAgency(int id){
        logger.info("Delete TravelAgency by id");
        TravelAgency travelAgency = travelAgencyRepository.findById(id);
        if(travelAgency == null){
            logger.error("TravelAgency not found");
            return null;
        }else{
            logger.info("TravelAgency deleted");
            travelAgencyRepository.delete(id);
        }
        return travelAgency;
    }

}