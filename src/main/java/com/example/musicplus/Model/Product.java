package com.example.musicplus.Model;

import java.io.Serializable;

public class Product implements Serializable {
    private int stock;
    private String name;
    private String category;
    private double price;
    private String description;

    public Product(int stock, String name, String category, double price, String description) {
        this.stock = stock;
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
    }
    public void reduceStock(int quantity) {
        if (this.stock >= quantity) {
            this.stock -= quantity;
        } else {
            // Handle insufficient stock (optional)
            System.out.println("Insufficient stock for " + this.name);
        }
    }
    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}