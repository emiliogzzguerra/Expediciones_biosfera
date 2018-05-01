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
    private String customerReference;
    private String tripReference;

    public Reservation(int quantity, double price, boolean isConfirmed, Bitmap ticket, String customerReference, String tripReference) {
        this.quantity = quantity;
        this.price = price;
        this.isConfirmed = isConfirmed;
        this.ticket = ticket;
        this.customerReference = customerReference;
        this.tripReference = tripReference ;
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

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    public String getTripReference() {
        return tripReference;
    }

    public void setTripReference(String tripReference) {
        this.tripReference = tripReference;
    }
}
