package com.encs5150.students1220216_1220071.travelplanner.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.encs5150.students1220216_1220071.travelplanner.database.DatabaseHelper;
import com.encs5150.students1220216_1220071.travelplanner.models.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationRepository {

    private DatabaseHelper dbHelper;

    public ReservationRepository(Context context) {
        dbHelper = new DatabaseHelper(context, "travel_planner.db", null, 1);
    }

    // create a new reservation
    public boolean createReservation(Reservation reservation) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("user_id", reservation.getUserId());
            values.put("trip_id", reservation.getTripId());
            values.put("quantity", reservation.getQuantity());
            values.put("reservation_type", reservation.getReservationType());
            values.put("reservation_date", reservation.getReservationDate());
            values.put("status", reservation.getStatus());
            values.put("additional_info", reservation.getAdditionalInfo());
            long result = db.insert("reservations", null, values);
            return result != -1;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // list all reservations for a specific user with trip destination name
    public List<Reservation> getReservationsByUser(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Reservation> reservations = new ArrayList<>();
        Cursor cursor = db.rawQuery("select reservations.*, trips.destination from reservations " +
                        "join trips on reservations.trip_id = trips.id " +
                        "where reservations.user_id = " + userId, null);
        while (cursor.moveToNext()) {
            reservations.add(cursorToReservation(cursor));
        }
        cursor.close();
        return reservations;
    }

    // list all reservations for admin view with trip name and user email
    public List<Reservation> getAllReservations() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Reservation> reservations = new ArrayList<>();
        Cursor cursor = db.rawQuery(
                "select reservations.*, trips.destination, users.email from reservations " +
                        "join trips on reservations.trip_id = trips.id " +
                        "join users on reservations.user_id = users.id", null);
        while (cursor.moveToNext()) {
            Reservation reservation = new Reservation();
            reservation.setId(cursor.getInt(0));
            reservation.setUserId(cursor.getInt(1));
            reservation.setTripId(cursor.getInt(2));
            reservation.setQuantity(cursor.getInt(3));
            reservation.setReservationType(cursor.getString(4));
            reservation.setReservationDate(cursor.getString(5));
            reservation.setStatus(cursor.getString(6));
            reservation.setAdditionalInfo(cursor.getString(7));
            reservation.setTripName(cursor.getString(8)); // from join with trips
            reservation.setUserEmail(cursor.getString(9)); // from join with users
            reservations.add(reservation);
        }
        cursor.close();
        return reservations;
    }

    // helper to convert a cursor row to a reservation object
    private Reservation cursorToReservation(Cursor cursor) {
        Reservation reservation = new Reservation();
        reservation.setId(cursor.getInt(0));
        reservation.setUserId(cursor.getInt(1));
        reservation.setTripId(cursor.getInt(2));
        reservation.setQuantity(cursor.getInt(3));
        reservation.setReservationType(cursor.getString(4));
        reservation.setReservationDate(cursor.getString(5));
        reservation.setStatus(cursor.getString(6));
        reservation.setAdditionalInfo(cursor.getString(7));
        reservation.setTripName(cursor.getString(8)); // from join with trips
        return reservation;
    }

    // cancel a reservation by setting status to cancelled
    public boolean cancelReservation(int reservationId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", "Cancelled");
        int rows = db.update("reservations", values, "id = " + reservationId, null);
        return rows > 0;
    }
}
