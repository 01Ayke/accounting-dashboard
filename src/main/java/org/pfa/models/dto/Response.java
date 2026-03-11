package org.pfa.models.dto;

import io.javalin.http.Context;
import java.util.Map;


public class Response {
    public String token;
    public String status;
    public Object message;
    public Integer code;
    public Object data;
    
    public Response(String token, String status, Object message, Integer code) {
        this.token = token;
        this.status = status;
        this.message = message;
        this.code = code;
        this.data = null;
    }

    public Response(String status, Object message, Integer code) {
        this.token = null;
        this.status = status;
        this.message = message;
        this.code = code;
        this.data = null;
    }

    public Response(String status, Object message, Integer code, Object data) {
        this.token = null;
        this.status = status;
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public void setLoginContext(Context ctx) {
        if (code >= 200 && code < 300) {
            try {
                ctx.status(code).json(Map.of("status", status, "message", message, "sID", token));
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("problem");
            }
        } else {
            ctx.status(code).json(Map.of("status", status, "message", message));
        }
    }

    public void setContext(Context ctx) {
        if (code >= 200 && code < 300) {
            try {

                ctx.status(code).json(Map.of("status", status, "message", message));
                
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("problem");
            }
        } else {
            ctx.status(code).json(Map.of("status", status, "message", message));
        }
    }


    public void setContextData(Context ctx) {
        if (code >= 200 && code < 300) {
            try {

                ctx.status(code).json(Map.of("status", status, "message", message, "data", data));
                
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("problem");
            }
        } else {
            ctx.status(code).json(Map.of("status", status, "message", message));
        }
    }

    
}
