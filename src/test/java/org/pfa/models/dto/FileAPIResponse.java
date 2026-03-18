package org.pfa.models.dto;

import java.util.Map;

public class FileAPIResponse {
    private Map<String, Integer> message;
    private String status;

    public Map<String, Integer> getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
