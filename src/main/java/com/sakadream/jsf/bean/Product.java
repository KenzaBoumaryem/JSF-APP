package com.sakadream.jsf.bean;

/**
 * Created by Phan Ba Hai on 17/07/2017.
 */
public class Product {
    private int id;
    private String name;
    private String description;
    private double  price;
    private int quantity;

    public Product() {
        this.id = 0;
        this.name = "";
        this.description = "";
        this.price = 0.0;
        this.quantity = 0;
    }

    public Product(String name, String description, double price, int quantity) {
        this.id = 0;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public Product(int id, String name, String description, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
