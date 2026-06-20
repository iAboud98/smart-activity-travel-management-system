# Final Submission Checklist

Complete this checklist during A20. Do not commit generated APK, ZIP, build, IDE, or emulator files unless the instructor explicitly requests them in Git.

## Repository

- [ ] Confirm the repository is private: [smart-activity-travel-management-system](https://github.com/iAboud98/smart-activity-travel-management-system).
- [ ] Confirm both students remain repository collaborators.
- [ ] Pull/merge the final approved work and confirm no unresolved conflicts.
- [x] Confirm the history shows regular, meaningful feature commits rather than a one-time upload (61 commits before A20).
- [x] Confirm `git status --short` was clean before producing handoff artifacts.
- [x] Confirm no secrets, plain-text stored user passwords, `.DS_Store`, `local.properties`, `.idea/`, `build/`, APKs, or unrelated screenshots are tracked.

Useful checks:

```bash
git status --short
git log --oneline --decorate --all
git ls-files
```

## Required Emulator Verification

- [x] Create/start a Pixel 3a XL emulator using API 28 and SwiftShader Software graphics.
- [x] Verify app label `1220216_1220071_CourseProject`, app logo, and launcher resources.
- [x] Verify splash timing/animation and Introduction wording.
- [x] Verify Connect failure/retry through instrumentation and successful live import of at least 10 trips.
- [x] Verify registration, wrong-password handling, successful user login, and Remember Me email behavior.
- [x] Verify all traveler drawer destinations and nested Back behavior.
- [x] Verify search/filter, details, favorite, reservation, My Reservations, and Special Section.
- [x] Verify profile field/password update and all three Contact Us actions. Profile-picture document-provider behavior remains device/provider dependent as noted in README.
- [x] Verify logout clears the session and Back cannot reopen the traveler shell.
- [x] Verify seeded admin login: `admin@admin.com` / `Admin123!`.
- [x] Verify the separate admin drawer destinations and role protection.
- [x] Record the final device/API/result in [progress-log.md](progress-log.md).

## Automated Checks

- [x] Run `./gradlew testDebugUnitTest` successfully.
- [x] Run `./gradlew lintDebug` successfully and review the report.
- [x] Run `./gradlew connectedDebugAndroidTest` successfully on the final emulator (10/10 passed, including Add New Admin role regression).
- [x] Run `./gradlew assembleDebug` successfully.

## APK Handoff

- [x] Confirm the debug APK exists at `app/build/outputs/apk/debug/app-debug.apk`.
- [x] Install that exact APK on the required emulator and launch it from cleared app data.
- [x] Copy the APK outside the repository to `/private/tmp/travel-planner-a20/app-debug.apk`.
- [x] Record the APK build date and checksum below.

APK build date: `2026-06-20`

APK SHA-256: `b3838c0a2116d88d934f63928afab1ba6cccb5fa0a51f43927dd1a14e4196c63`

## Project ZIP Handoff

- [x] Export a clean source archive equivalent to Android Studio's **Export to Zip File** operation.
- [x] Name the exported archive `Project.zip`.
- [x] Open the ZIP and confirm it contains the project source, Gradle wrapper, resources, and documentation.
- [x] Confirm the ZIP does not contain `.git/`, `.gradle/`, `build/`, `.idea/`, `local.properties`, APKs, emulator files, or machine-specific caches.
- [x] Extract the ZIP into a temporary directory and run a Gradle project task from the extracted copy.
- [x] Store `Project.zip` outside the Git working tree at `/private/tmp/travel-planner-a20/Project.zip`.

ZIP export date: `2026-06-20`

ZIP SHA-256: `_______________________________________________________________`

## Documentation and Discussion Readiness

- [x] README run instructions and known limitations match the final build.
- [ ] Screenshots, if included, come from the final Pixel 3a XL API 28 build.
- [ ] Aboud can explain the UI/theme, authentication, session, navigation, profile, contact, integration, and QA work.
- [ ] Lara can explain models, database tables/relationships, API parsing/import, trip features, reservations/favorites, special queries, and admin work.
- [ ] Both students can demonstrate the complete user and admin journeys without notes.
- [ ] Both students understand the known limitations and can distinguish course-project choices from production security practices.

## Final Files to Submit

- [x] Private GitHub repository link: `https://github.com/iAboud98/smart-activity-travel-management-system`
- [x] `Project.zip`
- [x] `app-debug.apk`
- [ ] Any report/screenshots explicitly required by the instructor.

Final sign-off:

- Student A: `____________________`  Date: `____________`
- Student B: `____________________`  Date: `____________`
