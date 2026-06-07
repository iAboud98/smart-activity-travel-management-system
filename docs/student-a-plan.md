# Student A Plan - App Shell, Authentication, User Screens, Integration

This plan divides the Android course project into Git-friendly commits for Student A. It is now customized for student IDs `1220216` and `1220071`, which means the project is a Travel Planner App using the Left Theme. It assumes Student A owns the application shell, visual direction, authentication flow, profile/contact screens, user navigation, integration polish, and final submission preparation.

Academic integrity note: use this document as a project-management checklist. The assignment says students must not submit AI-generated implementation, so both students should write the code themselves and be ready to explain every activity, fragment, database table, API call, validation rule, and Git commit during discussion.

## Project Facts From The Requirement

- The app must be written in Java for Android.
- The repository must be private and show regular progress, ideally 30-40 commits.
- The app name must be `1220216_1220071_CourseProject`.
- The minimum SDK must be API 28.
- The target test device is Pixel 3a XL with API Level 28 and software graphics.
- Smallest ID is `1220071`; last digit is `1`, which is odd, so the required application type is Travel Planner App.
- ID sum is `1220216 + 1220071 = 2440287`; last digit is `7`, which is greater than or equal to 5, so the required visual theme is Left Theme.
- The app must include splash/introduction, REST API import, login/register, navigation drawer, trips list, details, reservations, favorites, special section, profile management, contact intents, logout, admin drawer, admin CRUD features, local database, shared preferences, RecyclerView, fragments, animations, input validation, exception handling, and final APK/ZIP submission.

## Student A Ownership

Student A owns:

- Android Studio project setup and app-level configuration.
- App name, package setup, min SDK, Gradle structure, launcher icon/logo placeholder.
- Theme resources, colors, typography, reusable UI styles, and responsive layout rules.
- Splash screen, introduction screen, Connect button user flow.
- Login and registration UI.
- User input validation presentation and auth flow integration.
- User home screen and navigation drawer shell.
- Profile management screen.
- Contact Us screen with intents.
- Logout/session UI behavior.
- Final integration pass across all user-facing screens.
- README/progress documentation and submission packaging checklist.

Student A does not own:

- SQLite schema implementation, repository logic, or seed data, except where needed to connect UI.
- REST API endpoint creation, parsing, and database import logic.
- RecyclerView trip list/details/reservation/favorite/special implementation.
- Admin CRUD business logic.

## Shared Rules Before Any Coding

1. Both students must use the calculated decisions consistently: Travel Planner App, Left Theme, app name `1220216_1220071_CourseProject`.
2. Use one private GitHub repository. Add the second student as collaborator.
3. Work on feature branches, then merge to `main` regularly after testing. Branches are optional in the assignment, but they make the progress easier to defend.
4. Keep commits feature-sized. Do not commit the whole project in one block.
5. Every commit should build or at least leave a clearly documented temporary placeholder.
6. Update a progress log after meaningful milestones: what changed, who did it, what was tested, what is still blocked.
7. If the current tracked PDF deletion is accidental, restore it before coding or document why it was removed. Do not mix that repository cleanup with major app source changes.

## High-Level Dependency Map

- Student A Step 1 must happen before most coding because it creates the Android project structure.
- Student B Step 1 can start after Student A creates the project skeleton, because database classes need the real package/app structure.
- Student A Step 3 Commit A10 depends on Student B database/user repository work. Commit A09 is validation-only and can be finished before the database is ready.
- Student A Step 2 Commit A06 depends on Student B REST import service. Commit A05 is the splash/introduction UI and can be finished with a temporary Connect callback.
- Student A Step 4 drawer placeholders can happen before Student B feature fragments, but the final drawer wiring depends on Student B Steps 3 and 4.
- Student A profile update commits depend on Student B user repository methods.
- Student A final integration depends on Student B trips, reservations, favorites, special section, and admin features being merged.

## Proposed Commit Sequence

### Step 1 - Project Foundation And Shared Decisions

#### Commit A01 - `project: initialize Android Java project`

Description:

- Create the Android Studio project inside the repository instead of keeping only the README/screenshots.
- Use Java as the implementation language.
- Set the application display name to the required format: `1220216_1220071_CourseProject`.
- Set the minimum SDK to API 28.
- Choose a package name that clearly belongs to the group and course project.
- Create the initial `app` module, Gradle files, manifest, launcher activity, launcher icon placeholder, and base resource folders.
- Confirm the project opens in Android Studio and can build an empty APK.
- Add or confirm a `.gitignore` that excludes build outputs, local IDE state, generated APKs, and machine-specific files.
- Do not include unrelated generated files such as `.DS_Store` in the commit.

