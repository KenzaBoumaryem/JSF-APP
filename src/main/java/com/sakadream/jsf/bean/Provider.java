package com.sakadream.jsf.bean;

import jakarta.persistence.*; // Utilisation de Jakarta Persistence API
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "provider") // Table correspondante
public class Provider implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Génération automatique de l'ID
    private Long id;

    @Column(nullable = false, unique = true) // Nom obligatoire et unique
    private String name;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, orphanRemoval = true) // Relation One-to-Many
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
