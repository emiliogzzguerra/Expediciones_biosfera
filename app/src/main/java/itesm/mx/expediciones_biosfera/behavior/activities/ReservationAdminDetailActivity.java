package itesm.mx.expediciones_biosfera.behavior.activities;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    private ImageView ivTicket;

    private void findViews(){
        tvCustomer = this.findViewById(R.id.text_customer);
        tvDate =  this.findViewById(R.id.text_date);
        tvDestination = this.findViewById(R.id.text_destination);
        tvPrice = this.findViewById(R.id.text_price);
        tvStatus = this.findViewById(R.id.text_status);
        btnAccept = this.findViewById(R.id.button_accept);
        btnReject = this.findViewById(R.id.button_reject);
        ivTicket = this.findViewById(R.id.image_ticket);
    }

    public String getStatusMessage(String status) {
        Resources resources = getResources();
        int resourceId;
        switch (status) {
            case "approved":
                resourceId = R.string.approved;
                break;
            case "denied":
                resourceId = R.string.denied;
                break;
            default:
                resourceId = R.string.pending;
                break;
        }
        return resources.getString(resourceId);
    }

    private void setTicketImage() {
        Glide.with(ivTicket.getContext())
                .load(reservation.getTicketUrl())
                .dontAnimate()
                .into(ivTicket);
        ivTicket.setVisibility(View.VISIBLE);
    }

    private void setViewsAndButtons(){
        tvCustomer.setText(customer);
        tvDate.setText(reservation.getInitialDate().toString());
        tvDestination.setText(destination);
        tvPrice.setText("$"+String.valueOf(reservation.getPrice()));

        if(reservation.getIsPaid() != null) {
            String status = "";
            if (!reservation.getIsConfirmed().equals("approved")) {
                status = "Confirmación: " + getStatusMessage(reservation.getIsConfirmed());
                 btnAccept.setOnClickListener(this);
                 btnReject.setOnClickListener(this);
            } else if (reservation.getIsPaid() != null) {
                String statusMessage = getStatusMessage(reservation.getIsPaid());
                if(reservation.getIsPaid().equals("pending")) {
                    if(reservation.getTicketUrl() == null) {
                        btnAccept.setEnabled(false);
                        btnReject.setEnabled(false);
                    } else {
                        //Ticket is available
                        setTicketImage();
                        btnAccept.setEnabled(true);
                        btnReject.setEnabled(true);
                        btnAccept.setOnClickListener(this);
                        btnReject.setOnClickListener(this);
                    }
                } else if(reservation.getIsPaid().equals("approved")) {
                    setTicketImage();
                    btnAccept.setVisibility(View.GONE);
                    btnReject.setVisibility(View.GONE);
                }
                status = "Pago: " + statusMessage;
            }
            tvStatus.setText(status);
        }
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

    public void updatePaymentStatus(boolean isAccepted) {
        FirestoreReservationHelper reservationHelper = new FirestoreReservationHelper();
        Toast statusToast;

        if(isAccepted) {
            reservationHelper.setPaymentApproved(reservationReference);
            statusToast = Toast.makeText(getApplicationContext(), "Pago aprobado", Toast.LENGTH_LONG);
            statusToast.show();
        } else {
            reservationHelper.setPaymentDeclined(reservationReference);
            statusToast = Toast.makeText(getApplicationContext(), "Pago rechazado", Toast.LENGTH_LONG);
            statusToast.show();
        }
    }

    public void updateReservationConfirmation(boolean isAccepted) {
        FirestoreReservationHelper reservationHelper = new FirestoreReservationHelper();
        Toast statusToast;

        if(isAccepted){
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
    }

    private void updateStatus(Boolean isAccepted){
        if(reservation.getIsConfirmed().equals("approved")) {
            //The approval happens in payment
            updatePaymentStatus(isAccepted);
        } else {
            //The approval is for reservation confirmation
            updateReservationConfirmation(isAccepted);
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

        setViewsAndButtons();
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
                updateStatus(true);
                break;
            case R.id.button_reject:
                updateStatus(false);
                break;
            default:
                break;
        }
    }
}
