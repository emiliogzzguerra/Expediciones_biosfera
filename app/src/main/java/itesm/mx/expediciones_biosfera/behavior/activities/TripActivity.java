package itesm.mx.expediciones_biosfera.behavior.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import itesm.mx.expediciones_biosfera.R;
import itesm.mx.expediciones_biosfera.entities.models.Trip;

public class TripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Trip trip = new Trip("Titulo", null, 20, 500.13, 3, null, null);
        
        getActionBar().setTitle(trip.getTitle());

        setContentView(R.layout.activity_trip);
    }
}
