package com.sakadream.jsf.controller;

import com.sakadream.jsf.func.Functions;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.inject.Inject;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.sql.SQLException;

@Named("login")
@SessionScoped
public class LoginController implements Serializable {

    private static final long serialVersionUID = 1L;
    @EJB
    private Functions func;

    private String username;  // Ajout des champs pour les inputs
    private String password;

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

    public String login() throws SQLException, ClassNotFoundException {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean valid = func.checkLogin(username, password);
        if(valid) {
            return "home";
        } else {
            context.addMessage("loginForm",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Username or password not valid, please try again!", ""));
            return null;
        }
    }

    public String logout() {
        HttpSession session = func.getSession();
        session.invalidate();
        return "login";
    }
}