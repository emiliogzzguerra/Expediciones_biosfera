package itesm.mx.expediciones_biosfera.entities.models;

import android.graphics.Bitmap;
import android.media.Image;

/**
 * Created by avillarreal on 4/12/18.
 */

public class Reservation {
    private int quantity;
    private double price;
    private boolean isConfirmed;
    private Bitmap ticket;
    private Customer customer;
    private Trip trip;

    public Reservation(int quantity, double price, boolean isConfirmed, Bitmap ticket, Customer customer, Trip trip) {

        this.quantity = quantity;
        this.price = price;
        this.isConfirmed = isConfirmed;
        this.ticket = ticket;
        this.customer = customer;
        this.trip = trip;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public Bitmap getTicket() {
        return ticket;
    }

    public void setTicket(Bitmap ticket) {
        this.ticket = ticket;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }
}
