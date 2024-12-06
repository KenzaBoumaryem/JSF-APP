package com.sakadream.jsf.service;

import com.sakadream.jsf.bean.User;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.sql.*;

@Stateless
public class AuthService {

    // Vérifier les informations de connexion de l'utilisateur
    public boolean checkLogin(String username, String password) throws SQLException, ClassNotFoundException {
        User user = getUserByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    // Récupérer un utilisateur par son nom d'utilisateur depuis la base de données
    public User getUserByUsername(String username) throws SQLException, ClassNotFoundException {
        // Obtenir la connexion via la classe DatabaseConnectionService
        try (Connection connection = DatabaseConnectionService.getConnection()) {
            // Requête pour récupérer l'utilisateur
            String query = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Créer un objet User à partir du résultat de la base de données
                        int id = rs.getInt("id");
                        String retrievedUsername = rs.getString("username");
                        String password = rs.getString("password");
                        return new User(id, retrievedUsername, password);
                    } else {
                        return null;
                    }
                }
            }
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