Dependencies:

- Uses the provided university IDs, so the app name should be `1220216_1220071_CourseProject` from the first Android project commit.
- Does not depend on Student B.

Acceptance criteria:

- A clean Android project exists in the repository.
- The app builds on a clean checkout.
- `minSdkVersion` is API 28.
- The application label matches the required naming rule.
- The commit contains setup files only, not half-finished feature code.

#### Commit A02 - `docs: record selected Travel Planner type, Left Theme, and team workflow`

Description:

- Add a short project decision document or update the README with:
  - both students' roles,
  - selected app type: Travel Planner App because `1220071` ends in odd digit `1`,
  - selected visual theme: Left Theme because `2440287` ends in `7`,
  - required app name: `1220216_1220071_CourseProject`,
  - private GitHub repository workflow,
  - branch naming convention,
  - commit message convention,
  - how progress will be documented.
- Add a simple progress log table with columns for date, student, branch, commit, feature, tested status, and notes.
- Record the API/data wording that will be used everywhere: "trip", "destination", "country", "duration days", "price", "rating", "reserve", "travel offers", "popular destinations", and "recommended trips".
- Record the team's interpretation of Left Theme, such as left-side navigation drawer, left-weighted layout alignment, left-positioned key visual elements, or any instructor-specific direction if given.

Dependencies:

- Requires both students to agree that the calculated result is Travel Planner App and Left Theme.
- Helps Student B choose correct database fields and trip API object names.

Acceptance criteria:

- A teammate can read the README/docs and know exactly which variant the group is implementing.
- The docs explain how GitHub progress will reach 30-40 meaningful commits.
- No app feature implementation is mixed into this documentation commit.

#### Commit A03 - `project: add base package structure and placeholder navigation targets`

Description:

- Create clear package folders for the app architecture, for example:
  - activities,
  - fragments,
  - adapters,
  - models,
  - database,
  - repositories,
  - network,
  - utils.
- Add empty or minimal placeholder classes only where they are needed to let both students work without path conflicts.
- Add placeholder fragments or screens for the final drawer destinations:
  - Home,
  - Trips,
  - My Reservations,
  - Favorites,
  - Special Section,
  - Profile Management,
  - Contact Us.
- Use placeholder text only. Do not pretend features are complete.
- Add TODO notes only where ownership is clear, such as "Student B will replace this placeholder with the RecyclerView list."

Dependencies:

- Depends on Commit A01.
- Should be coordinated with Student B before they create database/repository classes.

Acceptance criteria:

- Both students know where to place new classes.
- The project still builds.
- Placeholder code is minimal and clearly temporary.
- No database or network logic is introduced yet.

### Step 2 - Design System, Splash Screen, And Introduction Flow

#### Commit A04 - `ui: define app theme resources and reusable screen styles`

Description:

- Create the app's base visual design according to the selected Left Theme.
- Define color resources for primary, secondary, background, surface, error, text, selected item, and disabled states.
- Define reusable button styles, text input styles, card/list item styles, toolbar styles, drawer item styles, and screen spacing resources.
- Add responsive layout values so screens work on the required Pixel 3a XL API 28 emulator and remain usable on smaller/larger screens.
- Add app logo or clean placeholder logo used by the splash screen.
- Add icons for navigation drawer items using Android vector drawables or an approved icon source.
- Make UI text resources centralized in `strings.xml` so the app consistently uses travel wording: Trips, Reserve, Destination, Country, Duration, Price, Rating, Favorites, Travel Offers, Popular Destinations, and Recommended Trips.

Dependencies:

- Depends on Commit A02 because the selected Left Theme and Travel Planner type affect colors, wording, and layout direction.
- Student B can continue data work in parallel.

Acceptance criteria:

- The app has a consistent visual identity before feature screens are built.
- No hard-coded colors or repeated button styles are scattered across layouts.
- Trip labels are not duplicated manually in multiple screens.
- The project builds and the default screen uses the new theme.

#### Commit A05 - `ui: implement animated splash and introduction screen`

Description:

