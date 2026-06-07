# Student B Plan - Data Layer, API Import, Trip Features, Reservations, Favorites, Admin

This plan divides the Android course project into Git-friendly commits for Student B. It is now customized for student IDs `1220216` and `1220071`, which means the project is a Travel Planner App using the Left Theme. It assumes Student B owns the local database, REST API integration, trip feature set, reservations, favorites, special section, admin tools, and technical documentation for the data/API side.

Academic integrity note: use this document as a project-management checklist. The assignment says students must not submit AI-generated implementation, so both students should write the code themselves and be ready to explain every activity, fragment, database table, API call, validation rule, and Git commit during discussion.

## Project Facts From The Requirement

- The app must be written in Java for Android.
- The app must import at least 10 trips from a RESTful API.
- API data must be stored in the local database after successful Connect.
- Local database management is required.
- RecyclerView is required for displaying trips.
- Fragments are required for details and feature sections.
- Users must be able to search, filter, favorite, reserve trips, view reservations, view favorites, and open a special section.
- Users should reserve trips rather than use "join" wording, because the selected app type is Travel Planner App.
- Admin must have a separate drawer and can add admins, view/delete users, add/edit/delete trips, view reservations, and logout.
- Admin account must exist before login:
  - email: `admin@admin.com`
  - password: `Admin123!`
- Passwords must be stored securely/encrypted, not as plain text.
- The app must include exception handling and input validation.

## Student B Ownership

Student B owns:

- Shared data models/contracts.
- SQLite database schema, helper, seed data, and repository classes.
- Secure password storage approach used by registration/login/admin.
- REST API endpoint or source containing at least 10 valid trip objects.
- Network client and JSON parser.
- Import service that saves API trip objects to the database.
- Trips RecyclerView list, adapter, item layout, and data loading.
- Trip details fragment.
- Search and filtering.
- Favorite button behavior and Favorites screen.
- Reservation form and My Reservations screen.
- Special Section logic and screen.
- Admin home/drawer, admin user management, admin trip management, admin reservations viewer.
- Database/API/admin discussion notes.
- Data-heavy QA and integration support.

Student B does not own:

- Initial Android project creation.
- App-wide visual theme resources except when applying them to Student B screens.
- Splash/introduction UI shell.
- Login/registration/profile/contact UI, except repository methods needed by those screens.
- Final user navigation shell, except providing fragments that plug into it.

## Shared Rules Before Any Coding

1. Wait for Student A Commit A01 before adding Android source files, unless you are only editing documentation.
2. Use Travel Planner fields consistently: destination/title, country, duration days, price, rating, description, and image.
3. Use a database schema that supports trips cleanly and does not include unused non-travel capacity or schedule fields unless the team intentionally adds them as optional travel metadata.
4. Keep repository methods stable so Student A can call them without rework.
5. Do not store plain text passwords.
6. Make every feature commit demonstrable with a small manual test.
7. Keep API data realistic and varied because search/filter/special-section quality depends on it.

## High-Level Dependency Map

- Student B Step 1 depends on Student A project skeleton.
- Student A authentication integration depends on Student B database/user repository.
- Student A Connect button depends on Student B API import service.
- Student B user feature fragments depend on Student A navigation host contract.
- Student A final drawer integration depends on Student B user feature fragments.
- Student B admin drawer can be built after Student A login can route admins, or it can start with a temporary entry point and integrate later.
- Final QA depends on both students merging regularly.

## Proposed Commit Sequence

### Step 1 - Data Contracts, Database Schema, And Repositories

#### Commit B01 - `data: define shared models and feature contracts`

Description:

- Define the shared data model plan before writing database-heavy code.
- Create model classes or documented contracts for:
  - user,
  - admin/user role,
  - trip,
  - reservation,
  - favorite,
  - session-related user identity if needed.
- Define the Travel Planner trip fields:
  - destination/title,
  - country,
  - duration days,
  - price,
  - rating,
  - description,
  - image.
