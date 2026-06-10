package com.encs5150.students1220216_1220071.travelplanner.models;

public class Trip {
    private int ID;  // database primary key assigned on insert
    private int apiID;  // id from the REST API response, used to prevent duplicate imports
    private String destination;
    private String country;
    private int durationDays;
    private double price;
    private double rating;
    private String description;
    private String imageUrl;
    private int isActive; // 1 = active, 0 = deleted

    public Trip() {
    }

    public Trip(int ID, int apiID, String destination, String country, int durationDays, double price,
                double rating, String description, String imageUrl, int isActive) {
        this.ID = ID;
        this.apiID = apiID;
        this.destination = destination;
        this.country = country;
        this.durationDays = durationDays;
        this.price = price;
        this.rating = rating;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isActive = isActive;
    }

    public int getID() {
        return ID;
    }

    public int getApiID() {
        return apiID;
    }

    public String getDestination() {
        return destination;
    }

    public String getCountry() {
        return country;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public double getPrice() {
        return price;
    }

    public double getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setApiID(int apiID) {
        this.apiID = apiID;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }
}
