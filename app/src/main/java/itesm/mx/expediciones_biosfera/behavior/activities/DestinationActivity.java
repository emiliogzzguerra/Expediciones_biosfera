package itesm.mx.expediciones_biosfera.behavior.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ir.apend.slider.model.Slide;
import ir.apend.slider.ui.Slider;

import itesm.mx.expediciones_biosfera.R;
import itesm.mx.expediciones_biosfera.entities.models.Destination;
import itesm.mx.expediciones_biosfera.utilities.StringFormatHelper;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class DestinationActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DESTINATION_OBJECT = "destination_object";

    Destination destination;

    ScrollView mainScrollView;
    ImageView transparentImageView;

    TextView tvDescription;
    TextView tvDuration;
    TextView tvPrice;
    TextView tvLocation;

    Button btnReserve;

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

        configureSlider();

        setViews();

    }

    private void configureActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String actionBarTitle = destination.getName();
        getSupportActionBar().setTitle(actionBarTitle);
    }

    private void findViews() {
        sliderImages = findViewById(R.id.sliderImages);
        mainScrollView = findViewById(R.id.scroll_view);
        transparentImageView = findViewById(R.id.transparent_image);
        tvDescription = findViewById(R.id.text_description);
        tvDuration = findViewById(R.id.text_duration);
        tvPrice = findViewById(R.id.text_price);
        tvLocation = findViewById(R.id.text_location);
        btnReserve = findViewById(R.id.btn_rsvp);

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

    private void setViews() {
        String description = destination.getDescription().replaceAll("\\\\n", "\n\n");
        String duration = String.format(getResources().getString(R.string.duration_text), destination.getDuration());
        String price = StringFormatHelper.getPriceFormat(destination.getPrice(), getResources());

        tvDescription.setText(description);
        tvDuration.setText(duration);
        tvPrice.setText(price);
        tvLocation.setText(destination.getCity() + ", " + destination.getState());

        if(Build.VERSION.SDK_INT >= 26) {
            tvDescription.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }

        btnReserve.setOnClickListener(this);

    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_rsvp:
                //startReservation();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.learn_more_destination, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.google_destination:
                googleDestination();
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void googleDestination() {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, destination.getName()); // query contains search string
        startActivity(intent);
    }

}