package com.example.myapplication;

import java.io.Serializable;

public class Room implements Serializable {
    private String id;
    private String name;
    private double price;
    private boolean isRented;
    private String tenantName;
    private String phoneNumber;
    private String birthDate;
    private String gender;
    private String address;
    private String rentalTime;

    public Room(String id, String name, double price, boolean isRented, String tenantName, String phoneNumber, String birthDate, String gender, String address, String rentalTime) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.isRented = isRented;
        this.tenantName = tenantName;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.rentalTime = rentalTime;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean isRented() { return isRented; }
    public void setRented(boolean rented) { isRented = rented; }

    public String getTenantName() { return tenantName; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getRentalTime() { return rentalTime; }
    public void setRentalTime(String rentalTime) { this.rentalTime = rentalTime; }
}
