package com.encs5150.students1220216_1220071.travelplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.encs5150.students1220216_1220071.travelplanner.activities.AdminHomeActivity;
import com.encs5150.students1220216_1220071.travelplanner.activities.LoginActivity;
import com.encs5150.students1220216_1220071.travelplanner.fragments.ContactUsFragment;
import com.encs5150.students1220216_1220071.travelplanner.fragments.FavoritesFragment;
import com.encs5150.students1220216_1220071.travelplanner.fragments.HomeFragment;
import com.encs5150.students1220216_1220071.travelplanner.fragments.MyReservationsFragment;
import com.encs5150.students1220216_1220071.travelplanner.fragments.ProfileManagementFragment;
import com.encs5150.students1220216_1220071.travelplanner.fragments.SpecialSectionFragment;
import com.encs5150.students1220216_1220071.travelplanner.fragments.TripsFragment;
import com.encs5150.students1220216_1220071.travelplanner.models.User;
import com.encs5150.students1220216_1220071.travelplanner.repositories.UserRepository;
import com.encs5150.students1220216_1220071.travelplanner.utils.SessionManager;
import com.encs5150.students1220216_1220071.travelplanner.utils.ValidationUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_SELECTED_DRAWER_ITEM = "selected_drawer_item";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar toolbar;
    private int selectedDrawerItemId = R.id.nav_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!SessionManager.hasActiveSession(this)) {
            routeToLogin();
            return;
        }

        if (SessionManager.isAdmin(this)) {
            routeToAdminHome();
            return;
        }

        if (!SessionManager.isUser(this)) {
            SessionManager.clearSession(this);
            routeToLogin();
            return;
        }

        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.main_toolbar);

        setupDrawer();
        setupBackBehavior();

        if (savedInstanceState == null) {
            navigateToDrawerDestination(R.id.nav_home);
        } else {
            selectedDrawerItemId = savedInstanceState.getInt(KEY_SELECTED_DRAWER_ITEM, R.id.nav_home);
            navigationView.setCheckedItem(selectedDrawerItemId);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SELECTED_DRAWER_ITEM, selectedDrawerItemId);
    }

    private void setupDrawer() {
        String displayName = resolveCurrentUserDisplayName();

        toolbar.setTitle(R.string.travel_planner_title);
        toolbar.setSubtitle(displayName);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View headerView = navigationView.getHeaderView(0);
        TextView headerTitle = headerView.findViewById(R.id.drawer_header_title);
        TextView headerSubtitle = headerView.findViewById(R.id.drawer_header_subtitle);
        headerTitle.setText(R.string.app_name);
        headerSubtitle.setText(displayName);

        navigationView.setNavigationItemSelectedListener(item -> handleDrawerItemSelected(item.getItemId()));
    }

    private void setupBackBehavior() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return;
                }

                if (selectedDrawerItemId != R.id.nav_home) {
                    navigateToDrawerDestination(R.id.nav_home);
                    return;
                }

                setEnabled(false);
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }

    private boolean handleDrawerItemSelected(int itemId) {
        if (itemId == R.id.nav_logout) {
            drawerLayout.closeDrawer(GravityCompat.START);
            logout();
            return false;
        }

        if (itemId == selectedDrawerItemId) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

        boolean didNavigate = navigateToDrawerDestination(itemId);
        drawerLayout.closeDrawer(GravityCompat.START);
        return didNavigate;
    }

    private Fragment createFragmentForItem(int itemId) {
        if (itemId == R.id.nav_home) {
            return new HomeFragment();
        } else if (itemId == R.id.nav_trips) {
            return new TripsFragment();
        } else if (itemId == R.id.nav_my_reservations) {
            return new MyReservationsFragment();
        } else if (itemId == R.id.nav_favorites) {
            return new FavoritesFragment();
        } else if (itemId == R.id.nav_special_section) {
            return new SpecialSectionFragment();
        } else if (itemId == R.id.nav_profile_management) {
            return new ProfileManagementFragment();
        } else if (itemId == R.id.nav_contact_us) {
            return new ContactUsFragment();
        }
        return null;
    }

    private boolean navigateToDrawerDestination(int itemId) {
        Fragment fragment = createFragmentForItem(itemId);
        if (fragment == null) {
            Toast.makeText(this, R.string.navigation_unavailable_toast, Toast.LENGTH_SHORT).show();
            navigationView.setCheckedItem(selectedDrawerItemId);
            return false;
        }

        try {
            showDrawerDestination(itemId, fragment);
            return true;
        } catch (RuntimeException exception) {
            Toast.makeText(this, R.string.navigation_error_toast, Toast.LENGTH_SHORT).show();
            navigationView.setCheckedItem(selectedDrawerItemId);
            return false;
        }
    }

    private void showDrawerDestination(int itemId, Fragment fragment) {
        selectedDrawerItemId = itemId;
        navigationView.setCheckedItem(itemId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragment)
                .commit();
    }

    private void logout() {
        SessionManager.clearSession(this);
        Toast.makeText(this, R.string.logout_success_toast, Toast.LENGTH_SHORT).show();
        routeToLogin();
    }

    private String resolveCurrentUserDisplayName() {
        String email = SessionManager.getCurrentUserEmail(this);
        String fallback = ValidationUtils.isBlank(email)
                ? getString(R.string.drawer_guest_user)
                : email;

        try {
            User user = new UserRepository(this).findUserByEmail(email);
            if (user == null) {
                return fallback;
            }

            String fullName = buildFullName(user.getFirstName(), user.getLastName());
            if (!ValidationUtils.isBlank(fullName)) {
                return fullName;
            }
        } catch (RuntimeException ignored) {
            return fallback;
        }

        return fallback;
    }

    private String buildFullName(String firstName, String lastName) {
        String cleanFirstName = ValidationUtils.cleanInput(firstName);
        String cleanLastName = ValidationUtils.cleanInput(lastName);
        return (cleanFirstName + " " + cleanLastName).trim();
    }

    private void routeToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void routeToAdminHome() {
        Intent intent = new Intent(this, AdminHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
