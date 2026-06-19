package com.encs5150.students1220216_1220071.travelplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.encs5150.students1220216_1220071.travelplanner.R;
import com.encs5150.students1220216_1220071.travelplanner.fragments.admin.AdminAddAdminFragment;
import com.encs5150.students1220216_1220071.travelplanner.fragments.admin.AdminAddTripFragment;
import com.encs5150.students1220216_1220071.travelplanner.fragments.admin.AdminDeleteTripsFragment;
import com.encs5150.students1220216_1220071.travelplanner.fragments.admin.AdminEditTripsFragment;
import com.encs5150.students1220216_1220071.travelplanner.fragments.admin.AdminHomeFragment;
import com.encs5150.students1220216_1220071.travelplanner.fragments.admin.AdminTripFormFragment;
import com.encs5150.students1220216_1220071.travelplanner.fragments.admin.AdminTripsFragment;
import com.encs5150.students1220216_1220071.travelplanner.fragments.admin.AdminViewReservationsFragment;
import com.encs5150.students1220216_1220071.travelplanner.fragments.admin.AdminViewUsersFragment;
import com.encs5150.students1220216_1220071.travelplanner.utils.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class AdminHomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // block non admins
        if (!SessionManager.isAdmin(this)) {
            SessionManager.clearSession(this);
            Toast.makeText(this, R.string.admin_access_denied_toast, Toast.LENGTH_LONG).show();
            routeToLogin();
            return;
        }

        setContentView(R.layout.activity_admin_home);

        drawerLayout = findViewById(R.id.admin_drawer_layout);
        navigationView = findViewById(R.id.admin_navigation_view);
        toolbar = findViewById(R.id.admin_toolbar);

        setSupportActionBar(toolbar);
        toolbar.setTitle("Admin Panel");

        // setup drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                                                R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // set admin email in drawer header
        TextView headerSubtitle = navigationView.getHeaderView(0).findViewById(R.id.admin_drawer_header_subtitle);
        headerSubtitle.setText(SessionManager.getCurrentUserEmail(this));

        // load admin home fragment by default
        if (savedInstanceState == null) {
            showFragment(new AdminHomeFragment());
            navigationView.setCheckedItem(R.id.admin_nav_home);
        }

        // handle drawer item clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.admin_nav_logout) {
                    SessionManager.clearSession(AdminHomeActivity.this);
                    Toast.makeText(AdminHomeActivity.this, "Logged out.", Toast.LENGTH_SHORT).show();
                    routeToLogin();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }

                Fragment fragment = createFragmentForItem(itemId);
                if (fragment != null) {
                    showFragment(fragment);
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private Fragment createFragmentForItem(int itemId) {
        if (itemId == R.id.admin_nav_home) {
            return new AdminHomeFragment();
        } else if (itemId == R.id.admin_nav_add_admin) {
            return new AdminAddAdminFragment();
        } else if (itemId == R.id.admin_nav_view_users) {
            return new AdminViewUsersFragment();
        } else if (itemId == R.id.admin_nav_add_trip) {
            return AdminTripFormFragment.newInstance(); // add mode
        } else if (itemId == R.id.admin_nav_edit_trips) {
            return new AdminTripsFragment(); // shows list with edit buttons
        } else if (itemId == R.id.admin_nav_delete_trips) {
            return new AdminTripsFragment(); // same list, delete buttons active in B20
        } else if (itemId == R.id.admin_nav_view_reservations) {
            return new AdminViewReservationsFragment();
        }
        return null;
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.admin_fragment_container, fragment)
                .commit();
    }

    private void routeToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}