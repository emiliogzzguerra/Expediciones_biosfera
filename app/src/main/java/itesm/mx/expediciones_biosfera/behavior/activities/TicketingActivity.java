package itesm.mx.expediciones_biosfera.behavior.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import itesm.mx.expediciones_biosfera.R;

/**
 * Created by emiliogonzalez on 5/4/18.
 */

public class TicketingActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int PICK_IMAGE = 2;


    private TextView tvStatus;
    private TextView tvHeader;
    private TextView tvDescription;
    private TextView tvNextSteps;
    private ImageView ivExample;
    private TextView tvTakePicture;
    private Button btnSelectPicture;
    private Button btnTakePicture;
    private TextView btUploadPicture;
    private ImageView ivPreviewImage;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            System.out.println("Request image capture");
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivPreviewImage.setImageBitmap(imageBitmap);
        }
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
            ivPreviewImage.setImageBitmap(yourSelectedImage);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketing);

        tvStatus = (TextView) findViewById(R.id.status_text);
        tvHeader = (TextView) findViewById(R.id.header_text);
        tvDescription = (TextView) findViewById(R.id.description_text);
        tvNextSteps = (TextView) findViewById(R.id.next_steps_text);
        ivExample = (ImageView) findViewById(R.id.example_image);
        ivPreviewImage = (ImageView) findViewById(R.id.preview_image);
        tvTakePicture = (TextView) findViewById(R.id.take_picture_text);
        btnSelectPicture = (Button) findViewById(R.id.select_picture_button);
        btnTakePicture = (Button) findViewById(R.id.take_picture_button);
        btUploadPicture = (TextView) findViewById(R.id.upload_picture_button);

        tvStatus.setText(R.string.ticketing_status_text);
        tvHeader.setText(R.string.ticketing_header_text);
        tvDescription.setText(R.string.ticketing_description_text);
        tvNextSteps.setText(R.string.ticketing_next_steps_text);
        tvTakePicture.setText(R.string.ticketing_take_picture_text);
        btnSelectPicture.setText(R.string.ticketing_select_picture_button);
        btnTakePicture.setText(R.string.ticketing_take_picture_button);
        btUploadPicture.setText(R.string.ticketing_upload_picture_button);
        ivExample.setImageResource(R.drawable.ticketing_example);

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        btnSelectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

    }


}
