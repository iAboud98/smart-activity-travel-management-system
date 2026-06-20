# Final Submission Checklist

Complete this checklist during A20. Do not commit generated APK, ZIP, build, IDE, or emulator files unless the instructor explicitly requests them in Git.

## Repository

- [ ] Confirm the repository is private: [smart-activity-travel-management-system](https://github.com/iAboud98/smart-activity-travel-management-system).
- [ ] Confirm both students remain repository collaborators.
- [ ] Pull/merge the final approved work and confirm no unresolved conflicts.
- [ ] Confirm the history shows regular, meaningful feature commits rather than a one-time upload.
- [ ] Confirm `git status --short` is clean before producing handoff artifacts.
- [ ] Confirm no secrets, plain-text stored user passwords, `.DS_Store`, `local.properties`, `.idea/`, `build/`, APKs, or unrelated screenshots are tracked.

Useful checks:

```bash
git status --short
git log --oneline --decorate --all
git ls-files
```

## Required Emulator Verification

- [ ] Create/start a Pixel 3a XL emulator using API 28 and Software graphics.
- [ ] Verify app label `1220216_1220071_CourseProject` and launcher icon.
- [ ] Verify splash timing/animation and Introduction wording.
- [ ] Verify Connect failure/retry and successful import of at least 10 trips.
- [ ] Verify registration, wrong-password handling, successful user login, and Remember Me email behavior.
- [ ] Verify all traveler drawer destinations and nested Back behavior.
- [ ] Verify search/filter, details, favorite, reservation, My Reservations, and Special Section.
- [ ] Verify profile update, profile picture selection, and all three Contact Us actions.
- [ ] Verify logout clears the session and Back cannot reopen the traveler shell.
- [ ] Verify seeded admin login: `admin@admin.com` / `Admin123!`.
- [ ] Verify every admin drawer destination and role protection.
- [ ] Record the final device/API/result in [progress-log.md](progress-log.md).

## Automated Checks

- [ ] Run `./gradlew testDebugUnitTest` successfully.
- [ ] Run `./gradlew lintDebug` successfully and review the report.
- [ ] Run `./gradlew connectedDebugAndroidTest` successfully on the final emulator.
- [ ] Run `./gradlew assembleDebug` successfully.

## APK Handoff

- [ ] Confirm the debug APK exists at `app/build/outputs/apk/debug/app-debug.apk`.
- [ ] Install that exact APK on the required emulator and launch it once.
- [ ] Copy the APK outside the repository for submission if the course portal requires a separate upload.
- [ ] Record the APK build date and optional checksum below.

APK build date: `____________________`

APK SHA-256: `_______________________________________________________________`

## Project ZIP Handoff

- [ ] In Android Studio, use **File → Export to Zip File** (wording may vary by version).
- [ ] Name the exported archive `Project.zip`.
- [ ] Open the ZIP and confirm it contains the project source, Gradle wrapper, resources, and documentation.
- [ ] Confirm the ZIP does not contain `.git/`, `.gradle/`, `build/`, `.idea/`, `local.properties`, APKs, emulator files, or machine-specific caches.
- [ ] Extract the ZIP into a temporary directory and confirm Android Studio can open/sync it.
- [ ] Store `Project.zip` outside the Git working tree unless the instructor explicitly says otherwise.

ZIP export date: `____________________`

ZIP SHA-256: `_______________________________________________________________`

## Documentation and Discussion Readiness

- [ ] README run instructions and known limitations match the final build.
- [ ] Screenshots, if included, come from the final Pixel 3a XL API 28 build.
- [ ] Aboud can explain the UI/theme, authentication, session, navigation, profile, contact, integration, and QA work.
- [ ] Lara can explain models, database tables/relationships, API parsing/import, trip features, reservations/favorites, special queries, and admin work.
- [ ] Both students can demonstrate the complete user and admin journeys without notes.
- [ ] Both students understand the known limitations and can distinguish course-project choices from production security practices.

## Final Files to Submit

- [ ] Private GitHub repository link: `https://github.com/iAboud98/smart-activity-travel-management-system`
- [ ] `Project.zip`
- [ ] `app-debug.apk`
- [ ] Any report/screenshots explicitly required by the instructor.

Final sign-off:

- Student A: `____________________`  Date: `____________`
- Student B: `____________________`  Date: `____________`
