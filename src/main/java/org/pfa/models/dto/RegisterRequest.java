package org.pfa.models.dto;

import java.util.List;

import io.javalin.http.Context;

public class RegisterRequest {
    private String mail;
    private String password;
    private String nom;
    private String prenom;
    private String code;
    private List<String> roles;

    public RegisterRequest() {}

    public RegisterRequest(Context ctx) {
        this.mail = ctx.formParam("mail");
        this.password = ctx.formParam("password");
        this.nom = ctx.formParam("nom");
        this.prenom = ctx.formParam("prenom");
        this.code = ctx.formParam("code");
        this.roles = List.of(ctx.formParam("roles").split(","));
    }

    public RegisterRequest(String mail, String password, String nom, String prenom, String code, List<String> roles) {
        this.mail = mail;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.code = code;
        this.roles = roles;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }
    public String getCode() {
        return code;
    }

    public List<String> getRoles() {
        return roles;
    }

    public boolean isValid() {
        return mail != null && !mail.isEmpty() &&
               password != null && !password.isEmpty() &&
               nom != null && !nom.isEmpty() &&
               prenom != null && !prenom.isEmpty() &&
               code != null && !code.isEmpty() &&
               roles != null && !roles.isEmpty();
    }
}
