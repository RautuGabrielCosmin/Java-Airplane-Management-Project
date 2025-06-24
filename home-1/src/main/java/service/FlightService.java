package service;

import domain.Flight;

import java.io.IOException;
import java.util.List;

public interface FlightService {
      Flight createFlight(int id, String locationDeparture, String locationArrival,
                          String dateDeparture, String dateArrival, int numberOfSeats, int flightCompanyId) throws IOException;
      Flight findById(int id) throws IOException;
      List<Flight> findAll() throws IOException;
      Flight updateFlight(int id, String newLocationDeparture, String newLocationArrival,
                          String newDateDeparture, String newDateArrival, int newNumberOfSeats,
                          int newFlightCompanyId) throws IOException;
      Flight deleteFlight(int id) throws IOException;
}

