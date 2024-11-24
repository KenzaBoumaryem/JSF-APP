package com.sakadream.jsf.func;

import com.sakadream.jsf.bean.Product;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class Functions implements Serializable {
    private Connection conn;


    private void connect() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try {
            String username = "root"; // Replace with your MySQL username
            String password = ""; // Replace with your MySQL password
            String url = "jdbc:mysql://localhost:3306/"; // Just connect to the host

            // Connect to MySQL
            conn = DriverManager.getConnection(url, username, password);


            // Create database if it doesn't exist
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS employeeDB");
            stmt.close();

            // Now connect to the specific database
            url = "jdbc:mysql://localhost:3306/employeeDB"; // Connect to the specific database
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            String username = "root"; // Replace with your local MySQL username
            String password = ""; // Replace with your local MySQL password
            String url = "jdbc:mysql://localhost:3306/"; // Just connect to the host

            // Connect to local MySQL
            conn = DriverManager.getConnection(url, username, password);

            // Create database if it doesn't exist
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS employeeDB");
            stmt.close();

            // Now connect to the specific local database
            url = "jdbc:mysql://localhost:3306/employeeDB"; // Connect to the specific local database
            conn = DriverManager.getConnection(url, username, password);
        }
    }


    private void cleanConnection() throws SQLException {
        conn.close();
    }

    public boolean checkLogin(String username, String password) throws SQLException, ClassNotFoundException {
        boolean b = true;

        connect();

        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM USERS WHERE USERNAME LIKE ? AND PASSWORD LIKE ?");
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);

        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()) setSessionUsername(username);
        else b = false;

        cleanConnection();

        return b;
    }

    public HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
    }

    private void setSessionUsername(String username) {
        HttpSession session = getSession();
        session.setAttribute("username", username);
    }

    public List<Product> showAllProducts() throws SQLException, ClassNotFoundException {
        List<Product> productList = new ArrayList<Product>();

        connect();

        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM PRODUCTS");
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Product product = new Product(resultSet.getInt("ID"), resultSet.getString("NAME"),
                    resultSet.getString("DESCRIPTION"), resultSet.getDouble("PRICE"),
                    resultSet.getInt("QUANTITY"));
            productList.add(product);
        }

        cleanConnection();

        return productList;
    }

    public Product getProductById(int id) throws SQLException, ClassNotFoundException {
        Product product = new Product();

        connect();

        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM PRODUCTS WHERE ID = ?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            product = new Product(resultSet.getInt("ID"), resultSet.getString("NAME"),
                    resultSet.getString("DESCRIPTION"), resultSet.getDouble("PRICE"),
                    resultSet.getInt("QUANTITY"));
        }

        cleanConnection();

        return product;
    }

    public void addProduct(String name, String description, double price, int quantity)
            throws SQLException, ClassNotFoundException {
        connect();

        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO PRODUCTS" +
                "(NAME, DESCRIPTION, PRICE, QUANTITY) " +
                "VALUES (?, ?, ?, ?)");
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, description);
        preparedStatement.setDouble(3, price);
        preparedStatement.setInt(4, quantity);
        preparedStatement.execute();

        cleanConnection();
    }

    public void editProduct(int id, String name, String description, double price, int quantity)
            throws SQLException, ClassNotFoundException {
        connect();

        PreparedStatement preparedStatement = conn.prepareStatement("UPDATE PRODUCTS " +
                "SET NAME = ?, DESCRIPTION = ?, PRICE = ?, QUANTITY = ? " +
                "WHERE ID = ?");
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, description);
        preparedStatement.setDouble(3, price);
        preparedStatement.setInt(4, quantity);
        preparedStatement.setInt(5, id);
        preparedStatement.execute();

        cleanConnection();
    }

    public void deleteProduct(int id) throws SQLException, ClassNotFoundException {
        connect();

        PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM PRODUCTS WHERE ID = ?");
        preparedStatement.setInt(1, id);
        preparedStatement.execute();

        cleanConnection();
    }

    public HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    public String getParameterByName(String name) {
        HttpServletRequest req = getRequest();
        return req.getParameter(name);
    }
}
