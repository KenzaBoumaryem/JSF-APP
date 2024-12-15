package com.sakadream.jsf.bean;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Provider implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // Specifies the column name
    private Long id;

    @Column(name = "name", nullable = false) // Ensure the name is not null
    private String name;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductProvider> products; // One Provider can have many ProductProviders

    // Constructors
    public Provider() {}

    public Provider(String name) {
        this.name = name;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductProvider> getProducts() {
        return products;
    }

    public void setProducts(List<ProductProvider> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Provider{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