- Decide common fields every trip should have:
  - local database id,
  - remote API id,
  - display name,
  - description,
  - image URL/path,
  - created/updated timestamps if useful.
- Define repository method names and expected behavior for Student A:
  - register user,
  - login user,
  - load current user,
  - update profile,
  - verify admin login,
  - load trips,
  - import trips,
  - add/remove favorite,
  - create reservation.
- Keep this commit focused on contracts and model shape. Avoid building UI.

Dependencies:

- Depends on Student A Commit A01 for project/package structure.
- Depends on Student A Commit A02 documenting the Travel Planner App and Left Theme decision.

Acceptance criteria:

- Student A can read the models/contracts and know what methods will be available.
- Model fields cover every assignment-required screen.
- The selected Travel Planner type is reflected clearly.
- No large UI or database implementation is mixed into this commit.

#### Commit B02 - `data: create SQLite schema and seed admin account`

Description:

- Create the SQLite database helper or equivalent local database management layer expected by the course.
- Add tables for:
  - users,
  - trips,
  - favorites,
  - reservations,
  - admins or user roles.
- Include required user fields:
  - email,
  - first name,
  - last name,
  - encrypted/secure password value,
  - gender,
  - trip category/travel preference,
  - phone number,
  - profile picture path/URI if supported,
  - role if using one users table for both users/admins.
- Include required trip fields: destination/title, country, duration days, price, rating, description, and image.
- Include reservation fields:
  - user id,
  - trip id,
  - traveler quantity,
  - reservation type,
  - reservation date,
  - status,
  - additional information.
- Include favorite fields:
  - user id,
  - trip id,
  - created date if useful.
- Add constraints that prevent duplicate users and duplicate favorites for the same user/trip.
- Seed the admin account:
  - email `admin@admin.com`,
  - password `Admin123!` stored securely,
  - admin role.
- Add upgrade handling that does not crash during development if the schema changes.

Dependencies:

- Depends on Commit B01.
- Student A Commit A10 depends on this.

Acceptance criteria:

- Database is created successfully on a fresh app install.
- Admin account exists after database creation.
- Passwords are not visible as plain text in database rows.
- Tables support every required user, trip, favorite, reservation, and admin function.

#### Commit B03 - `data: implement repositories for users, trips, favorites, and reservations`

Description:

- Build repository classes that hide raw database access from UI screens.
- User repository should support:
  - create/register user,
  - find user by email,
  - authenticate user,
  - authenticate admin,
  - update first name,
  - update last name,
  - update password securely,
  - update phone,
  - update profile picture path/URI,
  - list users for admin,
  - delete users safely.
- Trip repository should support:
  - insert imported trip,
  - update existing trip,
  - delete trip,
  - list all trips,
  - get trip by id,
  - search/filter queries,
  - list special trips.
- Favorite repository should support:
  - add favorite,
  - remove favorite,
  - check if trip is favorite for current user,
  - list favorites by user.
- Reservation repository should support:
  - create reservation,
  - list reservations by user,
  - list all reservations for admin,
  - include combined trip/user display data where needed.
- Return useful success/failure results so UI can show meaningful messages.
- Catch database exceptions and avoid crashing UI callers.

Dependencies:

- Depends on Commit B02.
- Student A Commit A10 and A14/A15 depend on these methods.

Acceptance criteria:

- Repository methods compile and can be called from UI without raw SQL.
- Duplicate email/favorite cases are handled.
- User/profile methods support Student A screens.
- Reservation/favorite methods support Student B feature screens.

### Step 2 - REST API Source, Network Client, And Import Flow

#### Commit B04 - `api: create or select REST endpoint with at least 10 trips`

Description:

