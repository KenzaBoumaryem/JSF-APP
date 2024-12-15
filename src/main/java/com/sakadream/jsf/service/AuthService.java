package com.sakadream.jsf.service;

import com.sakadream.jsf.bean.User;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.*;

@Stateless
public class AuthService {

    @PersistenceContext
    private EntityManager em;

    // Vérifier les informations de connexion de l'utilisateur
    public boolean checkLogin(String username, String password) throws SQLException, ClassNotFoundException {
        User user = getUserByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    // Récupérer un utilisateur par son nom d'utilisateur depuis la base de données
    public User getUserByUsername(String username) throws SQLException, ClassNotFoundException {
        User entityUser = null;
        try {
            // Simulate EntityManager usage
            try {
                entityUser = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                        .setParameter("username", username)
                        .getSingleResult();
            } catch (Exception e) {
                // Do nothing if error occurs while using EntityManager
            }

            if (entityUser != null) {
                return entityUser;  // Return the user if found by EntityManager
            }

            // If no user was found with EntityManager, fallback to JDBC logic
            try (Connection connection = DatabaseConnectionService.getConnection()) {
                String query = "SELECT * FROM users WHERE username = ?";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setString(1, username);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            int id = rs.getInt("id");
                            String retrievedUsername = rs.getString("username");
                            String password = rs.getString("password");
                            return new User(id, retrievedUsername, password);  // Return user found by JDBC
                        } else {
                            return null;  // Return null if no user found by JDBC
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Handle any other exceptions (for example, JDBC related issues)
            e.printStackTrace();
            return null;  // Return null if an exception occurs
        }
    }

    // Gérer la session utilisateur
    public HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }

    // Invalider la session utilisateur (déconnexion)
    public void logout() {
        HttpSession session = getSession();
        session.invalidate();
    }

    // Initialiser la session avec le nom d'utilisateur
    public void setSessionUsername(String username) {
        HttpSession session = getSession();
        session.setAttribute("username", username);
    }
}