- Add an animated splash screen that displays the application logo for 2-3 seconds.
- Use one of the required animation types here, such as fade, scale, slide, or splash animation.
- After the splash animation finishes, navigate to the introduction layout.
- The introduction layout must show:
  - the selected application title,
  - a short description for a Travel Planner App,
  - a clear Connect button.
- The introduction screen should not allow the user to continue to login before attempting the required API connection.
- Add basic loading state for Connect:
  - disable the button while connection is in progress,
  - show progress feedback,
  - avoid duplicate taps.
- At this stage, if Student B's API import service is not ready, wire the button to a temporary interface or placeholder that returns "not implemented" without navigating.

Dependencies:

- Depends on Commit A04.
- Full Connect success/failure behavior is completed in Commit A06 after Student B Commit B07 is ready.

Acceptance criteria:

- The app starts with a splash logo animation.
- Splash duration is inside the required 2-3 second range.
- Introduction screen appears after splash.
- Connect button exists and has a visible loading/disabled state.
- No user can accidentally bypass the intended connect-first flow.

#### Commit A06 - `flow: connect introduction button to API import result`

Description:

- Replace the temporary Connect behavior with Student B's real API import service.
- When the user taps Connect, call the API import flow once.
- If the API connection succeeds:
  - store the received trips in the local database through Student B's data layer,
  - navigate to the login/register screen,
  - avoid inserting duplicates if Connect is pressed again later.
- If the API connection fails:
  - show a clear Toast message,
  - stay on the introduction screen,
  - re-enable the Connect button,
  - leave the app in a safe state without partial broken navigation.
- Handle slow network, empty response, invalid JSON, duplicate data, and exceptions gracefully.

Dependencies:

- Depends on Student B Commit B07.
- Depends on Commit A05.

Acceptance criteria:

- Successful API import leads to login/register.
- Failed API import does not leave the introduction screen.
- The local database contains at least 10 imported trips after success.
- The user receives understandable feedback for failure.

### Step 3 - Login, Registration, Validation, And Session Entry

#### Commit A07 - `auth-ui: build login screen layout`

Description:

- Create the login screen with:
  - email field,
  - password field,
  - Remember Me checkbox,
  - Login button,
  - Sign-Up button.
- Apply the reusable theme styles from Commit A04.
- Use proper input types for email and password.
- Include inline error display support for invalid fields.
- Add loading state for login.
- Add navigation from Sign-Up to the registration screen placeholder.
- Pre-fill the email if Remember Me data already exists.
- Do not authenticate against hard-coded temporary users in this commit.

Dependencies:

- Depends on Commit A04.
- Can happen before Student B database auth is complete if it remains UI-only.

Acceptance criteria:

- Login screen matches the required fields.
- The screen is responsive and keyboard-safe.
- Sign-Up navigation works.
- Remembered email can be displayed when the shared-preference contract is available.

#### Commit A08 - `auth-ui: build registration screen layout`

Description:

- Create the registration screen with all required fields:
  - email,
  - first name,
  - last name,
  - password,
  - confirm password,
  - gender spinner,
  - trip category/travel preference spinner,
  - phone number.
- Use "travel preference" or "trip category" wording for the spinner instead of "major", because this project is a Travel Planner App.
- Add clear labels and error containers for every input.
- Add Register button and Back/Login link.
- Configure spinner options with sensible values.
- Use password input mode for password fields.
- Make the form scroll safely on the Pixel 3a XL emulator with the keyboard open.

Dependencies:

- Depends on Commit A04.
- Should coordinate field names with Student B Commit B01/B02 so the database schema matches the form.

Acceptance criteria:

- Registration screen contains every field from the requirement.
- No required input is hidden or inaccessible when the keyboard opens.
- Spinner values are visible and selectable.
- Navigation back to login works.

#### Commit A09 - `auth: add client-side validation and user-facing errors`

Description:

- Implement validation before login and registration submission.
- Login validation:
  - email is not empty,
  - email follows email format,
  - password is not empty.
- Registration validation:
  - email is not empty and follows email format,
  - first name is at least 3 characters,
  - last name is at least 3 characters,
  - password has at least 1 letter,
  - password has at least 1 number,
  - password has at least 6 characters,
  - confirm password matches password,
  - gender spinner has a valid selected value,
  - trip category/travel preference spinner has a valid selected value,
  - phone number is not empty and follows the agreed phone format.
