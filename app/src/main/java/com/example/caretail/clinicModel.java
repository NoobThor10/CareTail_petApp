package com.example.caretail;

public class clinicModel {
    public String name;
    public String location;
    public String openingHours;
    public String imageUrl;

    public clinicModel() {
        // Default constructor required for calls to DataSnapshot.getValue(clinicModel.class)
    }

    public clinicModel(String name, String location, String openingHours, String imageUrl) {
        this.name = name;
        this.location = location;
        this.openingHours = openingHours;
        this.imageUrl = imageUrl;
    }
}
