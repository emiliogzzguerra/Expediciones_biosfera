package itesm.mx.expediciones_biosfera.behavior.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import itesm.mx.expediciones_biosfera.R;
import itesm.mx.expediciones_biosfera.behavior.fragments.DatePickerFragment;
import itesm.mx.expediciones_biosfera.database.operations.FirestoreReservationHelper;
import itesm.mx.expediciones_biosfera.entities.models.Customer;
import itesm.mx.expediciones_biosfera.entities.models.Reservation;

/**
 * Created by emiliogonzalez on 4/27/18.
 */

public class ReservationActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String DESTINATION_OBJECT = "destination_object";

    private FirebaseFirestore db;
    private TextView tvHeader;
    private TextView tvSelectSize;
    private SeekBar sbSize;
    private TextView tvDate;
    private Button btDatePicker;
    private TextView tvSummaryPt1;
    private TextView tvSummaryPt2;
    private TextView tvEstimate;
    private int costPerPerson;
    private int totalPrice;
    private String destinationTitle;
    private Button btPreReservation;
    private int progressChangedValue;
    private FirestoreReservationHelper reservationHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        reservationHelper = new FirestoreReservationHelper();

        final Resources res = getResources();
        progressChangedValue = 2;

        //Uncomment to get the real price per person and calculate the total price
        //Intent intent = getIntent();
        costPerPerson = 100; //Remove this line when uncommenting
        destinationTitle = "Dunas de Juarez"; //Remove this line when uncommenting
        //costPerPerson = intent.getStringExtra(Paquete.PRICE_PER_PERSON);
        //destinationTitle = intent.getStringExtra(Paquete.DESTINATION_TITLE);

        tvHeader = (TextView) findViewById(R.id.tv_header);
        tvSelectSize = (TextView) findViewById(R.id.tv_select_size);
        sbSize = (SeekBar) findViewById(R.id.sb_size);
        tvDate = (TextView) findViewById(R.id.tv_date);
        btDatePicker = (Button) findViewById(R.id.btn_change);
        tvSummaryPt1 = (TextView) findViewById(R.id.tv_summary_pt1);
        tvSummaryPt2 = (TextView) findViewById(R.id.tv_summary_pt2);
        tvEstimate = (TextView) findViewById(R.id.tv_estimate);
        btPreReservation = (Button) findViewById(R.id.bt_pre_reservation);

        btPreReservation.setOnClickListener(this);

        sbSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                tvSelectSize.setText(res.getString(R.string.reservation_select_size,progressChangedValue));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                tvSummaryPt1.setText(res.getString(R.string.reservation_summary,destinationTitle,
                        progressChangedValue));

                totalPrice = costPerPerson * sbSize.getProgress();
                tvEstimate.setText(res.getString(R.string.reservation_price_estimate,
                        totalPrice));
            }
        });

        tvSelectSize.setText(res.getString(R.string.reservation_select_size,2));

        // Setting initial date to the first monday that is at least 7 days away
        final Calendar c = Calendar.getInstance();
        c.add( Calendar.DATE, 7 );
        while(c.get( Calendar.DAY_OF_WEEK ) != Calendar.MONDAY )
            c.add( Calendar.DATE, 1 );

        tvDate.setText(res.getString(R.string.reservation_date,
                String.format("%02d", c.get(Calendar.DAY_OF_MONTH)),
                String.format("%02d", c.get(Calendar.MONTH)),
                String.format("%02d", c.get(Calendar.YEAR))));

        tvSummaryPt1.setText(res.getString(R.string.reservation_summary,destinationTitle,
                progressChangedValue));

        tvSummaryPt2.setText(getResources()
                .getString(R.string.reservation_summary_part_two,
                        String.format("%02d", c.get(Calendar.DAY_OF_MONTH)),
                        String.format("%02d", c.get(Calendar.MONTH)),
                        String.format("%02d", c.get(Calendar.YEAR))));

        totalPrice = costPerPerson * sbSize.getProgress();
        tvEstimate.setText(res.getString(R.string.reservation_price_estimate,
                                totalPrice));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_pre_reservation:
                Date auxDate;
                Reservation rAux;
                SimpleDateFormat sdfmt1 = new SimpleDateFormat("dd/MM/yy");
                try {
                    System.out.println(tvDate.getText());
                    auxDate = sdfmt1.parse((String) tvDate.getText());
                    System.out.println(auxDate);
                    rAux = new Reservation(progressChangedValue,totalPrice,false,null,"customerReference","tripReference", auxDate);
                } catch (ParseException e) {
                    rAux = new Reservation(progressChangedValue,totalPrice,false,null,"customerReference","tripReference", null);
                    e.printStackTrace();
                }
                reservationHelper.addReservation(rAux);
                break;
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
