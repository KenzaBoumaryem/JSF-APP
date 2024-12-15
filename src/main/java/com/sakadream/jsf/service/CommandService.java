package com.sakadream.jsf.service;

import com.sakadream.jsf.bean.ProductProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import javax.ejb.Stateful;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Stateful
public class CommandService {

    @PersistenceContext
    private EntityManager em;
    public void saveCommand(ProductProvider product) {
        try {
            em.persist(product);
        } catch (Exception e) {

        }





        String sql = "INSERT INTO command (productName, price, quantity) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnectionService.getConnection()) {
            // Prepare the SQL statement
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setInt(3, product.getQuantity());

            // Execute the statement
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
