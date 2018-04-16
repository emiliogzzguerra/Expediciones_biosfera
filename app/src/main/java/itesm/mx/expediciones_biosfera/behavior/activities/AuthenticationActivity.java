package itesm.mx.expediciones_biosfera.behavior.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import itesm.mx.expediciones_biosfera.R;

public class AuthenticationActivity extends AppCompatActivity {
    private ImageView ivLogo;

    public void setImage() {
        ivLogo = findViewById(R.id.logo_image);
        ivLogo.setImageResource(R.drawable.logo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        setImage();
    }
}
