package service;

import domain.TravelAgency;

import java.io.IOException;
import java.util.List;

public interface TravelAgencyService {
    TravelAgency createTravelAgency(int id, String name, int numberOfEmployees, String flightsBooked) throws IOException;
    TravelAgency findById(int id) throws IOException;
    List<TravelAgency> findAll() throws IOException;
    TravelAgency updateTravelAgency(int id, String newName, int newNumberOfEmployees, String newFlightsBooked) throws IOException;
    TravelAgency deleteTravelAgency(int id) throws IOException;
}
