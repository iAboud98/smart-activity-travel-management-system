# Connect Button Guide for Student A

## Overview
The Connect button fetches trips from the REST API and stores them in the local database.

## API Endpoint
```
https://mocki.io/v1/9febcb0b-b3f6-493a-8e78-714a28fa676e
```
Expected trips after successful import: 14

## How to Use

### Step 1 - Add internet permission to AndroidManifest.xml
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### Step 2 - Create IntroductionActivity and add onTripsFetched method
```java
public void onTripsFetched(List<Trip> trips) {
    if (trips == null) {
        Toast.makeText(this, "Failed to fetch trips. Please check your connection.", Toast.LENGTH_SHORT).show();
        // stay on Introduction screen
    } else {
        TripRepository tripRepo = new TripRepository(this);
        tripRepo.importTrips(trips);
        // navigate to Login/Register screen
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
```

### Step 3 - Call ConnectionAsyncTask in the Connect button click listener
```java
new ConnectionAsyncTask(IntroductionActivity.this).execute();
```

## Expected Behavior

| Scenario | Result |
|---|---|
| API reachable | onTripsFetched fires with trips, saved to database, navigate to login |
| No internet | onTripsFetched fires with null, Toast shown, stay on screen |
| API unreachable | onTripsFetched fires with null, Toast shown, stay on screen |
| Duplicate import | Existing trips skipped, no duplicates in database |

---

## Note for Student B
After Student A finishes IntroductionActivity, add onPreExecute to ConnectionAsyncTask
to show a loading indicator while the API is being fetched. For example, disable the
Connect button or show a ProgressBar while the request is in progress.
