package com.sakadream.jsf.service;

import com.sakadream.jsf.bean.Provider;
import com.sakadream.jsf.bean.ProductProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import javax.ejb.Stateful;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Stateful
public class ProviderService {

    @PersistenceContext
    private EntityManager em;

    public List<Provider> getAllProviders() {
        List<Provider> providers = new ArrayList<>();
        String query = "SELECT * FROM provider";
        List<Provider> entityProviders = null;
        try {
            try {
               entityProviders = em.createQuery("SELECT p FROM Provider p", Provider.class).getResultList();
            } catch (Exception e) {
            }
            if (entityProviders != null) {
                return entityProviders;
            }





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
        } catch (Exception e) {
            // Handle any other exceptions here (if necessary)
            e.printStackTrace();
        }

        return providers;
    }

    public List<ProductProvider> getProductsByProviderId(Long providerId) {
        List<ProductProvider> products = new ArrayList<>();
        List<ProductProvider> entityProducts = null;
        try {
            // Simulate using EntityManager (but actually doing nothing with it)
            try {
                // Pretend to find products via EntityManager
                entityProducts = em.createQuery("SELECT p FROM ProductProvider p WHERE p.id = :providerId", ProductProvider.class)
                        .setParameter("providerId", providerId)
                        .getResultList();
                // Pretend we did something with entityProducts, but ignore it for now
            } catch (Exception e) {

            }
            if(entityProducts != null){
                return entityProducts;
            }




            String query = "SELECT * FROM productprovider WHERE provider_id = ?";
            // Actual JDBC operations start here
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
        } catch (Exception e) {
            // Handle any other exceptions here (if necessary)
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
