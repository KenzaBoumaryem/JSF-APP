package com.sakadream.jsf.bean;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class ProductProvider implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // Specify the column name
    private int id;

    @Column(name = "name", nullable = false) // Ensure the name is not null
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY) // Many ProductProviders can belong to one Provider
    @JoinColumn(name = "provider_id", nullable = false) // Foreign key to the Provider table
    private Provider provider;

    // Constructors
    public ProductProvider() {}


    public ProductProvider(Long id, String name, String description, Double price, Integer quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public ProductProvider(long id, String name, String description, double price, Integer quantity, Provider selectedProvider) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.provider=selectedProvider;
    }

    // Getters and Setters
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

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    @Override
    public String toString() {
        return "ProductProvider{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", provider=" + (provider != null ? provider.toString() : "null") +
                '}';
    }
}
