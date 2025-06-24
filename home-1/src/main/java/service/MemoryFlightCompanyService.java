package service;

import domain.FlightCompany;
import repository.FlightCompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.List;

public class MemoryFlightCompanyService implements FlightCompanyService {
    private static Logger logger = LoggerFactory.getLogger(MemoryFlightCompanyService.class);

    private final FlightCompanyRepository flightCompanyRepository;
    public MemoryFlightCompanyService(FlightCompanyRepository flightCompanyRepository) {
        this.flightCompanyRepository = flightCompanyRepository;
    }

    @Override
    public FlightCompany createFlightCompany(int id, String name, String location){
        logger.info("Creating flight company with id {} and name {} and location {}", id, name, location);
        if(name == null || name.isEmpty() || location == null || location.isEmpty()){
            logger.error("Name and location are empty");
            throw new IllegalArgumentException("Name and location are empty");
        }
        FlightCompany existing = flightCompanyRepository.findById(id);
        if(existing != null){
            logger.error("Flight company with id {} already exists", id);
            throw new IllegalArgumentException("Flight company with id " + id + " already exists");
        }
        FlightCompany flightCompany = new FlightCompany(id, name, location);
        logger.info("Saving flight company {}", flightCompany);
        return flightCompanyRepository.save(flightCompany);
    }

    @Override
    public FlightCompany findById(int id) {
        logger.info("Finding flight company with id {}", id);
        FlightCompany flightCompany = flightCompanyRepository.findById(id);
        if(flightCompany != null){
            logger.info("Found flight company {}", flightCompany);
        }else {
            logger.error("Flight company with id {} not found", id);
        }
        return flightCompany;
    }

    @Override
    public List<FlightCompany> findAll(){
        logger.info("Finding all flight companies");
        return flightCompanyRepository.findAll();
    }

    @Override
    public FlightCompany updateFlightCompany(int id, String newName, String newLocation){
        logger.info("Updating flight company with id {} and name {} and location {}", id, newName, newLocation);
        FlightCompany existing = flightCompanyRepository.findById(id);
        if(existing == null){
            logger.error("Flight company with id {} not found", id);
        }
        existing.setName(newName);
        existing.setLocation(newLocation);
        FlightCompany updated = flightCompanyRepository.save(existing);
        logger.info("Updated flight company with name {} and location {}", newName, newLocation);
        return updated;
    }

    @Override
    public FlightCompany deleteFlightCompany(int id){
        logger.info("Deleting flight company with id {}", id);
        FlightCompany existing = flightCompanyRepository.findById(id);
        if(existing == null){
            logger.error("Flight company with id {} not found", id);
        }else{
            logger.info("Deleted flight company with id {}", id);
            flightCompanyRepository.delete(id);
        }
        return existing;
    }
}
