package itesm.mx.expediciones_biosfera.entities.models;

import java.util.List;

/**
 * Created by avillarreal on 4/12/18.
 */

public class Customer {
    private String name;
    private String email;
    private String description;
    private List<Reservation> reservations;
    private List<Trip> trips;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public Customer(){}
    public Customer(String name, String email){
        this.name = name;
        this.email = email;
    }
    public Customer(String name, String email, String description, List<Reservation> reservations, List<Trip> trips) {

        this.name = name;
        this.email = email;
        this.description = description;
        this.reservations = reservations;
        this.trips = trips;
    }
}
