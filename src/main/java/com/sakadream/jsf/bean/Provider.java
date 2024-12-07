package com.sakadream.jsf.bean;

import java.io.Serializable;
import java.util.List;

public class Provider implements Serializable {

    private Long id;

    private String name;
    private List<ProductProvider> products;

    // Constructeurs
    public Provider() {}

    public Provider(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters et Setters
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
