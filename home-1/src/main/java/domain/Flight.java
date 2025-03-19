package domain;

import java.io.Serial;
import java.io.Serializable;

public class Flight implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String locationDeparture;
    private String locationArrival;
    private String dateDeparture;
    private String dateArrival;
    private int numberOfSeats;   // store seat count as int
    private int flightCompanyId; // foreign key in DB

    public Flight(int id,
                  String locationDeparture,
                  String locationArrival,
                  String dateDeparture,
                  String dateArrival,
                  int numberOfSeats,
                  int flightCompanyId) {
        this.id = id;
        this.locationDeparture = locationDeparture;
        this.locationArrival = locationArrival;
        this.dateDeparture = dateDeparture;
        this.dateArrival = dateArrival;
        this.numberOfSeats = numberOfSeats;
        this.flightCompanyId = flightCompanyId;
    }

    public Flight() {
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getLocationDeparture() {
        return locationDeparture;
    }
    public void setLocationDeparture(String locationDeparture) {
        this.locationDeparture = locationDeparture;
    }

    public String getLocationArrival() {
        return locationArrival;
    }
    public void setLocationArrival(String locationArrival) {
        this.locationArrival = locationArrival;
    }

    public String getDateDeparture() {
        return dateDeparture;
    }
    public void setDateDeparture(String dateDeparture) {
        this.dateDeparture = dateDeparture;
    }

    public String getDateArrival() {
        return dateArrival;
    }
    public void setDateArrival(String dateArrival) {
        this.dateArrival = dateArrival;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }
    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public int getFlightCompanyId() {
        return flightCompanyId;
    }
    public void setFlightCompanyId(int flightCompanyId) {
        this.flightCompanyId = flightCompanyId;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", locationDeparture='" + locationDeparture + '\'' +
                ", locationArrival='" + locationArrival + '\'' +
                ", dateDeparture='" + dateDeparture + '\'' +
                ", dateArrival='" + dateArrival + '\'' +
                ", numberOfSeats=" + numberOfSeats +
                ", flightCompanyId=" + flightCompanyId +
                '}';
    }
}
