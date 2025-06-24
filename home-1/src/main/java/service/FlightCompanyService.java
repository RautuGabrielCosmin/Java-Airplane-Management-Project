package service;

import domain.FlightCompany;

import java.io.IOException;
import java.util.List;

public interface FlightCompanyService {
    FlightCompany createFlightCompany(int id, String name, String location) throws IOException;
    FlightCompany updateFlightCompany(int id, String newName, String newLocation) throws IOException;
    FlightCompany deleteFlightCompany(int id) throws IOException;
    List<FlightCompany> findAll() throws IOException;
    FlightCompany findById(int id) throws IOException;
}