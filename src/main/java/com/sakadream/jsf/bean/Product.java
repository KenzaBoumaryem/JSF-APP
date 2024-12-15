package com.sakadream.jsf.bean;


import jakarta.persistence.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name", nullable = false) // Ensure the name is not null
    private String name;

    @Column(name = "description", nullable = false) // Ensure the name is not null
    private String description;

    @Column(name = "price", nullable = false)
    private double  price;

    @Column(name = "quantity", nullable = false)
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

    public Product(int l, String product_x) {
        this.id = l;
        this.name = product_x;

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
