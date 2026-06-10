package com.encs5150.students1220216_1220071.travelplanner.models;

public class User {
    private int ID;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String gender;
    private String category;
    private String phone;
    private String profilePicturePath;
    private String role; // "user" or "admin"

    public User(int id, String email, String firstName, String lastName, String password, String gender,
                String category, String phone, String profilePicturePath, String role) {
        this.ID = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.gender = gender;
        this.category = category;
        this.phone = phone;
        this.profilePicturePath = profilePicturePath;
        this.role = role;
    }

    public User() {
    }

    public int getId() {
        return ID;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getCategory() {
        return category;
    }

    public String getPhone() {
        return phone;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public String getRole() {
        return role;
    }

    public void setId(int id) {
        this.ID = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
