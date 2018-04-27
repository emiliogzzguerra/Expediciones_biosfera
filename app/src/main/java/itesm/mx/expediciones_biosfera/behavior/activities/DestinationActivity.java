package itesm.mx.expediciones_biosfera.behavior.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.w3c.dom.Text;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import itesm.mx.expediciones_biosfera.R;
import itesm.mx.expediciones_biosfera.entities.models.Destination;
import itesm.mx.expediciones_biosfera.entities.models.Trip;

public class DestinationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    FirebaseFirestore db;
    TextView tvLocation;
    TextView tvDate;
    TextView tvDescription;
    TextView tvPrice;
    TextView tvDuration;
    TextView tvCapacity;
    LatLng location;
    Map<String, Object> mapTrip, mapDestination;
    Destination destination;
    static DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        tvLocation = findViewById(R.id.tv_location);
        tvDate = findViewById(R.id.tv_date);
        tvDescription = findViewById(R.id.tv_description);
        tvPrice = findViewById(R.id.tv_price);
        tvDuration = findViewById(R.id.tv_duration);
        tvCapacity = findViewById(R.id.tv_capacity);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            destination = (Destination) bundle.getSerializable("Destination");
        }
            location = new LatLng(destination.getLat(), destination.getLon());
            tvLocation.setText(destination.getState() + ", " + destination.getCity());
            getSupportActionBar().setTitle(destination.getName());
            tvDescription.setText(destination.getDescription());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(location));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 7.0f));
    }
}