- Create or select a RESTful API endpoint that returns an array with at least 10 trip objects.
- The endpoint must match the selected Travel Planner App type.
- Each trip object should include id, destination/title, country, duration days, price, rating, description, and image.
- Ensure values are realistic and varied:
  - several countries,
  - different durations, prices, and ratings,
  - varied image URLs,
  - enough variety for filters and special section.
- Keep a copy of the API shape in docs so both students can explain it.
- Verify the endpoint works from a browser or REST client before coding.
- Avoid depending on an unstable endpoint that may disappear before submission.

Dependencies:

- Depends on the Travel Planner decision from Student A Commit A02.
- Student A Commit A06 eventually depends on this endpoint through import logic.

Acceptance criteria:

- API returns valid JSON array.
- Array contains at least 10 usable trip objects.
- Each trip object includes all fields needed for trip list/details.
- API endpoint/source is documented for discussion.

#### Commit B05 - `api: implement network client and JSON parser`

Description:

- Implement network code that fetches the API endpoint.
- Use a course-acceptable approach for Java Android networking, such as `HttpURLConnection` with background execution, or the library approved by the instructor/team.
- Do not run network operations on the main UI thread.
- Parse the JSON response into trip model objects.
- Validate required JSON fields before accepting a trip object.
- Handle failure cases:
  - no internet,
  - timeout,
  - invalid URL,
  - non-200 response,
  - empty response,
  - invalid JSON,
  - trip object missing required fields.
- Return clear success/failure information to the caller rather than crashing.

Dependencies:

- Depends on Commit B04.
- Uses models from Commit B01.

Acceptance criteria:

- Network request runs off the main thread.
- Valid JSON produces a list of trip model objects.
- Invalid responses produce controlled errors.
- No UI navigation is handled here; this commit is data/network only.

#### Commit B06 - `api: store imported API trips in local database`

Description:

- Add an import service or repository method that connects the network client to the local database.
- On successful API fetch:
  - parse all trip objects,
  - insert new trips into the trips table,
  - update existing trips using remote API id or another unique key,
  - avoid duplicate rows if the user presses Connect multiple times.
- On partial trip-object errors:
  - decide whether to reject the whole import or skip invalid trip objects with a logged warning,
  - make that behavior consistent and explainable.
- Return the number of inserted/updated trips to the caller if useful.
- Ensure at least 10 valid trips are stored after successful import.

Dependencies:

- Depends on Commits B02, B03, and B05.
- Student A Commit A06 depends on this.

Acceptance criteria:

- Successful import writes data into the local database.
- Repeating import does not duplicate trips.
- Import result can be used by Student A's Connect button.
- Database remains valid after failed import attempts.

#### Commit B07 - `api: verify connect success and failure scenarios`

Description:

- Manually test the import service with:
  - normal working API,
  - disabled internet or unreachable URL,
  - invalid JSON,
  - empty array,
  - duplicate import.
- Add logs or simple debug output that helps during development but does not expose sensitive data.
- Document expected UI behavior for Student A:
  - success: navigate to login/register after storing trips,
  - failure: Toast and remain on introduction screen.
- Fix any import bugs found during manual testing.

Dependencies:

- Depends on Commit B06.
- Blocks Student A Commit A06 final behavior.

Acceptance criteria:

- Student A can call one clear import entry point.
- Failure and success cases are predictable.
- At least 10 trips appear in the local database after success.

### Step 3 - Trips List, Details, Search, Favorites, Reservation Form

#### Commit B08 - `trips: build RecyclerView list fragment and adapter`

Description:

- Replace the Trips placeholder with a real RecyclerView fragment.
- Load trips from the local database, not directly from the API.
- Display all important trip summary fields: destination/title, country, duration, price, rating, and image.
- Create a reusable trip row/card layout that follows Student A's theme resources.
- Load images from URLs or show a stable fallback image if remote image loading is not implemented.
- Add empty state if no trips exist and a message telling the user to connect/import first if appropriate.
- Add click behavior to open the details fragment.

Dependencies:

