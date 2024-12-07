package com.sakadream.jsf.service;

import com.sakadream.jsf.bean.Provider;
import com.sakadream.jsf.bean.ProductProvider;

import javax.ejb.Stateful;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Stateful
public class ProviderService {

    public List<Provider> getAllProviders() {
        List<Provider> providers = new ArrayList<>();
        String query = "SELECT * FROM provider";

        try (Connection connection = DatabaseConnectionService.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                Provider provider = new Provider();
                provider.setId(rs.getLong("id"));
                provider.setName(rs.getString("name"));
                provider.setProducts(getProductsByProviderId(rs.getLong("id")));
                providers.add(provider);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return providers;
    }

    public List<ProductProvider> getProductsByProviderId(Long providerId) {
        List<ProductProvider> products = new ArrayList<>();
        String query = "SELECT * FROM product_provider WHERE provider_id = ?";

        try (Connection connection = DatabaseConnectionService.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setLong(1, providerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductProvider product = new ProductProvider();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));
                    product.setQuantity(rs.getInt("quantity"));
                    products.add(product);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return products;
    }

    public void addProductsToDatabase(List<ProductProvider> products) {
        String updateQuery = "UPDATE product_selected SET quantity = ? WHERE id = ?";
        String insertQuery = "INSERT INTO product_selected (id, name, description, price, quantity) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnectionService.getConnection()) {
            for (ProductProvider product : products) {
                try (PreparedStatement psUpdate = connection.prepareStatement(updateQuery)) {
                    psUpdate.setInt(1, product.getQuantity());
                    psUpdate.setInt(2, product.getId());
                    int rowsUpdated = psUpdate.executeUpdate();

                    if (rowsUpdated == 0) {
                        try (PreparedStatement psInsert = connection.prepareStatement(insertQuery)) {
                            psInsert.setInt(1, product.getId());
                            psInsert.setString(2, product.getName());
                            psInsert.setString(3, product.getDescription());
                            psInsert.setDouble(4, product.getPrice());
                            psInsert.setInt(5, product.getQuantity());
                            psInsert.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
