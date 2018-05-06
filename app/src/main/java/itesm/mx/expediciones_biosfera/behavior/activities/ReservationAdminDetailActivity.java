package itesm.mx.expediciones_biosfera.behavior.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SupportErrorDialogFragment;

import itesm.mx.expediciones_biosfera.R;
import itesm.mx.expediciones_biosfera.entities.models.Reservation;

public class ReservationAdminDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_detail);

        Reservation reservation = (Reservation) getIntent().getSerializableExtra("reservation");


        String destination = getIntent().getExtras().getString("destination");
        String customer = getIntent().getExtras().getString("customer");

        TextView tvCustomer = this.findViewById(R.id.text_customer);
        TextView tvDate =  this.findViewById(R.id.text_date);
        TextView tvDestination = this.findViewById(R.id.text_destination);
        TextView tvPrice = this.findViewById(R.id.text_price);
        TextView tvStatus = this.findViewById(R.id.text_status);

        Button btnAccept = this.findViewById(R.id.button_accept);
        Button btnReject = this.findViewById(R.id.button_reject);

        tvCustomer.setText(customer);
        tvDate.setText(reservation.getInitialDate().toString());
        tvDestination.setText(destination);
        tvPrice.setText("$"+String.valueOf(reservation.getPrice()));

        if(reservation.getIsPaid() != null) {
            if (!reservation.getIsConfirmed().equals("Aprobado")) {
                tvStatus.setText("Confirmacion: " + reservation.getIsConfirmed());
            } else if (reservation.getIsPaid() != null) {
                tvStatus.setText("Pago: " + reservation.getIsPaid());
            }
        }

        View.OnClickListener accion = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()){
                    case R.id.button_accept:
                        Toast.makeText(getBaseContext(), "Acepta", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.button_reject:
                        Toast.makeText(getBaseContext(), "Rechaza", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };

        btnAccept.setOnClickListener(accion);
        btnReject.setOnClickListener(accion);



    }
}