- Show field-level errors where possible and Toasts only for broader errors.
- Keep validation messages understandable enough for the discussion session.
- Put reusable validation logic in a utility class so profile management can reuse password/name/phone rules later.

Dependencies:

- Depends on Commits A07 and A08.
- Does not require Student B yet because it validates before database operations.

Acceptance criteria:

- Invalid login and registration inputs are blocked before database calls.
- Each invalid field explains what the user must fix.
- Password rules exactly match the assignment.
- The same validation methods can be reused by profile updates.

#### Commit A10 - `auth: integrate login, registration, remember-me, and role routing`

Description:

- Connect the login and registration screens to Student B's user repository/database layer.
- Registration behavior:
  - validate all inputs,
  - prevent duplicate email registration,
  - store the password securely through the agreed encryption/hashing method,
  - save all required profile fields,
  - redirect to Login screen after success,
  - show a clear error if registration fails.
- Login behavior:
  - validate inputs,
  - authenticate normal users from the database,
  - authenticate the required pre-created admin account,
  - if normal user login succeeds, navigate to the user Home layout,
  - if admin login succeeds, navigate to the admin Home layout provided by Student B,
  - if login fails, display an error message and remain on login.
- Remember Me behavior:
  - save the email in Shared Preferences when checked,
  - ensure any stored password-related value is encrypted and never plain text,
  - clear remembered values when unchecked or on logout if the team chooses that behavior.
- Session behavior:
  - store current user ID/email and role in a controlled session preference,
  - prevent a regular user from opening admin screens directly.

Dependencies:

- Depends on Student B Commit B02 for user/admin database tables.
- Depends on Student B Commit B03 for repository methods.
- Does not hard-depend on the finished admin drawer. If Student B Commit B16 is not ready yet, route successful admin login to a temporary admin placeholder and replace it during integration.

Acceptance criteria:

- Registration creates a real database user.
- Login succeeds for valid users and fails for invalid users.
- `admin@admin.com` with `Admin123!` reaches the admin area.
- Remember Me does not store plain text password data.
- A successful normal user login reaches the user home screen.

### Step 4 - Main User Home And Navigation Drawer

#### Commit A11 - `home: create user home screen and navigation drawer shell`

Description:

- Create the main user Activity or host screen with a Navigation Drawer.
- Add drawer items required by the assignment:
  - Home,
  - Trips,
  - My Reservations,
  - Favorites,
  - Special Section,
  - Profile Management,
  - Contact Us,
  - Logout.
- Label the main trip section as Trips.
- Make Home display:
  - application overview,
  - Left Theme description,
  - attractive UI design that matches the chosen Left Theme.
- Add a toolbar/header showing the app name and current user display name if available.
- Use a fragment container so Student B can plug in feature fragments cleanly.
- Use placeholder fragments for Student B-owned destinations until their commits are merged.

Dependencies:

- Depends on Commit A10 for user session entry.
- Student B can work on feature fragments in parallel after the host contract is clear.

Acceptance criteria:

- Normal user login opens the drawer-based home area.
- All required drawer items are present.
- Home page explains the application and theme.
- Drawer selection changes the displayed fragment or placeholder.

#### Commit A12 - `navigation: implement drawer routing, back behavior, and logout`

Description:

- Wire every drawer item to a fragment destination.
- Add correct back behavior:
  - close drawer if it is open,
  - return from detail/profile/contact screens sensibly,
  - avoid exiting the app unexpectedly from nested destinations.
- Implement Logout:
  - clear current session values,
  - return to Login screen,
  - prevent back navigation into authenticated screens after logout.
- Keep Remember Me behavior consistent with the team decision from Commit A10.
- Add clear Toast or UI feedback for logout if needed.
- Add exception handling for missing fragments or invalid navigation state.

Dependencies:

- Depends on Commit A11.
- Final routing to Trips, Reservations, Favorites, and Special Section depends on Student B Commits B08-B15.

Acceptance criteria:

- Drawer navigation does not crash.
- Logout ends the session and returns to Login.
- Pressing Back after logout does not reopen Home.
- Placeholder screens clearly identify unfinished Student B features until integrated.

#### Commit A13 - `ui: polish shared layouts and add second animation`

Description:

- Add a second animation type required by the assignment, separate from the splash animation.
- Good candidates:
  - button scale animation on important actions,
  - fragment slide transition,
  - fade animation for list/detail transitions.
