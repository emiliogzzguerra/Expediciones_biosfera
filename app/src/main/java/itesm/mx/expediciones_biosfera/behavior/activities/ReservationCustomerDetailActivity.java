package itesm.mx.expediciones_biosfera.behavior.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import itesm.mx.expediciones_biosfera.R;
import itesm.mx.expediciones_biosfera.utilities.FirestoreReservationHelper;
import itesm.mx.expediciones_biosfera.entities.models.Reservation;
import itesm.mx.expediciones_biosfera.utilities.StringFormatHelper;

public class ReservationCustomerDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int PICK_IMAGE = 2;
    public static final String RESERVATION_OBJECT = "RESERVATION_OBJECT";
    public static final String RESERVATION_REFERENCE = "RESERVATION_REFERENCE";
    public static final String DESTINATION_TITLE = "DESTINATION_TITLE";

    private TextView tvDescription;
    private ImageView ivExample;
    private Button btnSelectPicture;
    private Button btnTakePicture;
    private TextView btnUploadPicture;
    private ImageView ivPreviewImage;
    private Bitmap ticket;
    private Reservation reservation;
    private String reservationReference;
    private String destinationTitle;
    private ProgressBar progressBarTicketSubmission;
    private ProgressBar progressBarCurrentTicket;

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        btnUploadPicture.setEnabled(false);

    }

    private void selectFromCarousel() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        btnUploadPicture.setEnabled(false);

    }

    private StorageReference getTicketRef() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(getResources()
                .getString(R.string.ticket_reference));
        return storageRef.child(System.currentTimeMillis()+".jpg");
    }

    private byte[] getDataFromImage(){
        if(ticket != null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ticket.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return baos.toByteArray();
        } else {
            return null;
        }
    }

    private NetworkInfo getNetworkStatus(){
        ConnectivityManager connectivityManager =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    private void uploadTicketToFirebase() {
        if(getNetworkStatus() != null){
            if(reservation.getIsPaid().equals("approved")) {
                Toast.makeText(this, "Tu pago ya fue aprobado, no es necesario que subas otra foto", Toast.LENGTH_LONG).show();
            } else {
                final StorageReference ticketRef = getTicketRef();
                byte[] data = getDataFromImage();

                UploadTask uploadTask = ticketRef.putBytes(data);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Log.d("UPLOAD","Failure, unsuccesful upload");
                        Toast failureToast = Toast.makeText(getApplicationContext(),
                                "No se pudo enviar con éxito", Toast.LENGTH_LONG);
                        failureToast.show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("UPLOAD","Succesful upload");
                        Uri ticketReference = taskSnapshot.getDownloadUrl();
                        updateReservationObject(ticketReference.toString());
                        Toast successToast = Toast.makeText(getApplicationContext(),
                                "Enviado con éxito", Toast.LENGTH_LONG);
                        successToast.show();
                        sendUserToReservationList();
                    }
                });

                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        int currentProgress = (int) progress;
                        progressBarTicketSubmission.setProgress(currentProgress);
                    }
                }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });

            }
        } else {
            Toast.makeText(this
                    , "Se necesita conectividad para subir tu comprobante", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateReservationObject(String ticketReference) {
        if(reservationReference != null){
            FirestoreReservationHelper.setTicketUrl(reservationReference, ticketReference);
            FirestoreReservationHelper.setPaidPending(reservationReference);
        }
    }

    private void sendUserToReservationList() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK  && data != null) {
            Bundle extras = data.getExtras();
            ticket = (Bitmap) extras.get("data");
            ivPreviewImage.setImageBitmap(ticket);
            btnUploadPicture.setEnabled(true);
        }
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            final Uri selectedImage = data.getData();

            progressBarCurrentTicket.setVisibility(View.VISIBLE);

            Glide.with(ivPreviewImage.getContext())
                    .load(selectedImage)
                    .asBitmap()
                    .fitCenter()
                    .dontAnimate()
                    .listener(new RequestListener<Uri, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, Uri model, Target<Bitmap> target, boolean isFirstResource) {
                            // log exception
                            Log.e("TAG", "Error loading image", e);
                            selectFromCarousel();
                            Toast.makeText(ReservationCustomerDetailActivity.this, "No se pudo cargar la imagen que seleccionaste", Toast.LENGTH_SHORT).show();
                            return false; // important to return false so the error placeholder can be placed
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Uri model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBarCurrentTicket.setVisibility(View.GONE);
                            ticket = resource;
                            btnUploadPicture.setEnabled(true);

                            return false;
                        }
                    })
                    .into(ivPreviewImage);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_customer_detail);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            reservation = (Reservation) bundle.getSerializable(RESERVATION_OBJECT);
            reservationReference = (String) bundle.getSerializable(RESERVATION_REFERENCE);
            destinationTitle = (String) bundle.getSerializable(DESTINATION_TITLE);
        }

        configureActionBar();

        findViews();

        setViews();

    }

    private void findViews() {
        tvDescription = findViewById(R.id.description_text);
        ivExample = findViewById(R.id.example_image);
        ivPreviewImage = findViewById(R.id.preview_image);
        btnSelectPicture = findViewById(R.id.select_picture_button);
        btnTakePicture = findViewById(R.id.take_picture_button);
        btnUploadPicture = findViewById(R.id.upload_picture_button);
        progressBarTicketSubmission = findViewById(R.id.progress_bar_submit_picture);
        progressBarCurrentTicket = findViewById(R.id.progress_bar_current_ticket);
    }

    private void setViews() {
        String formattedPrice = StringFormatHelper.getPriceFormat(reservation.getPrice(), getResources());

        String formattedDate = StringFormatHelper.getDateAsString(reservation.getInitialDate(), false);

        tvDescription.setText(String.format(getResources().getString(R.string.rescusdetail_description_text),
                destinationTitle, formattedDate, formattedPrice
        ));

        ivExample.setImageResource(R.drawable.rescusdetail_example);

        btnTakePicture.setOnClickListener(this);

        btnSelectPicture.setOnClickListener(this);

        if(reservation.getTicketUrl() == null){
            ivPreviewImage.setImageDrawable(getDrawable(R.drawable.ticket_placeholder));
            progressBarCurrentTicket.setVisibility(View.GONE);

        } else {
            Glide.with(ivPreviewImage.getContext())
                    .load(reservation.getTicketUrl())
                    .dontAnimate()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBarCurrentTicket.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(ivPreviewImage);
            btnUploadPicture.setText(R.string.rescusdetail_replace_picture_button);

        }

        btnUploadPicture.setOnClickListener(this);

        btnUploadPicture.setEnabled(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_picture_button:
                takePicture();
                break;
            case R.id.select_picture_button:
                selectFromCarousel();
                break;
            case R.id.upload_picture_button:
                uploadTicketToFirebase();
                break;
            default:
        }
    }

    private void configureActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String actionBarTitle = "Estado de solicitud";
        getSupportActionBar().setTitle(actionBarTitle);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}