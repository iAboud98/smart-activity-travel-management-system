# 1220216_1220071_CourseProject

An Android Java **Travel Planner App** for ENCS5150. The application imports trips from a REST API, stores them in SQLite, and provides separate traveler and administrator experiences using the required **Left Theme**.

## Project Decisions

| Item | Value |
|---|---|
| App name | `1220216_1220071_CourseProject` |
| Package | `com.encs5150.students1220216_1220071.travelplanner` |
| Application type | Travel Planner App |
| Visual theme | Left Theme |
| Minimum Android version | API 28 |
| Implementation language | Java |

The smallest student ID is `1220071`; its final digit is odd, selecting the Travel Planner variant. The sum `1220216 + 1220071 = 2440287` ends in `7`, selecting the Left Theme. The theme is expressed through left-aligned screen composition and navigation drawers for both roles.

## Team

| Team member | Student ID | Role | Main responsibilities |
|---|---:|---|---|
| Aboud Fialah | 1220216 | Student A | Project/UI foundation, splash and introduction, authentication UI and integration, user drawer, profile, contact intents, final integration, QA, and submission documentation |
| Lara Fuqaha | 1220071 | Student B | Models, SQLite and repositories, REST import, trip/favorite/reservation features, Special Section, admin tools, data QA, and database/API documentation |

## Assignment Feature Map

| Requirement | Implementation |
|---|---|
| Splash and animations | Animated splash logo followed by the Introduction screen; drawer destinations use fragment transitions |
| REST API and local storage | Connect fetches the trip JSON, validates at least 10 usable trips, and imports/updates them in SQLite |
| Registration and login | Validated registration, duplicate-email protection, hashed passwords, Remember Me email, and role-based routing |
| Navigation Drawer and fragments | Separate traveler and admin drawers; feature destinations are implemented as fragments |
| RecyclerView | Trip, favorite, reservation, user, and admin reservation lists use RecyclerView adapters |
| Trips | Browse, search by destination/country/description, filter by duration/price/rating, and open full details |
| Favorites | Add/remove favorites from trip lists and details; view and reserve from Favorites |
| Reservations | Reserve a trip with traveler count, type, date, status, and optional notes; view/cancel reservations |
| Special Section | Travel Offers, Popular Destinations, and Recommended Trips are generated from rating/reservation rules |
| Profile Management | View and update names, phone, password, and profile picture URI with validation |
| Contact Us | Safe Dial, Map, and Email intents with no-handler error messages |
| Administrator area | Add admins, view/delete users, add/edit/deactivate trips, view reservations, and logout |
| Session protection | Normal users cannot open admin screens; missing/invalid sessions route to Login; logout clears the task/session |
| Error handling and QA | Clear API/database/input errors plus JVM, lint, and emulator instrumentation verification |

## API and Import

- Method: `GET`
- Endpoint: [Mocki Travel Planner dataset](https://mocki.io/v1/9febcb0b-b3f6-493a-8e78-714a28fa676e)
- Expected source size: 14 trips
- Required fields: `id`, `destination`, `country`, `duration_days`, `price`, `rating`, `description`, and `image`

Connect runs the request off the UI thread, validates each item, requires at least 10 usable trips, and then imports them through `TripRepository`. The unique remote `api_id` prevents duplicate rows; reconnecting refreshes an existing trip instead of creating another copy. A network failure or invalid/short response keeps the user on Introduction and enables retry.

## Local Database

The app uses `travel_planner.db` through `SQLiteOpenHelper` and repository classes.

| Table | Purpose |
|---|---|
| `users` | Traveler/admin accounts, profile values, role, active state, and SHA-256 password hash |
| `trips` | Imported/admin-created trip details and soft-delete state |
| `favorites` | Unique user-trip favorite pairs |
| `reservations` | User-trip reservation quantity, type, date, status, and notes |

`users` and `trips` use `is_active` for soft deletion. Favorites and reservations connect users to trips using their local IDs. See [database_and_api_docs.md](docs/database_and_api_docs.md) for the full schema and rules.

## Admin Login

The database seeds this demonstration administrator on first creation:

- Email: `admin@admin.com`
- Password: `Admin123!`

The password is hashed before it is inserted. These are course-project demo credentials and must not be reused for a real service.

## Run on Pixel 3a XL API 28

Prerequisites:

- Android Studio with its bundled JDK.
- Android SDK Platform 28 and a Pixel 3a XL API 28 system image.
- Internet access for Gradle dependencies, the trip API, and remote images.

Steps:

1. Clone the private repository: `git clone git@github.com:iAboud98/smart-activity-travel-management-system.git`.
2. Open the repository root in Android Studio and wait for Gradle sync to finish.
3. Open **Device Manager**, create a **Pixel 3a XL** virtual device, choose **API 28**, and select **Software** graphics in its advanced/emulated performance settings.
4. Select the `app` run configuration and the Pixel 3a XL emulator.
5. Run the application. After the splash screen, press **Connect** and wait for Login.
6. Register a traveler account or use the seeded admin credentials above.

Command-line build from the repository root:

```bash
./gradlew assembleDebug
```

The resulting APK is written to `app/build/outputs/apk/debug/app-debug.apk`.

## Verification

```bash
./gradlew testDebugUnitTest
./gradlew lintDebug
./gradlew connectedDebugAndroidTest
```

The A18 QA pass completed all three checks successfully, including nine emulator instrumentation tests covering API outcomes, registration/login failures, protected navigation, trips, favorites, reservations, profile updates, contact intents, and logout. The final release suite now contains eleven tests, including end-to-end Add New Admin and admin-trip duplicate regressions; all eleven pass on the required Pixel 3a XL API 28 AVD with SwiftShader software graphics.

## Known Limitations

- Data is local to one installation; there is no account or reservation synchronization across devices.
- The initial trip import and remote trip images require network access.
- Profile-picture persistence depends on the selected document provider continuing to grant URI read access.
- Popular Destinations and Recommended Trips may be empty until enough reservations exist to satisfy their rules.
- The course-project password scheme uses SHA-256 hashing; a production application should use a salted, adaptive password-hashing system and server-side authentication.
- Database migration logic is minimal because the assessed flow uses a fresh version-1 database.

## Project Documentation

- [Discussion notes](docs/discussion-notes.md)
- [Database, API, and admin documentation](docs/database_and_api_docs.md)
- [QA progress log](docs/progress-log.md)
- [Final submission checklist](docs/submission-checklist.md)

Final Pixel 3a XL verification captures are stored with the external release handoff artifacts rather than committed to the source repository.
