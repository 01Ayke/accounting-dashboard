package org.pfa.models.dto;

import io.javalin.http.Context;
import java.util.Map;


public class Response {
    public String token;
    public String status;
    public String message;
    public Integer code;
    
    public Response(String token, String status, String message, Integer code) {
        this.token = token;
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public Response(String status, String message, Integer code) {
        this.token = null;
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public void setContext(Context ctx) {
        if (code >= 200 && code < 300) {
            ctx.status(code).json(Map.of("status", status, "message", message, "sID", token));
        } else {
            ctx.status(code).json(Map.of("status", status, "message", message));
        }
    }
}