- Depends on Student A Commit A11 for fragment host/navigation contract.
- Depends on Commit B03 for trip repository.
- Depends on Commit B06 for imported data.

Acceptance criteria:

- Trips drawer item shows a RecyclerView list.
- Data comes from the local database.
- Trip rows are readable and visually consistent.
- Tapping a trip opens details.

#### Commit B09 - `trips: implement details fragment with full trip information`

Description:

- Create a trip details fragment.
- Show all available trip fields from the API/database.
- Include image or fallback image.
- Include action buttons:
  - Favorite or Remove Favorite depending on current state,
  - Reserve.
- Make sure details are loaded by trip id, not by passing a whole fragile object through navigation.
- Handle missing/deleted trip state gracefully with an error message and back navigation.

Dependencies:

- Depends on Commit B08.
- Uses favorite/reservation repositories from Commit B03.

Acceptance criteria:

- Details screen shows complete trip information.
- Details screen is opened from the RecyclerView list.
- Missing trip does not crash the app.
- Favorite and reservation buttons are visible for later commits.

#### Commit B10 - `trips: add search and filtering to list`

Description:

- Add search functionality to the Trips list.
- Search should match sensible trip fields: destination/title, country, and description.
- Add filtering functionality for travel-specific fields: country, duration, price range, and rating.
- Make search and filter work together instead of replacing each other unexpectedly.
- Add clear empty state for "no results".
- Provide a reset/clear filters action.
- Keep queries efficient enough for the expected data size.

Dependencies:

- Depends on Commit B08.
- Student A's theme resources from Commit A04 should be used for controls.

Acceptance criteria:

- User can search trips.
- User can filter trips.
- Search plus filter behaves predictably.
- Clearing filters restores the full list.

#### Commit B11 - `favorites: implement favorite toggle from list and details`

Description:

- Let the logged-in user add a trip to favorites.
- Let the logged-in user remove a trip from favorites.
- Favorite state should be user-specific, not global.
- Show favorite state consistently in:
  - RecyclerView trip row if shown there,
  - details fragment.
- Prevent duplicate favorite rows for the same user/trip.
- If no user session exists, show an error and route to login if appropriate.
- Update UI immediately after add/remove succeeds.

Dependencies:

- Depends on Commit B09.
- Depends on current user/session data from Student A Commit A10.
- Depends on FavoriteRepository from Commit B03.

Acceptance criteria:

- Favorite button persists the trip for the current user.
- Favorite state survives leaving and reopening the screen.
- Removing favorite updates database and UI.
- Duplicate favorites are not created.

#### Commit B12 - `reservations: implement trip reservation form`

Description:

- Open a reservation form when the user clicks the Reserve button.
- The form must include:
  - traveler quantity,
  - reservation type,
  - confirmation button.
- Use Travel Planner wording: traveler quantity and reservation type.
- Validate form input:
  - quantity/count must be positive,
  - reservation type must be selected,
  - trip must exist,
  - user must be logged in.
- Optionally validate traveler quantity against a max group size if the team adds that field, but do not use non-travel seat logic as a core travel requirement.
- Store reservation in the database with:
  - current user id,
  - trip id,
  - reservation date,
  - status,
  - quantity/count,
  - type,
  - additional info if useful.
- Show success/failure Toast.
- After success, return to details or open My Reservations depending on the team decision.

Dependencies:

- Depends on Commit B09.
- Depends on current user/session data from Student A Commit A10.
- Depends on ReservationRepository from Commit B03.

Acceptance criteria:

- User can create a trip reservation record.
- Invalid forms are blocked.
- Reservation appears in the database.
- UI gives clear confirmation.

### Step 4 - My Reservations, Favorites Screen, And Special Section

#### Commit B13 - `reservations: build My Reservations section`

Description:

- Build the My Reservations fragment shown from the user drawer.
- Load only reservations for the current logged-in user.
- Each reservation row must display:
  - trip name,
  - reservation date,
  - status,
  - additional information.
