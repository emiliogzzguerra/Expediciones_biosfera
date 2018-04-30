package itesm.mx.expediciones_biosfera.behavior.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

import itesm.mx.expediciones_biosfera.R;

/**
 * Created by emiliogonzalez on 4/27/18.
 */

public class ReservationActivity extends AppCompatActivity implements View.OnClickListener {

		private TextView tvHeader;
		private TextView tvSelectSize;
		private SeekBar sbSize;
		private TextView tvFecha;
		private DatePicker dp;
		private TextView tvSummary;
		private TextView tvEstimate;
        private int costPerPerson;
        private String destinationTitle;
		private Button btPreReservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        Resources res = getResources();

        //Uncomment to get the real price per person and calculate the total price
        //Intent intent = getIntent();
        costPerPerson = 100; //Remove this line when uncommenting
        destinationTitle = "Dunas de Juarez"; //Remove this line when uncommenting
        //costPerPerson = intent.getStringExtra(Paquete.PRICE_PER_PERSON);
        //destinationTitle = intent.getStringExtra(Paquete.DESTINATION_TITLE);

        tvHeader = (TextView) findViewById(R.id.tv_header);
        tvSelectSize = (TextView) findViewById(R.id.tv_select_size);
        sbSize = (SeekBar) findViewById(R.id.sb_size);
        tvFecha = (TextView) findViewById(R.id.tv_fecha);
        dp = (DatePicker) findViewById(R.id.dp);
        tvSummary = (TextView) findViewById(R.id.tv_summary);
        tvEstimate = (TextView) findViewById(R.id.tv_estimate);
        btPreReservation = (Button) findViewById(R.id.bt_pre_reservation);

        btPreReservation.setOnClickListener(this);

        sbSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 2;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                tvSelectSize.setText("¿Cuantas personas asistirán al viaje? (" + progressChangedValue + ")");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(ReservationActivity.this, "Cantidad de personas:" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
            }
        });

        String auxString = res.getString(R.string.reservation_summary,destinationTitle,
                dp.getDayOfMonth(),dp.getMonth(),dp.getYear());

        tvEstimate.setText(res.getString(R.string.reservation_price_estimate,
                                costPerPerson * sbSize.getProgress()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_pre_reservation:

                break;
        }
    }
}
