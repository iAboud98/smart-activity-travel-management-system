# Travel Planner App — Database, API, and Admin Documentation

## 1. Database Tables

### 1.1 Users Table
**Purpose:** Stores all user and admin accounts.

| Column | Type | Notes |
|---|---|---|
| id | INTEGER | Primary key, auto-incremented by SQLite |
| email | TEXT | Unique, prevents duplicate accounts |
| first_name | TEXT | Minimum 3 characters |
| last_name | TEXT | Minimum 3 characters |
| password | TEXT | Stored as SHA-256 hash, never plain text |
| gender | TEXT | Selected from spinner (Male/Female) |
| category | TEXT | Travel preference selected from spinner |
| phone | TEXT | Validated phone number |
| profile_picture | TEXT | Persisted profile image path or document URI |
| role | TEXT | "user" or "admin" |
| is_active | INTEGER | 1 = active, 0 = soft deleted |

### 1.2 Trips Table
**Purpose:** Stores all trips imported from the API or added by admin.

| Column | Type | Notes |
|---|---|---|
| id | INTEGER | Primary key, auto-incremented by SQLite |
| api_id | INTEGER | Unique ID from the REST API, prevents duplicate imports |
| destination | TEXT | Trip destination name |
| country | TEXT | Country of destination |
| duration_days | INTEGER | Number of days for the trip |
| price | REAL | Trip price in USD |
| rating | REAL | Trip rating from 0.0 to 5.0 |
| description | TEXT | Trip description |
| image_url | TEXT | URL to trip image |
| is_active | INTEGER | 1 = active, 0 = soft deleted by admin |

### 1.3 Favorites Table
**Purpose:** Stores which trips each user has favorited.

| Column | Type | Notes |
|---|---|---|
| id | INTEGER | Primary key, auto-incremented |
| user_id | INTEGER | References users.id |
| trip_id | INTEGER | References trips.id |
| UNIQUE(user_id, trip_id) | | Prevents duplicate favorites |

### 1.4 Reservations Table
**Purpose:** Stores all trip reservations made by users.

| Column | Type | Notes |
|---|---|---|
| id | INTEGER | Primary key, auto-incremented |
| user_id | INTEGER | References users.id |
| trip_id | INTEGER | References trips.id |
| quantity | INTEGER | Number of travelers |
| reservation_type | TEXT | Solo / Couple / Family / Group |
| reservation_date | TEXT | Date reservation was made (yyyy-MM-dd) |
| status | TEXT | "Confirmed" by default |
| additional_info | TEXT | Optional notes from user |

---

## 2. Table Relationships

```
USERS (1) ----< FAVORITES >---- (N) TRIPS
USERS (1) ----< RESERVATIONS >---- (N) TRIPS
```

- One user can have many favorites
- One trip can be favorited by many users
- One user can have many reservations
- One trip can have many reservations

---

## 3. Admin Account Seeding

The admin account is created automatically when the database is first created in `DatabaseHelper.onCreate()`:

- **Email:** admin@admin.com
- **Password:** Admin123! (stored as SHA-256 hash)
- **Role:** admin
- **is_active:** 1

This ensures an admin account always exists on fresh install.

---

## 4. Password Security

Passwords are hashed using **SHA-256** before being stored in the database.

- SHA-256 is a one-way hashing algorithm — the original password cannot be recovered from the hash
- When a user logs in, the entered password is hashed and compared to the stored hash
- This means even if the database is accessed directly, passwords are not visible as plain text
- The `hashPassword()` method in `DatabaseHelper.java` handles all hashing

---

## 5. REST API

**Endpoint:** https://mocki.io/v1/9febcb0b-b3f6-493a-8e78-714a28fa676e

**Type:** GET request, returns a JSON array of trip objects

**Trip JSON Shape:**
```json
{
    "id": 101,
    "destination": "Istanbul",
    "country": "Turkey",
    "duration_days": 5,
    "price": 750,
    "rating": 4.7,
    "description": "Explore the historic streets of Istanbul...",
    "image": "https://images.unsplash.com/..."
}
```

**Total trips in API:** 14

---

## 6. Import Flow (Connect Button to Database)

1. User opens the app for the first time and sees the Introduction screen
2. User taps **Connect** button
3. `ConnectionAsyncTask.doInBackground()` runs on a background thread and calls `HttpManager.getData()` to fetch the API URL
4. `HttpManager` opens an `HttpURLConnection`, reads the response as a string
5. `TripJsonParser.getTripsFromJson()` parses the JSON string into a `List<Trip>`
6. Each trip object is validated for required fields and sensible duration, price, and rating values; one invalid item is skipped without discarding later valid items
7. `ConnectionAsyncTask.onPostExecute()` runs on the main thread and calls `onTripsFetched()` in `IntroductionActivity`
8. `TripRepository.importTrips()` loops through the list and calls `insertTrip()` for each trip
9. `insertTrip()` uses `api_id` as a unique key — if the same remote trip already exists, its stored values are refreshed rather than duplicated
10. After import succeeds, user is navigated to the Login screen

---

## 7. Special Section Rules

The Special Section shows trips based on automatic rules — no manual flagging needed:

| Section | Rule |
|---|---|
| Travel Offers | rating >= 4.5 |
| Popular Destinations | trips with 5 or more total reservations |
| Recommended Trips | trips with 2 or more reservations in the last 7 days |

---

## 8. Deletion Behavior

### User Deletion (Admin)
- Uses **soft delete** — sets `is_active = 0`
- User data, reservations, and favorites are kept in the database
- Deleted user cannot log in
- Re-registering the same email restores and updates the inactive row, preserving its ID and relationships while applying the new password and requested user/admin role
- Admin cannot delete their own account

### Trip Deletion (Admin)
- Uses **soft delete** — sets `is_active = 0`
- Existing reservations for that trip are kept
- `getAllTrips()` filters `is_active = 1` so deleted trips do not appear in user-facing screens
- User-facing screens do not crash if a trip is deleted

---

## 9. Repository Classes

| Repository | Purpose |
|---|---|
| UserRepository | Register, login, update profile, delete users, list users |
| TripRepository | Insert/update/delete trips, search, filter, special section queries |
| FavoriteRepository | Add/remove favorites, check favorite state, list favorites |
| ReservationRepository | Create reservations, list by user, list all for admin |
