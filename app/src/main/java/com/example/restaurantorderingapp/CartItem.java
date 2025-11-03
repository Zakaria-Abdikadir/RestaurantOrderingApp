package com.example.restaurantorderingapp;

public class CartItem {
    private String name;
    private String imageResource;
    private double price;
    private int quantity;
    private String type; // "food" or "drink"

    public CartItem(String name, String imageResource, double price, int quantity, String type) {
        this.name = name;
        this.imageResource = imageResource;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getTotalPrice() {
        return price * quantity;
    }
}

