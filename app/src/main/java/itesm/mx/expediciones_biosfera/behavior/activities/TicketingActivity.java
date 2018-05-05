package itesm.mx.expediciones_biosfera.behavior.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import itesm.mx.expediciones_biosfera.R;
import itesm.mx.expediciones_biosfera.database.operations.FirestoreReservationHelper;
import itesm.mx.expediciones_biosfera.entities.models.Reservation;

/**
 * Created by emiliogonzalez on 5/4/18.
 */

public class TicketingActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int PICK_IMAGE = 2;
    public static final String RESERVATION_OBJECT = "RESERVATION_OBJECT";
    public static final String RESERVATION_REFERENCE = "RESERVATION_REFERENCE";

    private TextView tvHeader;
    private TextView tvDescription;
    private TextView tvNextSteps;
    private ImageView ivExample;
    private TextView tvTakePicture;
    private Button btnSelectPicture;
    private Button btnTakePicture;
    private TextView btnUploadPicture;
    private ImageView ivPreviewImage;
    private Bitmap ticket;
    private Reservation reservation;
    private String reservationReference;

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void selectFromCarousel() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private StorageReference getTicketRef(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://expedicionesbiosfera.appspot.com/tickets");
        return storageRef.child(System.currentTimeMillis()+".jpg");
    }

    private byte[] getDataFromImage(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ticket.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    private void uploadTicketToFirebase() {
        final StorageReference ticketRef = getTicketRef();

        byte[] data = getDataFromImage();

        UploadTask uploadTask = ticketRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                System.out.println("FAILURE!!!");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("SUCCESS!!!");
                Uri ticketReference = taskSnapshot.getDownloadUrl();
                updateReservationObject(ticketReference.toString());
            }
        });
    }

    private void updateReservationObject(String ticketReference) {
        FirestoreReservationHelper reservationHelper = new FirestoreReservationHelper();
        if(reservationReference != null){
            reservationHelper.setTicketUrl(reservationReference,ticketReference);
            reservationHelper.setPaidPending(reservationReference);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK  && data != null) {
            System.out.println("Request image capture");
            Bundle extras = data.getExtras();
            ticket = (Bitmap) extras.get("data");
            ivPreviewImage.setImageBitmap(ticket);
        }
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ticket = BitmapFactory.decodeStream(imageStream);
            ivPreviewImage.setImageBitmap(ticket);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketing);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            reservation = (Reservation) bundle.getSerializable(RESERVATION_OBJECT);
            reservationReference = (String) bundle.getSerializable(RESERVATION_REFERENCE);
        }

        tvHeader = (TextView) findViewById(R.id.header_text);
        tvDescription = (TextView) findViewById(R.id.description_text);
        tvNextSteps = (TextView) findViewById(R.id.next_steps_text);
        ivExample = (ImageView) findViewById(R.id.example_image);
        ivPreviewImage = (ImageView) findViewById(R.id.preview_image);
        tvTakePicture = (TextView) findViewById(R.id.take_picture_text);
        btnSelectPicture = (Button) findViewById(R.id.select_picture_button);
        btnTakePicture = (Button) findViewById(R.id.take_picture_button);
        btnUploadPicture = (TextView) findViewById(R.id.upload_picture_button);

        tvHeader.setText(R.string.ticketing_header_text);
        tvDescription.setText(R.string.ticketing_description_text);
        tvNextSteps.setText(R.string.ticketing_next_steps_text);
        tvTakePicture.setText(R.string.ticketing_take_picture_text);
        btnSelectPicture.setText(R.string.ticketing_select_picture_button);
        btnTakePicture.setText(R.string.ticketing_take_picture_button);
        btnUploadPicture.setText(R.string.ticketing_upload_picture_button);
        ivExample.setImageResource(R.drawable.ticketing_example);

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        btnSelectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFromCarousel();
            }
        });

        btnUploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadTicketToFirebase();
            }
        });
    }


}
