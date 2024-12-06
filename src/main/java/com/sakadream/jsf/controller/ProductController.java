package com.sakadream.jsf.controller;

import com.sakadream.jsf.bean.Product;
import com.sakadream.jsf.service.ProductService;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

@Named("prod")
@SessionScoped
public class ProductController implements Serializable {

    @EJB
    private ProductService productService;


    // Afficher tous les produits
    public List<Product> showAllProducts() throws SQLException, ClassNotFoundException {
        return productService.showAllProducts();
    }

    // Récupérer un produit par son ID
    public Product getProduct() throws SQLException, ClassNotFoundException {
        String idStr = productService.getParameterByName("id");
        if (idStr == null || idStr.isEmpty()) {
            throw new IllegalArgumentException("Product ID is missing or invalid.");
        }
        try {
            int id = Integer.parseInt(idStr);
            return productService.getProductById(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Product ID must be a valid number.");
        }
    }
    public String addProduct() throws SQLException, ClassNotFoundException {
        String name = productService.getParameterByName("name");
        String description = productService.getParameterByName("description");
        double price = Double.valueOf(productService.getParameterByName("price"));
        int quantity = Integer.valueOf(productService.getParameterByName("quantity"));

        productService.addProduct(name, description, price, quantity);
        return "home";
    }


    // Supprimer un produit
    public String deleteProduct(String idStr) throws SQLException, ClassNotFoundException {
        int id = Integer.parseInt(idStr);
        productService.deleteProduct(id);
        return "home";
    }

    // Télécharger les informations d'un produit
    public void downloadProductInfo(Product product) {
        try {
            if (product == null || product.getId() <= 0) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur",
                                "Produit invalide pour le téléchargement."));
                return;
            }

            // Appeler le service pour générer le fichier JSON
            String filePath = productService.downloadProduct(product.getId());
            File file = new File(filePath);

            if (file.exists()) {
                HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance()
                        .getExternalContext().getResponse();

                response.setContentType("application/json");
                response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
                response.setContentLength((int) file.length());

                try (FileInputStream fileInputStream = new FileInputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;

                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        response.getOutputStream().write(buffer, 0, bytesRead);
                    }

                    response.getOutputStream().flush();
                }

                FacesContext.getCurrentInstance().responseComplete();
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur",
                                "Fichier introuvable pour le produit."));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur",
                            "Impossible de télécharger le produit : " + e.getMessage()));
            e.printStackTrace();
        }
    }

    private int productId;
    private String name;
    private String description;
    private double price;
    private int quantity;

    // Getters and setters pour tous les champs
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    // Méthode appelée avant le rendu de la page d'édition
    public void loadProduct() {
        try {
            if (productId > 0) {
                Product product = productService.getProductById(productId);
                if (product != null) {
                    // Charger les données du produit dans les champs du contrôleur
                    this.name = product.getName();
                    this.description = product.getDescription();
                    this.price = product.getPrice();
                    this.quantity = product.getQuantity();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur",
                            "Impossible de charger le produit."));
        }
    }

    public String editProduct() {
        try {
            productService.editProduct(productId, name, description, price, quantity);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès",
                            "Produit modifié avec succès."));
            return "home?faces-redirect=true";
        } catch (SQLException | ClassNotFoundException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur",
                            "Impossible de modifier le produit."));
            return null;
        }
    }



}