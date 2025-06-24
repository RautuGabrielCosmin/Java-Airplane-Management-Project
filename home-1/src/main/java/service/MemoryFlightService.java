package service;
import java.util.List;
import domain.Flight;
import repository.FlightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MemoryFlightService implements FlightService {
    private static Logger logger = LoggerFactory.getLogger(MemoryFlightService.class);

    private final FlightRepository flightRepository;
    public MemoryFlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Flight createFlight(int id,
                               String locationDeparture,
                               String locationArrival,
                               String dateDeparture,
                               String dateArrival,
                               int numberOfSeats,
                               int flightCompanyId) {
        logger.info("Creating flight with id={}, departure={}, arrival={}, " +
                        "dateDeparture={}, dateArrival={}, seats={}, companyId={}",
                id, locationDeparture, locationArrival, dateDeparture, dateArrival,
                numberOfSeats, flightCompanyId);

        if (locationDeparture == null || locationDeparture.trim().isEmpty()) {
            logger.warn("Cannot create flight - locationDeparture is blank");
            throw new IllegalArgumentException("locationDeparture must not be blank");
        }
        if (locationArrival == null || locationArrival.trim().isEmpty()) {
            logger.warn("Cannot create flight - locationArrival is blank");
            throw new IllegalArgumentException("locationArrival must not be blank");
        }
        if (dateDeparture == null || dateDeparture.trim().isEmpty()) {
            logger.warn("Cannot create flight - dateDeparture is blank");
            throw new IllegalArgumentException("dateDeparture must not be blank");
        }
        if (dateArrival == null || dateArrival.trim().isEmpty()) {
            logger.warn("Cannot create flight - dateArrival is blank");
            throw new IllegalArgumentException("dateArrival must not be blank");
        }
        if (numberOfSeats < 0) {
            logger.warn("Cannot create flight - numberOfSeats is negative: {}", numberOfSeats);
            throw new IllegalArgumentException("numberOfSeats must be non-negative");
        }
        if (flightCompanyId < 0) {
            logger.warn("Cannot create flight - flightCompanyId is negative: {}", flightCompanyId);
            throw new IllegalArgumentException("flightCompanyId must be non-negative");
        }
        //verific daca exista id-ul zborului deja
        Flight existing = flightRepository.findById(id);
        if (existing != null && existing.getId() != 0) {
            logger.warn("Cannot create flight - flight with id={} already exists: {}", id, existing);
            throw new IllegalStateException("Flight with this ID already exists!");
        }

        Flight newFlight = new Flight(
                id,
                locationDeparture,
                locationArrival,
                dateDeparture,
                dateArrival,
                numberOfSeats,
                flightCompanyId
        );

        Flight saved = flightRepository.save(newFlight);
        logger.info("Flight created successfully: {}", saved);

        return saved;
    }

    public Flight findById(int id) {
        logger.info("Finding flight with id={}", id);
        Flight flight = flightRepository.findById(id);
        if(flight != null) {
            logger.info("Found flight with id={}", id);
        }else{
            logger.warn("Flight with id={} not found", id);
        }
        return flight;
    }

    public List<Flight> findAll() {
        logger.info("Finding all flights");
        return flightRepository.findAll();
    }

    @Override
    public domain.Flight updateFlight(int id, String newLocationDeparture, String newLocationArrival,
                                      String newDateDeparture, String newDateArrival, int newNumberOfSeats,
                                      int newFlightCompanyId) {
        logger.info("Updating flight with id {}, new location departure {}, new location arrival {}, " +
                        "new date departure {}, new date arrival {}, new number of seats {}, new flight company id {}"
                , id, newLocationDeparture, newLocationArrival, newDateDeparture, newDateArrival, newNumberOfSeats,
                newFlightCompanyId);
        Flight flight = flightRepository.findById(id);
        if (flight == null) {
            logger.error("Flight with id={} not found", id);
            throw new IllegalArgumentException("Flight with id " + id + " not found");
        }
        flight.setLocationDeparture(newLocationDeparture);
        flight.setLocationArrival(newLocationArrival);
        flight.setDateDeparture(newDateDeparture);
        flight.setDateArrival(newDateArrival);
        flight.setNumberOfSeats(newNumberOfSeats);
        flight.setFlightCompanyId(newFlightCompanyId);
        Flight updated = flightRepository.save(flight);
        logger.info("Flight updated successfully: {}", updated);
        return updated;
    }

    public Flight deleteFlight(int id) {
        logger.info("Deleting flight with id={}", id);
        Flight flight = flightRepository.findById(id);
        if (flight == null) {
            logger.error("Flight with id={} not found", id);
        }else{
            logger.info("Deleting flight with id={}", id);
            flightRepository.delete(id);
        }
        return flight;
    }
}
