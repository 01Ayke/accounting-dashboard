package org.pfa.models.dto;

import io.javalin.http.Context;

public class LogoutRequest {
    private String token;

    public  LogoutRequest(Context ctx) {
        this.token = ctx.header("Authorization");
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
