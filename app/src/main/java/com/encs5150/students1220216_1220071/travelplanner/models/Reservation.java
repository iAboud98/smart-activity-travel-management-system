package com.encs5150.students1220216_1220071.travelplanner.models;

public class Reservation {
    private int ID;
    private int userID;
    private int tripID;
    private int quantity;
    private String reservationType;
    private String reservationDate;
    private String status;
    private String additionalInfo;
    private String tripName; // for display only (not stored in database, fetched with join)

    public Reservation(int id, int userId, int tripId, int quantity, String reservationType,
                       String reservationDate, String status, String additionalInfo) {
        this.ID = id;
        this.userID = userId;
        this.tripID = tripId;
        this.quantity = quantity;
        this.reservationType = reservationType;
        this.reservationDate = reservationDate;
        this.status = status;
        this.additionalInfo = additionalInfo;
    }

    public Reservation() { }

    public int getId() {
        return ID;
    }

    public int getUserId() {
        return userID;
    }

    public int getTripId() {
        return tripID;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getReservationType() {
        return reservationType;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public String getStatus() {
        return status;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public String getTripName() { return tripName; }

    public void setUserId(int userId) {
        this.userID = userId;
    }

    public void setId(int id) {
        this.ID = id;
    }

    public void setTripId(int tripId) {
        this.tripID = tripId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setReservationType(String reservationType) { this.reservationType = reservationType; }

    public void setReservationDate(String reservationDate) { this.reservationDate = reservationDate; }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public void setTripName(String tripName) { this.tripName = tripName; }
}