- Also show quantity/count and reservation type if available because the reservation form collects them.
- Add empty state for users with no reservations.
- Let users open the related trip details from a reservation row if time allows.
- Keep combined reservation/trip data cleanly inside repository methods rather than duplicating logic in UI.

Dependencies:

- Depends on Commit B12.
- Depends on Student A Commit A11/A12 for drawer destination.

Acceptance criteria:

- My Reservations shows current user's previous and current reservations.
- Required reservation fields are visible.
- Empty state is clear.
- No other user's reservations appear.

#### Commit B14 - `favorites: build Favorites section`

Description:

- Build the Favorites fragment shown from the user drawer.
- Load only current user's favorite trips.
- For each favorite, show enough trip summary information to recognize it.
- Required actions:
  - remove from favorites,
  - open trip details,
  - make reservation directly.
- Keep favorite removal synchronized with list/details favorite state.
- Add empty state for no favorites.

Dependencies:

- Depends on Commit B11 and Commit B12.
- Depends on Student A Commit A11/A12 for drawer destination.

Acceptance criteria:

- Favorites screen lists only current user's favorites.
- User can remove a favorite.
- User can open details from favorites.
- User can start the trip reservation flow directly from favorites.

#### Commit B15 - `special: implement Travel Planner special section`

Description:

- Build the Special Section fragment shown from the user drawer.
- Choose a clear Travel Planner rule for what makes a trip special.
- Good examples:
  - travel offers,
  - popular destinations,
  - recommended trips.
- The rule must be explainable during discussion. Use an automatic rule in this plan so the special section does not wait for admin trip editing:
  - recommended trips: rating `>= 4.5`,
  - travel offers: price below the team's agreed budget threshold,
  - popular destinations: countries/destinations that appear more than once in the imported data, if the API data supports that.
- Special Section must support:
  - displaying special trips,
  - opening trip details,
  - adding trips to favorites.
- Reuse the trip adapter where possible, but avoid making the code confusing.
- Add empty state if no trip qualifies.

Dependencies:

- Depends on Commit B08, B09, and B11.
- Does not depend on admin trip editing because the chosen special-section rule is automatic.

Acceptance criteria:

- Special Section displays relevant special trips.
- User can open details.
- User can add/remove favorites.
- Logic is documented and easy to explain.

### Step 5 - Admin Home, Admin User Management, Admin Trip Management

#### Commit B16 - `admin: create admin home and separate navigation drawer`

Description:

- Create the admin home Activity/screen or admin host fragment.
- Add a separate Navigation Drawer for admin users.
- Admin drawer items must include:
  - Admin Home,
  - Add New Admin,
  - View Users,
  - Delete Users,
  - Add New Trips,
  - Edit Trips,
  - Delete Trips,
  - View Reservations,
  - Logout.
- Admin home should show a concise overview of admin capabilities.
- Use Student A's theme resources, but make the admin area visually distinct enough to avoid confusion with normal user mode.
- Add placeholders for admin actions until the next commits fill them.

Dependencies:

- Depends on Student A Commit A10 for admin login routing, or coordinate a temporary route.
- Depends on Student A Commit A04 for shared UI styles.

Acceptance criteria:

- `admin@admin.com` can reach a separate admin area after login.
- Admin drawer is different from user drawer.
- Required admin sections are visible.
- Regular users cannot see admin drawer.

#### Commit B17 - `admin-users: add admin creation and user list`

Description:

- Implement Add New Admin:
  - collect email, first name, last name, password, confirm password, phone if using shared user fields,
  - apply the same email/name/password validation rules,
  - store password securely,
  - prevent duplicate admin/user emails.
- Implement View Users:
  - show regular users in a RecyclerView or clear list,
  - display email, name, phone, gender, and trip category/travel preference if useful,
  - exclude sensitive password data.
- Keep admin creation separate from regular user registration in UI wording.
- Show success/error messages.

