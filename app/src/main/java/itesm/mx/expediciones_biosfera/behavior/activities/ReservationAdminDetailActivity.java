package itesm.mx.expediciones_biosfera.behavior.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import itesm.mx.expediciones_biosfera.R;
import itesm.mx.expediciones_biosfera.entities.models.Reservation;
import itesm.mx.expediciones_biosfera.utilities.FirestoreReservationHelper;
import itesm.mx.expediciones_biosfera.utilities.StringFormatHelper;

public class ReservationAdminDetailActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String RESERVATION_REFERENCE = "RESERVATION_REFERENCE";
    public static final String RESERVATION = "RESERVATION";
    public static final String CUSTOMER = "CUSTOMER";
    public static final String EMAIL = "EMAIL";
    public static final String DESTINATION = "DESTINATION";

    private String destination;
    private String customer;
    private String email;
    private String reservationReference;
    private TextView tvCustomer;
    private TextView tvCustomerEmail;
    private TextView tvDate;
    private TextView tvDestination;
    private TextView tvPrice;
    private TextView tvStatus;
    private Button btnAccept;
    private Button btnReject;
    private Reservation reservation;
    private ImageView ivTicket;
    private RelativeLayout layoutTicketImage;

    private void findViews(){
        tvCustomer = this.findViewById(R.id.text_customer);
        tvCustomerEmail = this.findViewById(R.id.text_customer_email);
        tvDate =  this.findViewById(R.id.text_date);
        tvDestination = this.findViewById(R.id.text_destination);
        tvPrice = this.findViewById(R.id.text_price);
        tvStatus = this.findViewById(R.id.text_status);
        btnAccept = this.findViewById(R.id.button_accept);
        btnReject = this.findViewById(R.id.button_reject);
        ivTicket = this.findViewById(R.id.image_ticket);
        layoutTicketImage = this.findViewById(R.id.layout_image);
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
        layoutTicketImage.setVisibility(View.VISIBLE);
    }

    public void sendEmail() {
        String email = tvCustomerEmail.getText().toString();
        String[] TO = {email};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void setOnClickEmail() {
        tvCustomerEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    private void setViewsAndButtons(){
        tvCustomer.setText(customer);
        tvCustomerEmail.setText(email);
        tvCustomerEmail.setPaintFlags(tvCustomerEmail.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        setOnClickEmail();
        tvDate.setText(StringFormatHelper.getDateAsString(reservation.getInitialDate(), true));
        tvDestination.setText(destination);
        tvPrice.setText(StringFormatHelper.getPriceFormat(reservation.getPrice(), getResources()));

        if(reservation.getIsPaid() != null) {
            String status = "";
            if (!reservation.getIsConfirmed().equals("approved")) {
                status = String.format(getResources().getString(R.string.confirmation_text), getStatusMessage(reservation.getIsConfirmed()));
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
                status = String.format(getResources().getString(R.string.payment_text), getStatusMessage(reservation.getIsPaid()));
            }
            tvStatus.setText(status);
        }
    }

    private void getDataFromIntent(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        reservation = (Reservation) intent.getSerializableExtra(RESERVATION);
        destination = extras.getString(DESTINATION);
        customer = extras.getString(CUSTOMER);
        email = extras.getString(EMAIL);
        reservationReference = extras.getString(RESERVATION_REFERENCE);
    }

    private void sendAdminBackToReservations(){
        finish();
    }

    public void updatePaymentStatus(boolean isAccepted) {
        FirestoreReservationHelper reservationHelper = new FirestoreReservationHelper();
        Toast statusToast;
        if(reservationReference != null) {
            if (isAccepted) {
                reservationHelper.setPaymentApproved(reservationReference);
                statusToast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.payment_approved_simple), Toast.LENGTH_LONG);
                statusToast.show();
            } else {
                reservationHelper.setPaymentDeclined(reservationReference);
                statusToast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.payment_rejected_simple), Toast.LENGTH_LONG);
                statusToast.show();
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.error_try_again_message), Toast.LENGTH_SHORT).show();
        }
    }

    public void updateReservationConfirmation(boolean isAccepted) {
        Toast statusToast;
        if(reservationReference != null) {
            if(isAccepted){
                FirestoreReservationHelper.setConfirmedApproved(reservationReference);
                statusToast = Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.reservation_approved_simple), Toast.LENGTH_SHORT);
                statusToast.show();
            } else {
                FirestoreReservationHelper.setConfirmedDeclined(reservationReference);
                statusToast = Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.payment_rejected_simple), Toast.LENGTH_SHORT);
                statusToast.show();
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.error_try_again_message), Toast.LENGTH_SHORT).show();
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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String actionBarTitle = getResources().getString(R.string.reservation_detail_action_bar_title);
        getSupportActionBar().setTitle(actionBarTitle);

        Window window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.adminPrimaryDarkColor));

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
