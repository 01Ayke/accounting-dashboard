package org.pfa.models.dto;

import io.javalin.http.Context;

public class LoginRequest {
    private String mail;
    private String password;

    public LoginRequest(Context ctx) {
        this.mail = ctx.formParam("mail");
        this.password = ctx.formParam("password");
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }
}
