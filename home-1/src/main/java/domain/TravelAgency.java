package domain;

import java.io.Serial;
import java.io.Serializable;

public class TravelAgency implements IdTravelAgency<Integer>, Serializable {
    @Serial
    private static final long serialVersionUID = 2L;

    private int id;
    private String name;
    private int numberOfEmployees;
    private String flightsBooked;

    public TravelAgency(int id, String name, int numberOfEmployees, String flightsBooked) {
        this.id = id;
        this.name = name;
        this.numberOfEmployees = numberOfEmployees;
        this.flightsBooked = flightsBooked;
    }

    public TravelAgency() {
        this.id = 0;
        this.name = "";
        this.numberOfEmployees = 0;
        this.flightsBooked = "";
    }

    @Override
    public Integer getIdTravelAgency() {
        return this.id;
    }

    @Override
    public void setIdTravelAgency(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfEmployees() {
        return this.numberOfEmployees;
    }
    public void setNumberOfEmployees(int numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public String getFlightsBooked() {
        return this.flightsBooked;
    }
    public void setFlightsBooked(String flightsBooked) {
        this.flightsBooked = flightsBooked;
    }

    @Override
    public String toString() {
        return "TravelAgencie{" +
                "ID=" + id +
                ", name='" + name + '\'' +
                ", numberOfEmployees=" + numberOfEmployees +
                ", flightsBooked='" + flightsBooked + '\'' +
                '}';
    }
}
