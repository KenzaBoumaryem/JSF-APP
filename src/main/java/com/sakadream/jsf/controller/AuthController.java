package com.sakadream.jsf.controller;

import com.sakadream.jsf.service.AuthService;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.sql.SQLException;

@Named("auth")
@SessionScoped
public class AuthController implements Serializable {

    @EJB
    private AuthService authService;

    private String username;
    private String password;

    // Méthode pour gérer la connexion
    public String login() throws SQLException, ClassNotFoundException {
        if (authService.checkLogin(username, password)) {
            authService.setSessionUsername(username);
            return "home";
        } else {
            return "loginFailed";
        }
    }

    // Méthode pour déconnexion
    public String logout() {
        authService.logout();
        return "login";
    }

    // Getters et setters pour username et password
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
