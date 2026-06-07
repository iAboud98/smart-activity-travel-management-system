# Project Plan Overview - Travel Planner App

This overview connects the two detailed student plans into one shared execution order. Use it as the quick reference before opening the full Student A and Student B files.

## Fixed Project Decisions

- Student IDs: `1220216` and `1220071`.
- Required app name: `1220216_1220071_CourseProject`.
- Smallest ID: `1220071`; last digit `1` is odd, so the app type is Travel Planner App.
- ID sum: `1220216 + 1220071 = 2440287`; last digit `7` is greater than or equal to 5, so the theme is Left Theme.
- Main user wording must be: Trips, Reserve, Destination, Country, Duration Days, Price, Rating, Travel Offers, Popular Destinations, Recommended Trips.
- Avoid event wording in the app UI. Do not use Events, Join, Seats, Featured Events, or University Events labels.

## Commit Count Guidance

The two detailed plans contain more than 40 proposed commits. That is intentional because it gives the team a safe, feature-by-feature path. The assignment asks for regular progress with around 30-40 commits, so the team should keep all feature commits separate and only combine small documentation or QA commits if the instructor expects the final count to be closer to 40.

Never combine major app files into one late commit. That is the main grading risk.

## Recommended Shared Merge Order

### Phase 1 - Repository And Decisions

1. Student A Commit A01: create the Android Java project with app name `1220216_1220071_CourseProject` and API 28 minimum SDK.
2. Student A Commit A02: document Travel Planner App, Left Theme, team workflow, branch names, and progress log.
3. Student A Commit A03: create base package structure and placeholder navigation targets.
4. Student B Commit B01: define shared models/contracts after the package structure exists.

Key handoff: Student B should not start database classes until A01/A03 make the real package structure clear.

### Phase 2 - Foundations In Parallel

1. Student A Commit A04: define Left Theme resources and shared UI styles.
2. Student A Commit A05: build splash and introduction UI with temporary Connect callback if needed.
3. Student A Commits A07-A09: build login/register UI and validation.
4. Student B Commits B02-B03: create database schema, seed admin, and implement repositories.
5. Student B Commits B04-B07: create/select trip API, parse it, import trips, and test success/failure.
6. Student A Commit A06: connect the introduction Connect button to Student B's tested API import.
7. Student A Commit A10: integrate login, registration, Remember Me, session, and role routing.

Key handoffs:

- A09 does not need Student B's database because it is validation-only.
- A10 needs B02/B03 because it writes and reads real users.
- A06 needs B07 because Connect must store at least 10 trips before navigating to login.
- A10 can route admin login to a temporary admin placeholder if B16 is not finished yet.

### Phase 3 - User Shell And Trip Features

1. Student A Commit A11: build the user home screen and Navigation Drawer with Trips, My Reservations, Favorites, Special Section, Profile Management, Contact Us, and Logout.
2. Student A Commit A12: finish drawer routing, back behavior, and logout.
3. Student B Commit B08: build Trips RecyclerView.
4. Student B Commit B09: build trip details.
5. Student B Commit B10: add trip search and filtering.
6. Student B Commit B11: add favorite toggle.
7. Student B Commit B12: add trip reservation form.
8. Student B Commit B13: build My Reservations.
9. Student B Commit B14: build Favorites.
10. Student B Commit B15: build Special Section with automatic Travel Planner rules.
11. Student A Commits A14-A16: build Profile and Contact Us while Student B finishes trip features.
12. Student A Commit A13: polish shared UI and add the second animation.

Key handoffs:

- B08-B15 need A11/A12 so the fragments have a drawer host.
- A14/A15 need B03 user repository methods.
- B15 uses automatic rules, so it does not wait for admin trip editing.

### Phase 4 - Admin Area

1. Student B Commit B16: build admin home and separate admin drawer.
2. Student B Commit B17: add admin creation and user list.
3. Student B Commit B18: implement safe user deletion.
4. Student B Commit B19: add/edit trips.
5. Student B Commit B20: delete/deactivate trips and view reservations.
6. Student B Commit B22: support final integration of all Student B features into the final user/admin shells.

Key handoffs:

- B16 needs A10 admin login routing, or both students agree on a temporary route.
- Admin users must not appear in the normal user drawer.
- Regular users must not access admin screens.

### Phase 5 - Integration, QA, Documentation, Submission

1. Student A Commit A17: connect the final user drawer to Student B's real Trips, Reservations, Favorites, and Special Section fragments.
2. Student B Commit B23: test database, API, favorites, reservations, admin edge cases.
3. Student A Commit A18: test the full normal user journey and fix UI/session/navigation issues.
4. Student B Commit B21: document database schema, API flow, special-section rule, and admin behavior.
5. Student A Commit A19: prepare README, discussion notes, and submission checklist.
6. Student B Commit B24 and Student A Commit A20: final data review, emulator verification, APK build, ZIP export, and submission handoff.

Key handoffs:

- A17 needs B08-B15 and should use B22 integration support.
- A18 should happen after both user and admin paths have been merged enough to test.
- A19 needs B21 so the README accurately explains the database and API.

## Final Consistency Checklist

- App name is exactly `1220216_1220071_CourseProject`.
- The UI says Travel Planner App, not Smart University Events App.
- Main drawer label is Trips, not Events/Trips.
- Main action label is Reserve, not Join.
- API stores at least 10 trips locally after Connect succeeds.
- Trip fields are destination/title, country, duration days, price, rating, description, and image.
- Registration uses trip category/travel preference for the required Major or Category spinner.
- Special Section uses clear Travel Planner rules such as recommended trips and travel offers.
- Admin drawer is separate from normal user drawer.
- Password values are never stored plainly.
- Every required screen is reachable after login.
- Logout clears the current session and prevents back navigation into protected screens.
- GitHub history shows steady feature-by-feature progress.
