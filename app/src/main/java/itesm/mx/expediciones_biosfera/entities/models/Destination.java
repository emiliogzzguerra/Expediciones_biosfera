package itesm.mx.expediciones_biosfera.entities.models;

import java.io.Serializable;
import java.util.List;

public class Destination implements Serializable{
    private String reference;
    private String state;
    private String city;
    private String name;
    private int duration;
    private int price;
    private double lat;
    private double lon;
    private String description;
    private List<String> imageUrls;

    public Destination(){ }

    public Destination(String reference, String state, String city, int duration, int price, double lat, double lon, String description, List<String> imageUrls, String name) {
        this.reference = reference;
        this.state = state;
        this.city = city;
        this.duration = duration;
        this.price = price;
        this.lat = lat;
        this.lon = lon;
        this.description = description;
        this.imageUrls = imageUrls;
        this.name = name;

    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
