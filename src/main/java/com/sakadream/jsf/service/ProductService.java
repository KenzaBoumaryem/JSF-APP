package com.sakadream.jsf.service;

import com.sakadream.jsf.bean.Product;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ProductService {

    private DatabaseConnectionService dbService = new DatabaseConnectionService();

    // Afficher tous les produits
    public List<Product> showAllProducts() throws SQLException, ClassNotFoundException {
        List<Product> productList = new ArrayList<>();

        // Récupérer la connexion via DatabaseConnectionService
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
            throw e;  // Vous pouvez personnaliser cette exception ou la gérer autrement
        }

        return productList;
    }

    // Récupérer un produit par son ID
    public Product getProductById(int id) throws SQLException, ClassNotFoundException {
        Product product = null;

        // Récupérer la connexion via DatabaseConnectionService
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

        return product;
    }

    // Ajouter un nouveau produit
    public void addProduct(String name, String description, double price, int quantity)
            throws SQLException, ClassNotFoundException {
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
        try (Connection conn = dbService.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM PRODUCTS WHERE ID = ?")) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw e;
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
