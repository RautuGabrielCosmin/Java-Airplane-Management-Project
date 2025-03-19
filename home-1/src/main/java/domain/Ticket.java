package domain;

import java.io.Serial;
import java.io.Serializable;

public class Ticket implements IdTicket, Serializable {
    @Serial
    private static final long serialVersionUID = 5L;

    private int id;
    private int flightId;
    private int touristId;
    private int employeeId;
    private String seatType;
    private String seatNumber;
    private String customerName;

    public Ticket() {
    }

    public Ticket(int id,
                  int flightId,
                  int touristId,
                  int employeeId,
                  String seatType,
                  String seatNumber,
                  String customerName) {
        this.id = id;
        this.flightId = flightId;
        this.touristId = touristId;
        this.employeeId = employeeId;
        this.seatType = seatType;
        this.seatNumber = seatNumber;
        this.customerName = customerName;
    }

    @Override
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getFlightId() {
        return flightId;
    }
    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public int getTouristId() {
        return touristId;
    }
    public void setTouristId(int touristId) {
        this.touristId = touristId;
    }

    public int getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getSeatType() {
        return seatType;
    }
    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public String getSeatNumber() {
        return seatNumber;
    }
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", flightId=" + flightId +
                ", touristId=" + touristId +
                ", employeeId=" + employeeId +
                ", seatType='" + seatType + '\'' +
                ", seatNumber='" + seatNumber + '\'' +
                ", customerName='" + customerName + '\'' +
                '}';
    }
}
