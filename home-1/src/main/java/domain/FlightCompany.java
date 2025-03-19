package domain;

import java.io.Serializable;

public class FlightCompany implements IdFlight<Integer>, Serializable {
    private int id;
    private String name;
    private String location;

    public FlightCompany(int id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public FlightCompany() {
    }

    @Override
    public Integer getIdFlight() {
        return id;
    }

    @Override
    public void setIdFlight(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "FlightCompany{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
