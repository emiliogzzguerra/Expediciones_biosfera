package itesm.mx.expediciones_biosfera.entities.models;

import java.io.Serializable;
import java.util.Date;

public class Reservation implements Serializable{
    private int quantity;
    private double price;
    private String isConfirmed;
    private String isPaid;
    private String ticketUrl;
    private String customerReference;
    private String tripReference;
    private Date initialDate;
    private String reference;


    public Reservation(){}
    // Constructor with a reference
    public Reservation(int quantity, double price, String isConfirmed, String isPaid,
                       String ticketUrl, String customerReference, String tripReference,
                       Date initialDate, String reference) {
        this.quantity = quantity;
        this.price = price;
        this.isConfirmed = isConfirmed;
        this.isPaid = isPaid;
        this.ticketUrl = ticketUrl;
        this.customerReference = customerReference;
        this.tripReference = tripReference;
        this.initialDate = initialDate;
        this.reference = reference;
    }

    // Constructor without a reference
    public Reservation(int quantity, double price, String isConfirmed, String isPaid,
                       String ticketUrl, String customerReference, String tripReference,
                       Date initialDate) {
        this.quantity = quantity;
        this.price = price;
        this.isConfirmed = isConfirmed;
        this.isPaid = isPaid;
        this.ticketUrl = ticketUrl;
        this.customerReference = customerReference;
        this.tripReference = tripReference;
        this.initialDate = initialDate;
        this.reference = null;
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

    public String getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(String isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }

    public String getTicketUrl() {
        return ticketUrl;
    }

    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
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

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
