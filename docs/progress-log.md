# Progress Log

## 2026-06-20 — Student A A18: full user QA and stabilization

- Ran JVM tests and Android lint successfully.
- Ran all 9 instrumentation tests successfully on Medium Phone API 36.1.
- Live-smoke-tested cold launch, splash, Introduction, the configured Mocki endpoint, successful import, and routing to Login.
- Automated coverage included invalid/short API data, malformed trip isolation, duplicate registration, wrong password, missing session, quoted search input, trip details, favorites, reservation validation/persistence, all traveler destinations, profile update, Dial/Map/Email intents, and logout.
- Fixed nested navigation/session issues, API failure classification, item-level JSON validation, parameterized search, invalid reservation parsing, and database/load failure feedback.
- Deferred required Pixel 3a XL API 28 final visual/build verification to A20.

## 2026-06-20 — Student A A19: documentation and submission preparation

- Expanded the README with project decisions, team ownership, requirement mapping, API/database overview, admin credentials, Pixel 3a XL API 28 run steps, verification commands, and known limitations.
- Added instructor discussion notes covering navigation, database relationships, API import, validation, reservations/favorites, and admin protection.
- Added the final GitHub/APK/ZIP/emulator/team sign-off checklist.
- Reconciled Special Section wording with the UI: Travel Offers, Popular Destinations, and Recommended Trips.
- Final APK build, `Project.zip` export, screenshots, and Pixel 3a XL API 28 sign-off remain assigned to A20.

## 2026-06-20 — Student A A20: final release verification and handoff

- Launched the existing `Pixel_3a_XL` AVD with Android API 28 and forced SwiftShader software rendering; confirmed API level 28, boot completion, and enabled animations.
- Ran `./gradlew clean testDebugUnitTest lintDebug connectedDebugAndroidTest assembleDebug` successfully.
- All 9 A20 instrumentation tests passed on `Pixel_3a_XL(AVD) - 9` with no skipped or failed tests.
- Installed the exact generated APK and cold-launched it from cleared app data.
- Visually verified the app logo, `1220216_1220071_CourseProject` label, 2.5-second animated splash flow, Left Theme accent/alignment, and Travel Planner wording.
- Pressed Connect against the live configured Mocki endpoint and confirmed successful routing to Login.
- Logged in with the seeded administrator and confirmed the protected Admin area and its separate Home, Add Admin, View Users, Manage Trips, View Reservations, and Logout drawer items.
- Confirmed the repository contains 61 incremental commits before A20, exceeding the 30–40 progress target.
- Audited password writes: registration/profile/admin seed values are hashed before SQLite storage; Remember Me stores only email. The documented demo admin credential remains intentionally visible for assessment.
- Confirmed no APK, build folder, local IDE/cache, `local.properties`, `.DS_Store`, or unrelated screenshot is tracked; removed one obsolete zero-byte step marker.
- Final APK: `app/build/outputs/apk/debug/app-debug.apk`.
- APK SHA-256: `6e5c21834426f0bd45eb806f9f1ec2d3664396770f17e3879a81cfaea65420fa`.
- External handoff directory: `/private/tmp/travel-planner-a20/` (APK copy, `Project.zip`, checksums, and verification captures).

## 2026-06-20 — Critical admin-role hotfix

- Found that `AdminAddAdminFragment` supplied the admin role but `UserRepository.registerUser()` overwrote every inserted role with `user`.
- Split account creation into explicit repository contracts: public `registerUser()` always persists `user`, while the protected admin flow calls `registerAdmin()`, which always persists `admin`.
- Added regression coverage proving a tampered public-registration model cannot escalate privileges and a newly registered admin authenticates with `ROLE_ADMIN` and is counted as an admin.
- Added an end-to-end Add New Admin form regression and passed the expanded 10/10 instrumentation suite on Pixel 3a XL API 28.
- Fixed deleted-email reuse: duplicate checks now consider active accounts only, and registration with an inactive email restores that same row with the new details, password hash, active state, and requested role.
- Reproduced `admin2@admin.com` as user → delete → Add New Admin; verified the local ID is preserved, the old password stops working, and the new account authenticates as admin.
- Re-ran compilation, JVM tests, lint, and APK assembly successfully.
- Rebuilt the final APK and source ZIP and refreshed external handoff checksums.

## 2026-06-20 — Admin-trip creation hotfix

- Separated admin-created trips from remote API upserts: local trips now store a null `api_id`, so creating another local trip cannot conflict with or overwrite the first one.
- Corrected repository success semantics so a successful remote refresh reports success instead of a false failure.
- Added case-insensitive, trimmed duplicate-active-destination validation to both the admin form and repository create/update paths.
- Added safe numeric parsing for duration, price, and rating.
- Reproduced the reported `Ramallah` sequence and verified the first create succeeds, a second `ramallah` is rejected, the original data remains unchanged, and a different local trip can still be created.
- Passed compilation, JVM tests, lint, APK assembly, and the expanded 11/11 instrumentation suite on Pixel 3a XL API 28.
- Refreshed the final APK and source ZIP handoff artifacts with the hotfix.
