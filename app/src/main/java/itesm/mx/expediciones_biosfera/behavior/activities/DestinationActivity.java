package itesm.mx.expediciones_biosfera.behavior.activities;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;
import ir.apend.slider.model.Slide;
import ir.apend.slider.ui.Slider;
import itesm.mx.expediciones_biosfera.R;
import itesm.mx.expediciones_biosfera.entities.models.Destination;
import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class DestinationActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String DESTINATION_OBJECT = "material_type";

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    Destination destination;

    ScrollView mainScrollView;
    ImageView transparentImageView;

    TextView tvDescription;
    TextView tvDuration;
    TextView tvPrice;

    Slider sliderImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            destination = (Destination) bundle.getSerializable(DESTINATION_OBJECT);
        }

        configureActionBar();

        findViews();

        configureMap();

        configureSlider();

        setTextViews();

    }

    private void configureActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String actionBarTitle = destination.getState() + ", " + destination.getCity();
        getSupportActionBar().setTitle(actionBarTitle);
    }

    private void findViews() {
        sliderImages = findViewById(R.id.sliderImages);
        mainScrollView = findViewById(R.id.scroll_view);
        transparentImageView = findViewById(R.id.transparent_image);
        tvDescription = findViewById(R.id.text_description);
        tvDuration = findViewById(R.id.text_duration);
        tvPrice = findViewById(R.id.text_price);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

    }

    private void configureMap() {
        mapFragment.getMapAsync(this);

        transparentImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
    }

    private void configureSlider() {
        if(destination.getImageUrls().size() == 0) {
            sliderImages.setVisibility(View.GONE);
            return;
        }

        List<Slide> slideList = new ArrayList<>();

        for(String imageUrl : destination.getImageUrls()) {
            slideList.add(new Slide(0, imageUrl , getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));

        }

        sliderImages.addSlides(slideList);

    }

    private void setTextViews() {
        String description = destination.getDescription().replaceAll("\\\\n", "\n\n");
        String duration = String.format(getResources().getString(R.string.duration_text), destination.getDuration());
        String price = String.format(getResources().getString(R.string.price_text), destination.getPrice());
        tvDescription.setText(description);
        tvDuration.setText(duration);
        tvPrice.setText(price);

        if(Build.VERSION.SDK_INT >= 26) {
            tvDescription.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        LatLng location = new LatLng(destination.getLat(), destination.getLon());

        mMap.addMarker(new MarkerOptions().position(location).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 7.0f));
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}