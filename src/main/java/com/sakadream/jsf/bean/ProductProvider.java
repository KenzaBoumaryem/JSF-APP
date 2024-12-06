package com.sakadream.jsf.bean;


import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "product_provider") // Table correspondante
public class ProductProvider implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Génération automatique de l'ID
    private int id;

    @Column(nullable = false) // Champ obligatoire
    private String name;

    @Column(length = 500) // Longueur maximale pour la description
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne // Relation Many-to-One avec Provider
    @JoinColumn(name = "provider_id", nullable = false) // Clé étrangère
    private Provider provider;

    // Getters et Setters
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

