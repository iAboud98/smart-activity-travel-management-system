package com.encs5150.students1220216_1220071.travelplanner.models;

public class Favorite {
    private int ID;
    private int userID;
    private int tripID;

    public Favorite(int id, int userId, int tripId) {
        this.ID = id;
        this.userID = userId;
        this.tripID = tripId;
    }

    public Favorite() { }

    public int getId() {
        return ID;
    }

    public int getUserId() {
        return userID;
    }

    public int getTripId() {
        return tripID;
    }

    public void setId(int id) {
        this.ID = id;
    }

    public void setUserId(int userId) {
        this.userID = userId;
    }

    public void setTripId(int tripId) {
        this.tripID = tripId;
    }
}
