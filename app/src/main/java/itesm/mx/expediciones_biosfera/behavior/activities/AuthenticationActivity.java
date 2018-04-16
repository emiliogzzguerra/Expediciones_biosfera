package itesm.mx.expediciones_biosfera.behavior.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;

import itesm.mx.expediciones_biosfera.R;

public class AuthenticationActivity extends AppCompatActivity implements View.OnClickListener {
    private GoogleSignInOptions gso;
    private GoogleSignInClient googleSignInClient;
    private SignInButton signInButton;
    private ImageView ivLogo;

    public void configureGoogleSignIn() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void setImage() {
        ivLogo = findViewById(R.id.logo_image);
        ivLogo.setImageResource(R.drawable.logo);
    }

    public void setSignInWithGoogleButton() {
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);

        //Set text label
        TextView tvSignInTag = (TextView) signInButton.getChildAt(0);
        tvSignInTag.setText(R.string.sign_in_google);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        configureGoogleSignIn();
        setImage();
        setSignInWithGoogleButton();
    }
}
