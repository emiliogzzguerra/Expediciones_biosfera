package itesm.mx.expediciones_biosfera.behavior.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import itesm.mx.expediciones_biosfera.R;
import itesm.mx.expediciones_biosfera.entities.models.Customer;

public class AuthenticationActivity extends AppCompatActivity implements View.OnClickListener {
    private GoogleSignInOptions gso;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;
    private SignInButton signInButton;
    private ImageView ivLogo;
    private FirebaseFirestore firestoreDB;

    private int RC_SIGN_IN = 0;

    public void configureGoogleSignIn() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
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

    public void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void redirectToDrawer(FirebaseUser user) {
        Log.i("Sign In: ", "Redirect to Drawer Activity");
        Intent intent = new Intent(this, DrawerActivity.class);
        startActivity(intent);
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("Firebase Auth", account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()) {
                           // Sign in success
                           Log.d("Firebase Auth", "signInWithCredential:success");
                           FirebaseUser user = firebaseAuth.getCurrentUser();
                           insertUserToFirestore(user);
                           String toastMessage = "Bienvenido, " + user.getDisplayName();
                           Toast.makeText(getApplicationContext(), toastMessage , Toast.LENGTH_LONG).show();
                           redirectToDrawer(user);
                       } else {
                           Log.d("Firebase Auth", "signInWithCredential:failure");
                           Toast.makeText(getApplicationContext(), "La autenticación no fue exitosa", Toast.LENGTH_LONG).show();
                       }
                    }
                });
    }

    private void insertUserToFirestore(FirebaseUser user){
        firestoreDB.collection("users").document(user.getUid()).set(
                new Customer(user.getDisplayName(), user.getEmail()));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Result from sign in intent
        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Google SignIn", "Google sign in failed", e);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null) {
            redirectToDrawer(currentUser);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        configureGoogleSignIn();
        setImage();
        setSignInWithGoogleButton();
        firestoreDB = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }
}
