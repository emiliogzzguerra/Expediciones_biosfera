package itesm.mx.expediciones_biosfera.behavior.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import itesm.mx.expediciones_biosfera.R;
import itesm.mx.expediciones_biosfera.entities.models.Destination;

public class DestinationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    FirebaseFirestore db;
    ImageView ivPhoto;
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
        ivPhoto = findViewById(R.id.iv_main_image);
        tvLocation = findViewById(R.id.tv_location);
        tvDate = findViewById(R.id.text_date);
        tvDescription = findViewById(R.id.tv_description);
        tvPrice = findViewById(R.id.text_price);
        tvDuration = findViewById(R.id.tv_duration);
        tvCapacity = findViewById(R.id.tv_capacity);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            destination = (Destination) bundle.getSerializable("Destination");
        }
            Glide.with(ivPhoto.getContext())
                    .load("https://firebasestorage.googleapis.com/v0/b/expedicionesbiosfera.appspot.com/o/Cuatro%20cienegas%2Fcuatro-cienegas-coahuila-pueblo-magico.1600.jpg?alt=media&token=59c33c6d-ccbc-46a4-b6d1-16a0ea4c08ad")
                    .placeholder(R.drawable.common_google_signin_btn_text_light)
                    .dontAnimate()
                    .into(ivPhoto);
            location = new LatLng(destination.getLat(), destination.getLon());
            tvLocation.setText(destination.getState() + ", " + destination.getCity());
            getSupportActionBar().setTitle(destination.getName());
            tvDescription.setText(destination.getDescription().replaceAll("\\\\n", "\n"));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(location));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 7.0f));
    }
}