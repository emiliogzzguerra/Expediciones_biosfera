package itesm.mx.expediciones_biosfera.behavior.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Calendar;
import java.util.Date;
import itesm.mx.expediciones_biosfera.R;
import itesm.mx.expediciones_biosfera.utilities.FirestoreReservationHelper;
import itesm.mx.expediciones_biosfera.entities.models.Destination;
import itesm.mx.expediciones_biosfera.entities.models.Reservation;
import itesm.mx.expediciones_biosfera.utilities.StringFormatHelper;

public class ReservationActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String DESTINATION_OBJECT = "destination_object";

    private Destination destination;
    private TextView tvSelectSize;
    private SeekBar sbSize;
    private TextView tvDate;
    private Button btnDatePicker;
    private TextView tvSummary;
    private TextView tvEstimate;
    private int costPerPerson;
    private int totalPrice;
    private String destinationTitle;
    private Button btnPreReservation;
    private int progressChangedValue;
    private Calendar calendarDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            destination = (Destination) bundle.getSerializable(DESTINATION_OBJECT);
        }

        configureActionBar();

        findViews();

        setVariables();

        configureSeekBar();

        configureDatePicker();

        setView();

        setButtonListeners();

    }

    public void redirectToPackages() {
        finish();
    }

    private void createReservation() {
        FirebaseAuth firebaseAuth;
        FirebaseUser fbuser;
        firebaseAuth = FirebaseAuth.getInstance();
        fbuser = firebaseAuth.getCurrentUser();
        String userReference = fbuser.getUid();

        Reservation reservation = new Reservation(progressChangedValue,
                            totalPrice, "pending", "pending",
                    null, userReference, destination.getReference(),
                            calendarDate.getTime());
        FirestoreReservationHelper.addReservation(reservation);
        Toast.makeText(this, "Se ha creado una nueva solicitud", Toast.LENGTH_SHORT).show();
        redirectToPackages();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pre_reservation:
                createReservation();
                break;
        }
    }

    private void configureActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String destinationName = destination.getState() + ", " + destination.getCity();
        String actionBarTitle = String.format(getResources().getString(R.string.reservation_action_bar_title), destinationName);
        getSupportActionBar().setTitle(actionBarTitle);
    }

    private void findViews() {
        tvSelectSize = findViewById(R.id.text_select_size);
        sbSize = findViewById(R.id.sb_size);
        tvDate = findViewById(R.id.text_date);
        btnDatePicker = findViewById(R.id.btn_change_date);
        tvSummary = findViewById(R.id.text_summary);
        tvEstimate = findViewById(R.id.text_estimate);
        btnPreReservation = findViewById(R.id.btn_pre_reservation);
    }

    private void setVariables() {
        progressChangedValue = 2;
        costPerPerson = destination.getPrice();
        destinationTitle = destination.getCity();
    }

    private void configureSeekBar() {
        sbSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                tvSelectSize.setText(getResources().getString(R.string.reservation_select_size, progressChangedValue));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                if(sbSize.getProgress() == 0) {
                    sbSize.setProgress(1);
                }

                tvSummary.setText(String.format(getResources().getString(R.string.reservation_summary),
                        destinationTitle, progressChangedValue, StringFormatHelper.getDateAsString(calendarDate.getTime(), false)
                ));

                totalPrice = costPerPerson * sbSize.getProgress();
                updatePriceView();
            }
        });

    }

    private void configureDatePicker() {
        calendarDate = Calendar.getInstance();
        calendarDate.setTime(new Date());
        calendarDate.add( Calendar.DATE, 7 );
        while(calendarDate.get( Calendar.DAY_OF_WEEK ) != Calendar.MONDAY ) {
            calendarDate.add( Calendar.DATE, 1 );
        }

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendarDate.set(Calendar.YEAR, year);
                calendarDate.set(Calendar.MONTH, monthOfYear);
                calendarDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateDateView();
            }

        };

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(ReservationActivity.this,
                        R.style.MyDatePickerDialogTheme, date,
                        calendarDate.get(Calendar.YEAR), calendarDate.get(Calendar.MONTH), calendarDate.get(Calendar.DAY_OF_MONTH));

                Date minDate = getFutureDate(10);
                Date maxDate = getFutureDate(200);

                dialog.getDatePicker().setMinDate(minDate.getTime());
                dialog.getDatePicker().setMaxDate(maxDate.getTime());

                dialog.show();

            }
        });
    }

    private Date getFutureDate(int days) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    private void updateDateView() {
        tvDate.setText(StringFormatHelper.getDateAsString(calendarDate.getTime(), true));
    }

    private void updatePriceView() {
        tvEstimate.setText(StringFormatHelper.getPriceFormat(totalPrice, getResources()));
    }

    private void setView() {
        tvSelectSize.setText(getResources().getString(R.string.reservation_select_size,2));

        tvSummary.setText(String.format(getResources().getString(R.string.reservation_summary),
                destinationTitle, progressChangedValue, StringFormatHelper.getDateAsString(calendarDate.getTime(), false)));

        totalPrice = costPerPerson * sbSize.getProgress();

        updatePriceView();
        updateDateView();
    }

    private void setButtonListeners() {
        btnPreReservation.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
