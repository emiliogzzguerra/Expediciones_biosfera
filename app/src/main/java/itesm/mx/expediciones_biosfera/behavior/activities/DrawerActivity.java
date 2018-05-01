package itesm.mx.expediciones_biosfera.behavior.activities;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import itesm.mx.expediciones_biosfera.R;
import itesm.mx.expediciones_biosfera.behavior.fragments.PackagesFragment;
import itesm.mx.expediciones_biosfera.behavior.fragments.ProfileFragment;
import itesm.mx.expediciones_biosfera.database.operations.User;
import itesm.mx.expediciones_biosfera.database.operations.UserOperations;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    UserOperations dao;

    //Regresa true si ya existe un usuario con Firebase ID en la tabla SQLite
    //False de lo contrario
    public boolean userExists(String firebaseId){
        dao = new UserOperations(this);
        dao.open();
        ArrayList<User> users = new ArrayList<>();
        users = dao.getAllUsers();
        for(int i = 0; i < users.size(); i++){
            if(users.get(i).getFbid().equals(firebaseId)){
                return true;
            }
        }
        dao.close();
        return false;
    }

    public void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void setDrawerLayout() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }
        };
        toggle.setDrawerIndicatorEnabled(true);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    public void configureNavigationView() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_packages);

        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.isChecked()) {
                ActionBar supportActionBar = getSupportActionBar();
                if(supportActionBar != null) {
                    supportActionBar.setTitle(item.getTitle());
                }
            }
        }
    }

    public void getFirebaseUser() {
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        setToolbar();
        setDrawerLayout();
        configureNavigationView();
        getFirebaseUser();

        //Si el usuario está registrado, la aplicación te dirige a la pantalla de paquetes.
        //De lo contrario, la aplicación te dirige a la pantalla de perfil.
        if(userExists(currentUser.getUid())){
            PackagesFragment packagesFragment = new PackagesFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.content_frame,
                    packagesFragment).commit();
        }else{
            ProfileFragment profileFragment = new ProfileFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.content_frame,
                    profileFragment).commit();
        }



    }

    public void signOut() {
        firebaseAuth.signOut();
        Toast.makeText(this, "Successfully Signed Out", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, AuthenticationActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggle != null && toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.nav_packages) {
            fragment = new PackagesFragment();
        } else if (id == R.id.nav_profile) {
            fragment = new ProfileFragment();
        } else if (id == R.id.nav_signout) {
            signOut();
        }

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            item.setChecked(true);
            ActionBar supportActionBar = getSupportActionBar();
            if(supportActionBar != null) {
                supportActionBar.setTitle(item.getTitle());
            }
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
