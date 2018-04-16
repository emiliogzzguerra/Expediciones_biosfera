package itesm.mx.expediciones_biosfera.behavior.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.sql.Date;

import itesm.mx.expediciones_biosfera.R;
import itesm.mx.expediciones_biosfera.entities.models.Destination;
import itesm.mx.expediciones_biosfera.entities.models.Trip;

public class TripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Destination destination = new Destination("Parras", "Coahuila", "Parras", 25.442905, -102.176722, "Pueblo magico", null);
        Trip trip = new Trip("Titulo", Date.valueOf("2015-15-01"), 20, 500.13, 3, destination, null);
        
        getActionBar().setTitle(trip.getTitle());

        setContentView(R.layout.activity_trip);
    }
}
