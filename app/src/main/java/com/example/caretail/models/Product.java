package com.example.caretail.models;

public class Product {
    private String name;
    private String description;
    private String price;
    private int imageResource;
    private float rating;

    public Product(String name, String description, String price, int imageResource, float rating) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResource = imageResource;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResource() {
        return imageResource;
    }

    public float getRating() {
        return rating;
    }
}
