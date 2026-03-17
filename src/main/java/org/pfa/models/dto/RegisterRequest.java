package org.pfa.models.dto;

import io.javalin.http.Context;

public class RegisterRequest {
    private String mail;
    private String password;
    private String nom;
    private String prenom;
    private String code;

    public RegisterRequest() {}

    public RegisterRequest(Context ctx) {
        this.mail = ctx.formParam("mail");
        this.password = ctx.formParam("password");
        this.nom = ctx.formParam("nom");
        this.prenom = ctx.formParam("prenom");
        this.code = ctx.formParam("code");
    }

    public RegisterRequest(String mail, String password, String nom, String prenom) {
        this.mail = mail;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
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

    public boolean isValid() {
        return mail != null && !mail.isEmpty() &&
               password != null && !password.isEmpty() &&
               nom != null && !nom.isEmpty() &&
               prenom != null && !prenom.isEmpty();
    }
}
