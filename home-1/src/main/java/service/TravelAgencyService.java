package service;

import domain.TravelAgency;

import java.util.List;

public interface TravelAgencyService {
    TravelAgency createTravelAgency(int id, String name, int numberOfEmployees, String flightsBooked);
    TravelAgency findById(int id);
    List<TravelAgency> findAll();
    TravelAgency updateTravelAgency(int id, String newName, int newNumberOfEmployees, String newFlightsBooked);
    TravelAgency deleteTravelAgency(int id);
}