Dependencies:

- Depends on Commit B16.
- Depends on UserRepository from Commit B03.
- Should reuse validation utility from Student A Commit A09 where practical.

Acceptance criteria:

- Admin can add another admin.
- Admin can view users.
- Duplicate email is rejected.
- No password hashes/encrypted values are displayed.

#### Commit B18 - `admin-users: implement safe user deletion`

Description:

- Add Delete Users functionality.
- Let admin select a user and delete them after confirmation.
- Prevent dangerous deletion cases:
  - admin should not accidentally delete themselves while logged in,
  - do not delete the last admin account,
  - decide whether deleting a user also deletes that user's reservations/favorites or marks them inactive.
- Keep database referential behavior consistent:
  - either cascade related favorites/reservations,
  - or block deletion until related data is handled,
  - document the chosen behavior.
- Refresh the users list after deletion.
- Show clear success/failure messages.

Dependencies:

- Depends on Commit B17.
- Depends on database constraints from Commit B02.

Acceptance criteria:

- Admin can delete allowed users.
- Protected admin accounts are not accidentally removed.
- Related data behavior is consistent and explainable.
- UI refreshes after deletion.

#### Commit B19 - `admin-trips: add and edit trips`

Description:

- Implement Add New Trips:
  - present a form with Travel Planner fields,
  - validate required fields,
  - insert the trip into the local database.
- Implement Edit Trips:
  - show existing trips,
  - open trip edit form,
  - pre-fill current values,
  - validate edited values,
  - update database.
- Required trip fields: destination/title, country, duration days, price, rating, description, and image.
- Keep the user trip list synchronized after admin changes.
- Do not add a manual special flag unless the team intentionally expands the automatic special-section rule.

Dependencies:

- Depends on Commit B16.
- Depends on TripRepository from Commit B03.
- Should align with trip details/list fields from Commits B08-B09.

Acceptance criteria:

- Admin can create a new trip.
- Admin can edit existing trip fields.
- User-facing lists/details reflect admin-created and admin-edited trips.
- Invalid trip values are blocked.

#### Commit B20 - `admin-trips: delete trips and view reservations`

Description:

- Implement Delete Trips:
  - show current trips,
  - require confirmation before deleting,
  - handle related favorites/reservations consistently.
- Decide and document related-data behavior:
  - block deletion if reservations exist,
  - delete related favorites and keep reservation history with trip name copied,
  - or mark trip inactive instead of physical delete.
- Implement View Reservations:
  - show all reservations across users,
  - include user name/email,
  - trip name,
  - reservation date,
  - status,
  - quantity/count,
  - reservation type,
  - additional information.
- Add filters or sorting if time allows, such as by user, trip, date, or status.
- Ensure admin logout returns to Login and clears admin session.

Dependencies:

- Depends on Commit B19 for trip management.
- Depends on ReservationRepository from Commit B03.
- Depends on reservations created in Commit B12 for test data.

Acceptance criteria:

- Admin can delete or deactivate trips safely.
- Admin can view reservations from all users.
- Admin logout works.
- User-facing screens do not crash if a trip was deleted/deactivated.

### Step 6 - Data/API Documentation, Integration QA, And Final Support

#### Commit B21 - `docs: document database schema, API flow, and admin behavior`

Description:

- Add data/API documentation for the discussion session.
- Include:
  - database table list,
  - purpose of each table,
  - important columns,
  - relationships between users, trips, favorites, and reservations,
  - admin account seeding,
  - password security approach,
  - API endpoint/source,
  - trip JSON object shape,
  - import flow from Connect button to database,
  - special-section rule,
  - deletion behavior for users/trips.
- Add a simple text ERD or table relationship diagram if time allows.
- Keep it accurate with the actual implementation.

Dependencies:

- Depends on Commits B01-B20.
- Student A Commit A19 depends on this documentation.

Acceptance criteria:

