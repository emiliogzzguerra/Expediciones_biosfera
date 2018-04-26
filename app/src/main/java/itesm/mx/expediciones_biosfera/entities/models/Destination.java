package itesm.mx.expediciones_biosfera.entities.models;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by avillarreal on 4/12/18.
 */

public class Destination {
    private String name;
    private String state;
    private String city;
    private double lat;
    private double lon;
    private String description;
    private List<Bitmap> images;

    public Destination(String name, String state, String city, double lat, double lon, String description, List<Bitmap> images) {
        this.name = name;
        this.state = state;
        this.city = city;
        this.lat = lat;
        this.lon = lon;
        this.description = description;
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Bitmap> getImages() {
        return images;
    }

    public void setImages(List<Bitmap> images) {
        this.images = images;
    }
}