- Apply responsive spacing and consistent typography across splash, introduction, login, registration, home, drawer, profile, and contact screens.
- Check landscape/portrait behavior if time allows.
- Improve accessibility basics:
  - readable contrast,
  - meaningful content descriptions for important icons/images,
  - touch targets large enough for mobile use.
- Remove rough placeholder UI from Student A-owned screens.

Dependencies:

- Depends on Commits A04-A12.
- Does not block Student B, but Student B should reuse the final styles.

Acceptance criteria:

- The app has at least two animations total.
- UI looks consistent across Student A screens.
- No critical text is clipped on Pixel 3a XL API 28.
- Student B can reuse the style resources for list/admin screens.

### Step 5 - Profile Management And Contact Us

#### Commit A14 - `profile: display current user information`

Description:

- Create the Profile Management fragment/screen.
- Display current user information from the database/session:
  - email,
  - first name,
  - last name,
  - gender,
  - trip category/travel preference,
  - phone number,
  - profile picture if available.
- Email should usually be read-only unless the team explicitly supports changing it safely.
- Show a default profile image if the user has not selected one.
- Show empty/error states if the user record cannot be loaded.
- Use the shared design system and drawer host.

Dependencies:

- Depends on Commit A11.
- Depends on Student B Commit B03 for loading user data.

Acceptance criteria:

- Logged-in user sees their own profile information.
- No other user's data is shown accidentally.
- Missing profile picture is handled gracefully.
- Screen remains usable if database loading fails.

#### Commit A15 - `profile: update user fields with validation`

Description:

- Allow the user to update:
  - first name,
  - last name,
  - password,
  - phone number,
  - profile picture.
- Reuse the same validation rules from registration:
  - first/last name minimum 3 characters,
  - password has letter, number, and at least 6 characters,
  - phone follows agreed format.
- Require confirmation for password update if the UI includes a new password/confirm password pair.
- Store updated password securely through Student B's repository/database method.
- Let the user select/add a profile picture using a safe Android intent or agreed local storage approach.
- Show success and failure feedback.
- Refresh displayed profile data after saving.

Dependencies:

- Depends on Commit A14.
- Depends on Student B Commit B03 for update methods.
- If profile images require database/file-path support, coordinate with Student B before finalizing storage.

Acceptance criteria:

- User can update every required editable field.
- Invalid values are blocked with clear messages.
- Updated values persist after leaving and reopening Profile.
- Password updates do not store plain text.
- Profile picture appears after selection and survives app restart if the chosen storage approach supports it.

#### Commit A16 - `contact: add call, map, and email intents`

Description:

- Create the Contact Us screen with exactly the required three buttons:
  - Call Us,
  - Locate Us,
  - Email Us.
- Call Us:
  - open the phone dialer with a predefined number,
  - use a safe intent that does not require placing the call automatically.
- Locate Us:
  - open Google Maps or a map-compatible app with a predefined location.
- Email Us:
  - open Gmail or an email-compatible app with a predefined email address, subject, and optional body.
- Handle the case where no app exists to handle the intent by showing a Toast instead of crashing.
- Keep predefined contact values in one clear place, not scattered through the layout and code.

Dependencies:

- Depends on Commit A11.
- Does not depend on Student B.

Acceptance criteria:

- All three buttons open the correct external app/intention on the emulator/device.
- Missing external app handlers do not crash the app.
- Contact screen matches the visual theme.

### Step 6 - Final Integration, QA, Documentation, And Submission

#### Commit A17 - `integration: connect user drawer to feature fragments`

Description:

- Replace Trips, My Reservations, Favorites, and Special Section placeholders with Student B's completed fragments.
- Confirm data is passed correctly between list, details, reservation form, favorites, and drawer destinations.
- Ensure labels match the selected Travel Planner App everywhere: Trips, Reserve, destination, country, duration days, price, rating, travel offers, popular destinations, and recommended trips.
- Confirm the user cannot access features before login.
- Confirm admin-only screens are not visible in the normal user drawer.
- Fix integration-level UI spacing or toolbar title issues.

Dependencies:

- Depends on Student B Commits B08-B15.
- Depends on Commit A12.

Acceptance criteria:

- Every normal user drawer item opens a real feature screen.
- List/detail/reserve/favorite actions work from the user shell.
- No placeholder text remains in user-facing required screens.

#### Commit A18 - `qa: stabilize full user journey and error handling`

