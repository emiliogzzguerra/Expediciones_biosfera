package itesm.mx.expediciones_biosfera.behavior.activities;

import android.content.Intent;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import itesm.mx.expediciones_biosfera.R;
import itesm.mx.expediciones_biosfera.behavior.fragments.ReservationsListFragment;

public class AdminDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private boolean doubleBackToExitPressedOnce = false;


    public void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void setDrawerLayout() {
        drawer = findViewById(R.id.drawer_layout);
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
        navigationView.setCheckedItem(R.id.nav_reservations);
        View headerView = navigationView.getHeaderView(0);
        TextView tvName = headerView.findViewById(R.id.text_user_name);
        TextView tvMail = headerView.findViewById(R.id.text_email);
        tvName.setText(currentUser.getDisplayName());
        tvMail.setText(currentUser.getEmail());
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
        setContentView(R.layout.activity_admin_drawer);
        getFirebaseUser();

        setToolbar();
        setDrawerLayout();
        configureNavigationView();
        ReservationsListFragment adminReservationsListFragment = new ReservationsListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content_frame, adminReservationsListFragment).commit();
    }

    public void signOut() {
        firebaseAuth.signOut();
        Toast.makeText(this, getResources().getString(R.string.sign_out_success), Toast.LENGTH_LONG).show();
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

        if (id == R.id.nav_reservations) {
            fragment = new ReservationsListFragment();
        } else if(id == R.id.nav_signout) {
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
            if (doubleBackToExitPressedOnce) {
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                MenuItem item = navigationView.getMenu().getItem(1);
                onNavigationItemSelected(item);
                return;
            }

            doubleBackToExitPressedOnce = true;

            Toast.makeText(this, R.string.double_click_back, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        }
    }
}
