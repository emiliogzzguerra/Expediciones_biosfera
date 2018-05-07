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
import itesm.mx.expediciones_biosfera.utilities.FirestoreReservationHelper;

public class ReservationAdminDetailActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String RESERVATION_REFERENCE = "RESERVATION_REFERENCE";
    public static final String RESERVATION = "RESERVATION";
    public static final String CUSTOMER = "CUSTOMER";
    public static final String DESTINATION = "DESTINATION";

    private String destination;
    private String customer;
    private String reservationReference;
    private TextView tvCustomer;
    private TextView tvDate;
    private TextView tvDestination;
    private TextView tvPrice;
    private TextView tvStatus;
    private Button btnAccept;
    private Button btnReject;
    private Reservation reservation;

    private void findViews(){
        tvCustomer = this.findViewById(R.id.text_customer);
        tvDate =  this.findViewById(R.id.text_date);
        tvDestination = this.findViewById(R.id.text_destination);
        tvPrice = this.findViewById(R.id.text_price);
        tvStatus = this.findViewById(R.id.text_status);
        btnAccept = this.findViewById(R.id.button_accept);
        btnReject = this.findViewById(R.id.button_reject);
    }

    private void setViewsAndButtons(Reservation reservation){
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
        btnAccept.setOnClickListener(this);
        btnReject.setOnClickListener(this);
    }

    private void getDataFromIntent(){
        reservation = (Reservation) getIntent().getSerializableExtra(RESERVATION);
        destination = getIntent().getExtras().getString(DESTINATION);
        customer = getIntent().getExtras().getString(CUSTOMER);
        reservationReference = getIntent().getExtras().getString(RESERVATION_REFERENCE);
    }

    private void sendAdminBackToReservations(){
        finish();
    }

    private void changeConfirmationStatus(Boolean b){
        FirestoreReservationHelper reservationHelper = new FirestoreReservationHelper();
        Toast statusToast;
        if(b){
            reservationHelper.setConfirmedApproved(reservationReference);
            statusToast = Toast.makeText(getApplicationContext(),
                    "Reservación aprobada", Toast.LENGTH_LONG);
            statusToast.show();
        } else {
            reservationHelper.setConfirmedDeclined(reservationReference);
            statusToast = Toast.makeText(getApplicationContext(),
                    "Reservación declinada", Toast.LENGTH_LONG);
            statusToast.show();
        }
        sendAdminBackToReservations();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_detail);

        configureActionBar();

        getDataFromIntent();

        findViews();

        setViewsAndButtons(reservation);
    }

    private void configureActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //String actionBarTitle = destination.getName();
        //getSupportActionBar().setTitle(actionBarTitle);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_accept:
                changeConfirmationStatus(true);
                break;
            case R.id.button_reject:
                changeConfirmationStatus(false);
                break;
            default:
                break;
        }
    }
}
