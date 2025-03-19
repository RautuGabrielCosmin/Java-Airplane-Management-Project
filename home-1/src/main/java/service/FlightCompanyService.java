package service;

import domain.FlightCompany;

import java.util.List;

public interface FlightCompanyService {
    FlightCompany createFlightCompany(int id, String name, String location);
    FlightCompany updateFlightCompany(int id, String newName, String newLocation);
    FlightCompany deleteFlightCompany(int id);
    List<FlightCompany> findAll();
    FlightCompany findById(int id);
}
