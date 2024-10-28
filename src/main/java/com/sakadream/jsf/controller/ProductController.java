package com.sakadream.jsf.controller;

import com.sakadream.jsf.bean.Product;
import com.sakadream.jsf.func.Functions;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
 * ProductController for managing products.
 */
@ManagedBean(name = "prod", eager = true)
@RequestScoped
public class ProductController implements Serializable {

    private Functions func = new Functions();

    // Show all products
    public List<Product> showAllProducts() throws SQLException, ClassNotFoundException {
        return func.showAllProducts(); // Ensure this method exists in Functions
    }

    // Get a specific product by ID
    public Product getProduct() throws SQLException, ClassNotFoundException {
        String idStr = func.getParameterByName("id");
        int id;
        try {
            id = Integer.valueOf(idStr);
        } catch (NumberFormatException e) {
            id = 0; // Default to 0 if parsing fails
        }
        return func.getProductById(id); // Ensure this method exists in Functions
    }

    // Add a new product
    public String addProduct() throws SQLException, ClassNotFoundException {
        String name = func.getParameterByName("name"); // Assuming Product has a name field
        String description = func.getParameterByName("description"); // Assuming Product has a description field
        String priceStr = func.getParameterByName("price");
        double price = Double.valueOf(priceStr); // Assuming Product has a price field
        String quantityStr = func.getParameterByName("quantity");
        int quantity = Integer.valueOf(quantityStr); // Assuming Product has a quantity field

        func.addProduct(name, description, price, quantity); // Ensure this method exists in Functions
        return "home"; // Redirect after adding
    }

    // Edit an existing product
    public void editProduct() throws SQLException, ClassNotFoundException, IOException {
        String idStr = func.getParameterByName("editForm:id");
        int id = Integer.valueOf(idStr);
        String name = func.getParameterByName("name");
        String description = func.getParameterByName("description");
        String priceStr = func.getParameterByName("price");
        double price = Double.valueOf(priceStr);
        String quantityStr = func.getParameterByName("quantity");
        int quantity = Integer.valueOf(quantityStr);

        func.editProduct(id, name, description, price, quantity); // Ensure this method exists in Functions

        FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml"); // Redirect to home.xhtml
    }

    // Delete a product
    public String deleteProduct(String idStr) throws SQLException, ClassNotFoundException {
        int id = Integer.valueOf(idStr);

        func.deleteProduct(id); // Ensure this method exists in Functions
        return "home"; // Redirect after deletion
    }
}
