package service;

import domain.Flight;

import java.util.List;

public interface FlightService {
      Flight createFlight(int id, String locationDeparture, String locationArrival,
                          String dateDeparture, String dateArrival, int numberOfSeats, int flightCompanyId);
      Flight findById(int id);
      List<Flight> findAll();
      Flight updateFlight(int id, String newLocationDeparture, String newLocationArrival,
                          String newDateDeparture, String newDateArrival, int newNumberOfSeats,
                          int newFlightCompanyId);
      Flight deleteFlight(int id);
}

