package com.sakadream.jsf.func;

import com.sakadream.jsf.bean.Product;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phan Ba Hai on 17/07/2017.
 */
public class Functions {
    private Connection conn;

    private void connect() throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        try {
            String username = "sa";
            String password = "kniza123##@";
            String url = "jdbc:sqlserver://sakadream-sof305.database.windows.net:1433;databaseName=SOF305";

            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            String username = "sa";
            String password = "kniza123##@";
            String url = "jdbc:sqlserver://localhost:1433;databaseName=SOF305_Offline";

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