Description:

- Test the complete normal user journey:
  - launch app,
  - splash animation,
  - introduction,
  - failed API connection behavior,
  - successful API connection behavior,
  - registration,
  - login,
  - drawer navigation,
  - trip list,
  - search/filter,
  - trip details,
  - favorite trip,
  - make reservation,
  - view reservation,
  - open special trip,
  - update profile,
  - use contact buttons,
  - logout.
- Check important failure cases:
  - duplicate registration email,
  - wrong password,
  - empty API response,
  - invalid trip/reservation input,
  - no current user session,
  - database operation failure.
- Fix Student A-owned UI/session/navigation bugs found during testing.
- Record test results in the progress log.

Dependencies:

- Depends on Student B feature integration and Student A commits A01-A17.

Acceptance criteria:

- Required normal-user flow is demonstrable without crashes.
- Toast/error messages are clear.
- Session/logout behavior is reliable.
- The progress log names what was tested and what was fixed.

#### Commit A19 - `docs: prepare README, discussion notes, and submission checklist`

Description:

- Update README with:
  - app name,
  - selected app type and theme: Travel Planner App and Left Theme,
  - team members and responsibilities,
  - feature list mapped to assignment requirements,
  - API endpoint/source,
  - database overview,
  - how to run on Pixel 3a XL API 28,
  - admin credentials,
  - screenshots of key screens if appropriate,
  - known limitations if any.
- Prepare discussion notes explaining:
  - navigation structure,
  - database structure at a high level,
  - API integration flow,
  - validation rules,
  - how reservations/favorites work,
  - how admin functions are protected.
- Add a final submission checklist:
  - private GitHub repository link,
  - `Project.zip` exported from Android Studio,
  - `app-debug.apk` built from Android Studio,
  - APK path: `app/build/outputs/apk/debug/app-debug.apk`,
  - confirmation that both students can explain the project.

Dependencies:

- Depends on Student B Commit B21 for database/API/admin notes.
- Should be one of the final commits after features are stable.

Acceptance criteria:

- README is useful for the instructor and for the team during discussion.
- Submission requirements are listed clearly.
- No generated build artifacts are committed unless the instructor explicitly requested them in Git.

#### Commit A20 - `release: final emulator verification and build handoff`

Description:

- Run the app on Pixel 3a XL API 28 with software graphics.
- Verify the final app name `1220216_1220071_CourseProject`, launcher icon/logo, splash timing, Left Theme, and Travel Planner wording.
- Build the debug APK from Android Studio.
- Export the project ZIP from Android Studio.
- Confirm the repository has regular progress with at least 30 meaningful commits, preferably close to the 30-40 range requested by the assignment. This plan has more than 40 proposed commits, so the team can combine only small documentation/QA commits if the instructor expects a tighter count.
- Confirm no passwords are stored as plain text in obvious places.
- Confirm no `.DS_Store`, build folders, local IDE caches, or unrelated screenshots are accidentally committed.
- Add final progress-log entry stating the tested emulator/device and build outputs.

Dependencies:

- Depends on all Student A and Student B feature commits.
- Should be the last Student A-owned commit before submission.

Acceptance criteria:

- Final APK exists locally.
- Final ZIP exists locally.
- App runs on the required emulator.
- GitHub history shows incremental work, not a one-time upload.

## Student A Suggested Branches

- `feature/project-foundation`
- `feature/ui-theme-splash`
- `feature/auth-flow`
- `feature/user-navigation`
- `feature/profile-contact`
- `feature/final-integration`
- `feature/submission-docs`

## Student A Main Risks

- Travel Planner and Left Theme wording is applied inconsistently across screens.
- Authentication UI is finished before the database contract is agreed, causing rework.
- Connect button is visually complete but not integrated with real API/database behavior.
- Drawer placeholders remain until the end and integration becomes rushed.
- Remember Me stores sensitive values incorrectly.
- Final APK/ZIP build is attempted too late.

## Student A Definition Of Done

Student A's work is complete when:

- The app launches through splash and introduction.
- Connect success/failure behaves exactly as required.
- Login/register/profile/contact/user drawer/logout work end to end.
- Student B's feature fragments are integrated into the user drawer.
- The app has at least two animations.
- The UI consistently follows the selected Left Theme.
- Final docs and submission checklist are ready.
- Student A can explain every screen they own without reading from code.