- Both students can use the document to explain database/API integration.
- The docs match the implemented schema and behavior.
- Admin behavior and security decisions are clear.

#### Commit B22 - `integration: connect data features to final user and admin shells`

Description:

- Work with Student A to plug all Student B fragments into the final navigation hosts.
- Verify:
  - Trips drawer opens the real list,
  - trip details open from list/favorites/special/reservations,
  - favorite state is consistent across screens,
  - reservation form works from details and favorites,
  - My Reservations shows newly created reservations,
  - Special Section uses the final rule,
  - Admin drawer opens all admin CRUD screens,
  - admin logout returns to login.
- Fix fragment argument, toolbar title, empty state, and session bugs found during integration.

Dependencies:

- Depends on Student A Commit A12.
- Depends on Student B Commits B08-B20.
- Supports Student A Commit A17 and should be done before or alongside that integration pass.

Acceptance criteria:

- Required Student B features are reachable from Student A's navigation shell.
- No critical placeholder screens remain.
- User/admin role boundaries are respected.

#### Commit B23 - `qa: validate database, API, and admin edge cases`

Description:

- Test data-heavy edge cases:
  - first app launch with empty database,
  - successful API import,
  - duplicate API import,
  - failed API import,
  - search with no result,
  - filters with no result,
  - favorite duplicate prevention,
  - removing favorite from different screens,
  - reservation creation with invalid quantity,
  - reservation creation after trip deletion/deactivation,
  - deleting user with existing reservations/favorites,
  - deleting/editing trips with existing favorites/reservations,
  - admin cannot delete last admin,
  - regular user cannot access admin features.
- Fix Student B-owned bugs.
- Update progress log with tested scenarios.

Dependencies:

- Depends on Commit B22.
- Supports Student A Commit A18.

Acceptance criteria:

- Data layer is stable under realistic failure cases.
- Admin operations do not corrupt user-facing data.
- API import and duplicate handling are reliable.
- Test notes are recorded.

#### Commit B24 - `release: final data review and APK build support`

Description:

- Review final database/API/admin implementation before submission.
- Confirm:
  - at least 10 trips are available after Connect,
  - all trip fields required by the assignment are displayed somewhere,
  - reservations are stored locally,
  - favorites are stored locally,
  - profile updates persist,
  - admin CRUD operations persist,
  - passwords are not stored plainly,
  - exception handling avoids crashes in common failure cases.
- Help Student A verify the final APK on Pixel 3a XL API 28.
- Make final fixes only if they are small and safe.
- Do not start major refactors immediately before submission.

Dependencies:

- Depends on all Student B commits.
- Should happen alongside Student A Commit A20.

Acceptance criteria:

- Student B can explain database schema and API integration confidently.
- Final app data behavior matches assignment requirements.
- No data/admin blocker remains for APK/ZIP submission.

## Student B Suggested Branches

- `feature/data-schema`
- `feature/api-import`
- `feature/trips-list-details`
- `feature/reservations-favorites`
- `feature/special-section`
- `feature/admin-tools`
- `feature/data-qa-docs`

## Student B Main Risks

- API endpoint does not have enough valid trip objects or disappears before submission.
- Database schema changes late and breaks Student A screens.
- Password security is treated as an afterthought.
- Favorites/reservations are accidentally global instead of per-user.
- Admin deletion corrupts reservations/favorites.
- User and admin navigation are mixed together.
- Search/filter/special logic is not explainable during discussion.

## Student B Definition Of Done

Student B's work is complete when:

- API import stores at least 10 valid trips locally.
- RecyclerView list, details, search, filtering, favorite, and reservation flow work.
- My Reservations, Favorites, and Special Section work for the current user.
- Admin can add admins, view/delete users, add/edit/delete trips, view reservations, and logout.
- Database schema and API flow are documented.
- Data-heavy edge cases are tested.
- Student B can explain every table, repository method, API field, and admin rule they own.
