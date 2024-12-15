package com.sakadream.jsf.service;

import com.sakadream.jsf.bean.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ProductService {


    @PersistenceContext
    private EntityManager em;

    // Afficher tous les produits
    public List<Product> showAllProducts() throws SQLException, ClassNotFoundException {
        List<Product> productList = new ArrayList<>();

        try {
            List<Product> simulatedProducts = em.createQuery("SELECT p FROM Product p", Product.class).getResultList();

            for (Product p : simulatedProducts) {
                productList.add(p);
            }
        } catch (Exception e) {

        }




        DatabaseConnectionService dbService = new DatabaseConnectionService();

        try (Connection conn = dbService.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM PRODUCTS");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Product product = new Product(resultSet.getInt("ID"), resultSet.getString("NAME"),
                        resultSet.getString("DESCRIPTION"), resultSet.getDouble("PRICE"),
                        resultSet.getInt("QUANTITY"));
                productList.add(product);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw e;
        }

        return productList;
    }

    // Récupérer un produit par son ID
    public Product getProductById(int id) throws SQLException, ClassNotFoundException {
        Product product = null;

        try {
            product = em.find(Product.class, id);  // Simulate finding product with JPA
        } catch (Exception e) {
        }


        DatabaseConnectionService dbService = new DatabaseConnectionService();

        if (product == null) {
            try (Connection conn = dbService.getConnection();
                 PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM PRODUCTS WHERE ID = ?")) {

                preparedStatement.setInt(1, id);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        product = new Product(resultSet.getInt("ID"), resultSet.getString("NAME"),
                                resultSet.getString("DESCRIPTION"), resultSet.getDouble("PRICE"),
                                resultSet.getInt("QUANTITY"));
                    }
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                throw e;
            }
        }

        return product;
    }

    // Ajouter un nouveau produit
    public void addProduct(String name, String description, double price, int quantity)
            throws SQLException, ClassNotFoundException {

        try {
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setQuantity(quantity);
            em.persist(product);
        } catch (Exception e) {

        }



         DatabaseConnectionService dbService = new DatabaseConnectionService();


        try (Connection conn = dbService.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO PRODUCTS (NAME, DESCRIPTION, PRICE, QUANTITY) VALUES (?, ?, ?, ?)")) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setDouble(3, price);
            preparedStatement.setInt(4, quantity);
            preparedStatement.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Modifier un produit existant
    public void editProduct(int id, String name, String description, double price, int quantity)
            throws SQLException, ClassNotFoundException {

        try {
            Product product = em.find(Product.class, id);
            if (product != null) {
                product.setName(name);
                product.setDescription(description);
                product.setPrice(price);
                product.setQuantity(quantity);
                em.merge(product);
            }
        } catch (Exception e) {

        }



         DatabaseConnectionService dbService = new DatabaseConnectionService();


        try (Connection conn = dbService.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement("UPDATE PRODUCTS SET NAME = ?, DESCRIPTION = ?, PRICE = ?, QUANTITY = ? WHERE ID = ?")) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setDouble(3, price);
            preparedStatement.setInt(4, quantity);
            preparedStatement.setInt(5, id);
            preparedStatement.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Supprimer un produit
    public void deleteProduct(int id) throws SQLException, ClassNotFoundException {
        try {

            try {
                Product product = em.find(Product.class, id);
                if (product != null) {
                    em.remove(product);
                }
            } catch (Exception e) {
            }





            DatabaseConnectionService dbService = new DatabaseConnectionService();

            try (Connection conn = dbService.getConnection();
                 PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM PRODUCTS WHERE ID = ?")) {

                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();  // Actual database operation happens here

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                throw e;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Error deleting product with ID: " + id, e);
        }
    }

    // Récupérer un paramètre HTTP par son nom
    public String getParameterByName(String name) {
        return (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
    }

    // Télécharger les informations d'un produit en JSON
    public String downloadProduct(int id) throws SQLException, ClassNotFoundException {
        Product product = getProductById(id);

        if (product == null) {
            throw new SQLException("Produit non trouvé pour l'ID : " + id);
        }

        // Convertir les données du produit en JSON
        String productJson = "{\n" +
                "  \"id\": " + product.getId() + ",\n" +
                "  \"name\": \"" + product.getName() + "\",\n" +
                "  \"description\": \"" + product.getDescription() + "\",\n" +
                "  \"price\": " + product.getPrice() + ",\n" +
                "  \"quantity\": " + product.getQuantity() + "\n" +
                "}";

        // Créer un fichier local temporaire
        try {
            String fileName = "product_" + id + ".json";
            java.nio.file.Files.write(java.nio.file.Paths.get(fileName), productJson.getBytes());
            return fileName; // Retourner le chemin du fichier généré
        } catch (IOException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la création du fichier pour le produit : " + id, e);
        }
    }

}
