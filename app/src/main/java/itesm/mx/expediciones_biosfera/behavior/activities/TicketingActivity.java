package itesm.mx.expediciones_biosfera.behavior.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import itesm.mx.expediciones_biosfera.R;

/**
 * Created by emiliogonzalez on 5/4/18.
 */

public class TicketingActivity extends AppCompatActivity {

    TextView tvStatus;
    TextView tvHeader;
    TextView tvDescription;
    TextView tvNextSteps;
    ImageView ivExample;
    TextView tvTakePicture;
    Button btSelectPicture;
    Button btTakePicture;
    TextView btUploadPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketing);

        tvStatus = (TextView) findViewById(R.id.status_text);
        tvHeader = (TextView) findViewById(R.id.header_text);
        tvDescription = (TextView) findViewById(R.id.description_text);
        tvNextSteps = (TextView) findViewById(R.id.next_steps_text);
        ivExample = (ImageView) findViewById(R.id.example_image);
        tvTakePicture = (TextView) findViewById(R.id.take_picture_text);
        btSelectPicture = (Button) findViewById(R.id.select_picture_button);
        btTakePicture = (Button) findViewById(R.id.take_picture_button);
        btUploadPicture = (TextView) findViewById(R.id.upload_picture_button);

        tvStatus.setText(R.string.ticketing_status_text);
        tvHeader.setText(R.string.ticketing_header_text);
        tvDescription.setText(R.string.ticketing_description_text);
        tvNextSteps.setText(R.string.ticketing_next_steps_text);
        tvTakePicture.setText(R.string.ticketing_take_picture_text);
        btTakePicture.setText(R.string.ticketing_take_picture_button);
        btUploadPicture.setText(R.string.ticketing_upload_picture_button);
        ivExample.setImageResource(R.drawable.ticketing_example);

    }
}
