package com.encs5150.students1220216_1220071.travelplanner;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.encs5150.students1220216_1220071.travelplanner.activities.IntroductionActivity;
import com.encs5150.students1220216_1220071.travelplanner.activities.AdminHomeActivity;
import com.encs5150.students1220216_1220071.travelplanner.activities.LoginActivity;
import com.encs5150.students1220216_1220071.travelplanner.activities.RegistrationActivity;
import com.encs5150.students1220216_1220071.travelplanner.activities.SplashActivity;
import com.encs5150.students1220216_1220071.travelplanner.database.DatabaseHelper;
import com.encs5150.students1220216_1220071.travelplanner.fragments.ContactUsFragment;
import com.encs5150.students1220216_1220071.travelplanner.fragments.FavoritesFragment;
import com.encs5150.students1220216_1220071.travelplanner.fragments.HomeFragment;
import com.encs5150.students1220216_1220071.travelplanner.fragments.MyReservationsFragment;
import com.encs5150.students1220216_1220071.travelplanner.fragments.ProfileManagementFragment;
import com.encs5150.students1220216_1220071.travelplanner.fragments.ReservationFormFragment;
import com.encs5150.students1220216_1220071.travelplanner.fragments.SpecialSectionFragment;
import com.encs5150.students1220216_1220071.travelplanner.fragments.TripDetailsFragment;
import com.encs5150.students1220216_1220071.travelplanner.fragments.TripsFragment;
import com.encs5150.students1220216_1220071.travelplanner.fragments.admin.AdminAddAdminFragment;
import com.encs5150.students1220216_1220071.travelplanner.models.Reservation;
import com.encs5150.students1220216_1220071.travelplanner.models.Trip;
import com.encs5150.students1220216_1220071.travelplanner.models.User;
import com.encs5150.students1220216_1220071.travelplanner.network.TripJsonParser;
import com.encs5150.students1220216_1220071.travelplanner.repositories.FavoriteRepository;
import com.encs5150.students1220216_1220071.travelplanner.repositories.ReservationRepository;
import com.encs5150.students1220216_1220071.travelplanner.repositories.TripRepository;
import com.encs5150.students1220216_1220071.travelplanner.repositories.UserRepository;
import com.encs5150.students1220216_1220071.travelplanner.utils.SessionManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class UserJourneyInstrumentedTest {
    private static final String DATABASE_NAME = "travel_planner.db";
    private static final String AUTH_PREFS_NAME = "travel_planner_auth";

    private Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        resetAppState();
    }

    @After
    public void tearDown() {
        resetAppState();
    }

    @Test
    public void splashStartsLogoAnimation() {
        try (ActivityScenario<SplashActivity> scenario = ActivityScenario.launch(SplashActivity.class)) {
            scenario.onActivity(activity -> {
                View logo = activity.findViewById(R.id.splash_logo);
                assertNotNull(logo.getAnimation());
                assertTrue(logo.getAnimation().hasStarted());
            });
        }
    }

    @Test
    public void introductionHandlesNetworkAndEmptyApiFailuresAndAllowsRetry() {
        try (ActivityScenario<IntroductionActivity> scenario =
                     ActivityScenario.launch(IntroductionActivity.class)) {
            scenario.onActivity(activity -> {
                activity.onTripsFetched(Collections.emptyList());
                TextView status = activity.findViewById(R.id.connect_status);
                assertEquals(
                        activity.getString(R.string.connect_invalid_data_status),
                        status.getText().toString()
                );
                assertTrue(activity.findViewById(R.id.connect_button).isEnabled());
                assertEquals(View.GONE, activity.findViewById(R.id.connect_progress).getVisibility());

                activity.onTripsFetched(null);
                assertEquals(
                        activity.getString(R.string.connect_failure_status),
                        status.getText().toString()
                );
                assertTrue(activity.findViewById(R.id.connect_button).isEnabled());
            });
        }
    }

    @Test
    public void introductionImportsTenUsableTripsAndOpensLogin() {
        List<Trip> trips = new ArrayList<>();
        for (int index = 1; index <= 10; index++) {
            trips.add(createTrip(index, "Destination " + index));
        }

        try (ActivityScenario<IntroductionActivity> scenario =
                     ActivityScenario.launch(IntroductionActivity.class)) {
            scenario.onActivity(activity -> activity.onTripsFetched(trips));
            onView(withId(R.id.login_title)).check(matches(isDisplayed()));
        }

        assertEquals(10, new TripRepository(context).getAllTrips().size());
    }

    @Test
    public void parserSkipsMalformedTripWithoutDiscardingLaterValidTrips() {
        String json = "["
                + validTripJson(1, "Jerusalem") + ","
                + "{\"id\":2,\"destination\":\"Broken\",\"country\":\"PS\","
                + "\"duration_days\":0,\"price\":100,\"rating\":9,"
                + "\"description\":\"Invalid values\",\"image\":\"https://example.com/2.jpg\"},"
                + validTripJson(3, "Bethlehem")
                + "]";

        List<Trip> trips = TripJsonParser.getTripsFromJson(json);
        assertEquals(2, trips.size());
        assertEquals("Jerusalem", trips.get(0).getDestination());
        assertEquals("Bethlehem", trips.get(1).getDestination());
        assertTrue(TripJsonParser.getTripsFromJson("").isEmpty());
        assertTrue(TripJsonParser.getTripsFromJson("not-json").isEmpty());
    }

    @Test
    public void repositoriesSupportNormalJourneyAndExpectedFailures() {
        UserRepository userRepository = new UserRepository(context);
        User registeredUser = createUser();
        // Even a manipulated public-registration model cannot escalate its role.
        registeredUser.setRole(SessionManager.ROLE_ADMIN);
        assertTrue(userRepository.registerUser(registeredUser));
        assertFalse(userRepository.registerUser(registeredUser));
        assertNull(userRepository.loginUser(registeredUser.getEmail(), "Wrong123"));

        User storedUser = userRepository.loginUser(registeredUser.getEmail(), registeredUser.getPassword());
        assertNotNull(storedUser);
        assertEquals(SessionManager.ROLE_USER, storedUser.getRole());
        SessionManager.saveSession(context, storedUser);
        assertTrue(SessionManager.hasActiveSession(context));
        assertTrue(SessionManager.isUser(context));

        User newAdmin = createUser();
        newAdmin.setEmail("new-admin@example.com");
        // registerAdmin owns the role decision instead of trusting the model.
        newAdmin.setRole(SessionManager.ROLE_USER);
        assertTrue(userRepository.registerAdmin(newAdmin));
        User storedAdmin = userRepository.loginUser(newAdmin.getEmail(), newAdmin.getPassword());
        assertNotNull(storedAdmin);
        assertEquals(SessionManager.ROLE_ADMIN, storedAdmin.getRole());
        assertEquals(1, userRepository.countAdmins());

        TripRepository tripRepository = new TripRepository(context);
        assertTrue(tripRepository.insertTrip(createTrip(21, "Traveler's Haven")));
        Trip storedTrip = tripRepository.getAllTrips().get(0);
        assertEquals(1, tripRepository.searchWithFilter(
                "Traveler's",
                null,
                0,
                0,
                0
        ).size());

        FavoriteRepository favoriteRepository = new FavoriteRepository(context);
        assertTrue(favoriteRepository.addFavorite(storedUser.getId(), storedTrip.getID()));
        assertFalse(favoriteRepository.addFavorite(storedUser.getId(), storedTrip.getID()));
        assertEquals(1, favoriteRepository.getFavoriteTripsByUser(storedUser.getId()).size());

        Reservation reservation = new Reservation();
        reservation.setUserId(storedUser.getId());
        reservation.setTripId(storedTrip.getID());
        reservation.setQuantity(2);
        reservation.setReservationType("Couple");
        reservation.setReservationDate("2026-06-20");
        reservation.setStatus("Confirmed");
        reservation.setAdditionalInfo("Window seats");
        ReservationRepository reservationRepository = new ReservationRepository(context);
        assertTrue(reservationRepository.createReservation(reservation));
        assertEquals(1, reservationRepository.getReservationsByUser(storedUser.getId()).size());

        assertTrue(userRepository.updateFirstName(storedUser.getId(), "Updated"));
        assertTrue(userRepository.updatePassword(storedUser.getId(), "Updated123"));
        assertNull(userRepository.loginUser(registeredUser.getEmail(), registeredUser.getPassword()));
        assertNotNull(userRepository.loginUser(registeredUser.getEmail(), "Updated123"));

        SessionManager.clearSession(context);
        assertFalse(SessionManager.hasActiveSession(context));
    }

    @Test
    public void mainActivityRejectsMissingSession() {
        SessionManager.clearSession(context);
        try (ActivityScenario<MainActivity> ignored = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.login_title)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void addAdminScreenPersistsAdminRoleAndAdminLogin() {
        UserRepository userRepository = new UserRepository(context);

        // Reproduce the reported sequence: create as user, delete, then reuse as admin.
        User deletedUser = createUser();
        deletedUser.setEmail("admin2@admin.com");
        deletedUser.setPassword("OldUser123");
        assertTrue(userRepository.registerUser(deletedUser));
        User storedDeletedUser = userRepository.findUserByEmail(deletedUser.getEmail());
        assertNotNull(storedDeletedUser);
        int deletedUserId = storedDeletedUser.getId();
        assertTrue(userRepository.deleteUser(deletedUserId));
        assertFalse(userRepository.emailExists(deletedUser.getEmail()));

        User signedInAdmin = createUser();
        signedInAdmin.setEmail("signed-in-admin@example.com");
        assertTrue(userRepository.registerAdmin(signedInAdmin));
        User storedSignedInAdmin = userRepository.findUserByEmail(signedInAdmin.getEmail());
        assertNotNull(storedSignedInAdmin);
        SessionManager.saveSession(context, storedSignedInAdmin);

        try (ActivityScenario<AdminHomeActivity> scenario =
                     ActivityScenario.launch(AdminHomeActivity.class)) {
            scenario.onActivity(activity -> {
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.admin_fragment_container, new AdminAddAdminFragment())
                        .commitNow();

                setText(activity, R.id.admin_email_input, "admin2@admin.com");
                setText(activity, R.id.admin_first_name_input, "Created");
                setText(activity, R.id.admin_last_name_input, "Admin");
                setText(activity, R.id.admin_phone_input, "+970599777777");
                setText(activity, R.id.admin_password_input, "Created123");
                setText(activity, R.id.admin_confirm_password_input, "Created123");
                activity.findViewById(R.id.admin_add_button).performClick();

                User createdAdmin = new UserRepository(activity)
                        .findUserByEmail("admin2@admin.com");
                assertNotNull(createdAdmin);
                assertEquals(deletedUserId, createdAdmin.getId());
                assertEquals(SessionManager.ROLE_ADMIN, createdAdmin.getRole());
                assertNull(new UserRepository(activity)
                        .loginUser("admin2@admin.com", "OldUser123"));

                User authenticatedAdmin = new UserRepository(activity)
                        .loginUser("admin2@admin.com", "Created123");
                assertNotNull(authenticatedAdmin);
                assertEquals(SessionManager.ROLE_ADMIN, authenticatedAdmin.getRole());
            });
        }
    }

    @Test
    public void registrationAndLoginUiHandleDuplicateAndWrongPassword() {
        try (ActivityScenario<RegistrationActivity> scenario =
                     ActivityScenario.launch(RegistrationActivity.class)) {
            scenario.onActivity(activity -> {
                setText(activity, R.id.registration_email_input, "traveler@example.com");
                setText(activity, R.id.first_name_input, "Travel");
                setText(activity, R.id.last_name_input, "Tester");
                setText(activity, R.id.registration_password_input, "Travel123");
                setText(activity, R.id.confirm_password_input, "Travel123");
                setText(activity, R.id.phone_input, "+970599123456");
                ((Spinner) activity.findViewById(R.id.gender_spinner)).setSelection(1);
                ((Spinner) activity.findViewById(R.id.trip_category_spinner)).setSelection(1);
                activity.findViewById(R.id.register_button).performClick();
            });
        }
        assertNotNull(new UserRepository(context).findUserByEmail("traveler@example.com"));

        Intent loginIntent = new Intent(context, LoginActivity.class);
        loginIntent.putExtra(LoginActivity.EXTRA_PREFILL_EMAIL, "traveler@example.com");
        try (ActivityScenario<LoginActivity> loginScenario = ActivityScenario.launch(loginIntent)) {
            loginScenario.onActivity(activity -> {
                setText(activity, R.id.login_password_input, "Wrong123");
                activity.findViewById(R.id.login_button).performClick();
                assertEquals(
                        activity.getString(R.string.login_invalid_credentials_status),
                        ((TextView) activity.findViewById(R.id.login_status)).getText().toString()
                );

                setText(activity, R.id.login_password_input, "Travel123");
                activity.findViewById(R.id.login_button).performClick();
            });
            onView(withId(R.id.main_toolbar)).check(matches(isDisplayed()));
        }

        SessionManager.clearSession(context);
        try (ActivityScenario<RegistrationActivity> duplicateScenario =
                     ActivityScenario.launch(RegistrationActivity.class)) {
            duplicateScenario.onActivity(activity -> {
                setText(activity, R.id.registration_email_input, "traveler@example.com");
                setText(activity, R.id.first_name_input, "Travel");
                setText(activity, R.id.last_name_input, "Tester");
                setText(activity, R.id.registration_password_input, "Travel123");
                setText(activity, R.id.confirm_password_input, "Travel123");
                setText(activity, R.id.phone_input, "+970599123456");
                ((Spinner) activity.findViewById(R.id.gender_spinner)).setSelection(1);
                ((Spinner) activity.findViewById(R.id.trip_category_spinner)).setSelection(1);
                activity.findViewById(R.id.register_button).performClick();
            });
            duplicateScenario.onActivity(activity -> assertEquals(
                    activity.getString(R.string.registration_duplicate_email_error),
                    ((com.google.android.material.textfield.TextInputLayout) activity.findViewById(
                            R.id.registration_email_input_layout
                    )).getError()
            ));
        }
    }

    @Test
    public void userShellReachesEveryDestinationAndLogsOut() {
        UserRepository userRepository = new UserRepository(context);
        assertTrue(userRepository.registerUser(createUser()));
        User user = userRepository.findUserByEmail("traveler@example.com");
        assertNotNull(user);
        SessionManager.saveSession(context, user);

        TripRepository tripRepository = new TripRepository(context);
        assertTrue(tripRepository.insertTrip(createTrip(31, "Nablus")));
        Trip trip = tripRepository.getAllTrips().get(0);

        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            assertDestination(scenario, R.id.nav_home, HomeFragment.class);
            assertDestination(scenario, R.id.nav_trips, TripsFragment.class);

            scenario.onActivity(activity -> {
                activity.openTripDetails(Integer.MAX_VALUE, TripDetailsFragment.SOURCE_TRIPS);
                activity.getSupportFragmentManager().executePendingTransactions();
                assertCurrentFragment(activity, TripDetailsFragment.class);
                assertEquals(View.VISIBLE, activity.findViewById(R.id.details_error).getVisibility());
                activity.navigateBack();
                activity.getSupportFragmentManager().executePendingTransactions();
                assertCurrentFragment(activity, TripsFragment.class);

                activity.openTripDetails(trip.getID(), TripDetailsFragment.SOURCE_TRIPS);
                activity.getSupportFragmentManager().executePendingTransactions();
                assertCurrentFragment(activity, TripDetailsFragment.class);

                activity.openReservationForm(trip.getID(), trip.getDestination());
                activity.getSupportFragmentManager().executePendingTransactions();
                assertCurrentFragment(activity, ReservationFormFragment.class);

                activity.navigateBack();
                activity.getSupportFragmentManager().executePendingTransactions();
                assertCurrentFragment(activity, TripDetailsFragment.class);

                activity.findViewById(R.id.details_favorite_button).performClick();
                assertTrue(new FavoriteRepository(activity).isFavorite(user.getId(), trip.getID()));

                activity.findViewById(R.id.details_reserve_button).performClick();
                activity.getSupportFragmentManager().executePendingTransactions();
                assertCurrentFragment(activity, ReservationFormFragment.class);

                setText(activity, R.id.reservation_quantity, "999999999999999999999");
                ((Spinner) activity.findViewById(R.id.reservation_type_spinner)).setSelection(1);
                activity.findViewById(R.id.reservation_confirm_button).performClick();
                assertNotNull(((EditText) activity.findViewById(R.id.reservation_quantity)).getError());

                setText(activity, R.id.reservation_quantity, "2");
                ((Spinner) activity.findViewById(R.id.reservation_type_spinner)).setSelection(1);
                activity.findViewById(R.id.reservation_confirm_button).performClick();
                activity.getSupportFragmentManager().executePendingTransactions();
                assertCurrentFragment(activity, MyReservationsFragment.class);
                assertEquals(
                        1,
                        new ReservationRepository(activity)
                                .getReservationsByUser(user.getId())
                                .size()
                );
            });

            assertDestination(scenario, R.id.nav_my_reservations, MyReservationsFragment.class);
            assertDestination(scenario, R.id.nav_favorites, FavoritesFragment.class);
            assertDestination(scenario, R.id.nav_special_section, SpecialSectionFragment.class);
            assertDestination(scenario, R.id.nav_profile_management, ProfileManagementFragment.class);
            scenario.onActivity(activity -> {
                setText(activity, R.id.profile_first_name_input, "Updated");
                setText(activity, R.id.profile_last_name_input, "Traveler");
                setText(activity, R.id.profile_phone_input, "+970599654321");
                activity.findViewById(R.id.save_profile_button).performClick();
                User updated = new UserRepository(activity).findUserByEmail(user.getEmail());
                assertNotNull(updated);
                assertEquals("Updated", updated.getFirstName());
                assertEquals("+970599654321", updated.getPhone());
            });
            assertDestination(scenario, R.id.nav_contact_us, ContactUsFragment.class);
            verifyContactIntents();

            onView(withContentDescription(R.string.drawer_open)).perform(click());
            onView(withText(R.string.nav_logout)).perform(click());
            onView(withId(R.id.login_title)).check(matches(isDisplayed()));
            assertFalse(SessionManager.hasActiveSession(context));
        }
    }

    private void assertDestination(
            ActivityScenario<MainActivity> scenario,
            int menuItemId,
            Class<? extends Fragment> expectedClass
    ) {
        scenario.onActivity(activity -> {
            assertTrue(activity.navigateToDrawerDestination(menuItemId));
            activity.getSupportFragmentManager().executePendingTransactions();
            assertCurrentFragment(activity, expectedClass);
        });
    }

    private void assertCurrentFragment(
            MainActivity activity,
            Class<? extends Fragment> expectedClass
    ) {
        Fragment fragment = activity.getSupportFragmentManager()
                .findFragmentById(R.id.main_fragment_container);
        assertNotNull(fragment);
        assertEquals(expectedClass, fragment.getClass());
    }

    private void verifyContactIntents() {
        androidx.test.espresso.intent.Intents.init();
        try {
            Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(
                    Activity.RESULT_OK,
                    null
            );
            intending(hasAction(Intent.ACTION_DIAL)).respondWith(result);
            intending(hasAction(Intent.ACTION_VIEW)).respondWith(result);
            intending(hasAction(Intent.ACTION_SENDTO)).respondWith(result);

            onView(withId(R.id.call_us_button)).perform(click());
            intended(hasAction(Intent.ACTION_DIAL));
            onView(withId(R.id.locate_us_button)).perform(click());
            intended(hasAction(Intent.ACTION_VIEW));
            onView(withId(R.id.email_us_button)).perform(click());
            intended(hasAction(Intent.ACTION_SENDTO));
        } finally {
            androidx.test.espresso.intent.Intents.release();
        }
    }

    private User createUser() {
        User user = new User();
        user.setEmail("traveler@example.com");
        user.setFirstName("Travel");
        user.setLastName("Tester");
        user.setPassword("Travel123");
        user.setGender("Female");
        user.setCategory("Cultural tours");
        user.setPhone("+970599123456");
        user.setProfilePicturePath("");
        user.setRole(SessionManager.ROLE_USER);
        user.setIsActive(1);
        return user;
    }

    private Trip createTrip(int apiId, String destination) {
        Trip trip = new Trip();
        trip.setApiID(apiId);
        trip.setDestination(destination);
        trip.setCountry("Palestine");
        trip.setDurationDays(3);
        trip.setPrice(350);
        trip.setRating(4.8);
        trip.setDescription("A QA test trip.");
        trip.setImageUrl("https://example.com/trip.jpg");
        trip.setIsActive(1);
        return trip;
    }

    private String validTripJson(int id, String destination) {
        return "{\"id\":" + id
                + ",\"destination\":\"" + destination
                + "\",\"country\":\"Palestine\",\"duration_days\":3"
                + ",\"price\":350,\"rating\":4.8,\"description\":\"Valid trip\""
                + ",\"image\":\"https://example.com/" + id + ".jpg\"}";
    }

    private void setText(android.app.Activity activity, int viewId, String value) {
        ((EditText) activity.findViewById(viewId)).setText(value);
    }

    private void resetAppState() {
        SessionManager.clearSession(context);
        context.getSharedPreferences(AUTH_PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .commit();

        DatabaseHelper helper = new DatabaseHelper(context, DATABASE_NAME, null, 1);
        SQLiteDatabase database = helper.getWritableDatabase();
        database.delete("favorites", null, null);
        database.delete("reservations", null, null);
        database.delete("trips", null, null);
        database.delete("users", null, null);
        helper.close();
    }
}
