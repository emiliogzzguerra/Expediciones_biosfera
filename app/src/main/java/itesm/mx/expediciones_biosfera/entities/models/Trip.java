package itesm.mx.expediciones_biosfera.entities.models;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.w3c.dom.Document;

import java.util.Date;
import java.util.List;

/**
 * Created by avillarreal on 4/12/18.
 */

public class Trip {
    private String title;
    private Date date;
    private int capacity;
    private double price;
    private int duration;
    private Destination destination;
    private List<Customer> customers;

    public Trip(){ };
    public Trip(String title, Date date, int capacity, double price, int duration, Destination destination, List<Customer> customers) {
        this.title = title;
        this.date = date;
        this.capacity = capacity;
        this.price = price;
        this.duration = duration;
        this.destination = destination;
        this.customers = customers;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Destination getDestination() {
        return destination;
    }


    public  void setDestination(Destination destination) {
        this.destination = destination;
    }
    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
}
