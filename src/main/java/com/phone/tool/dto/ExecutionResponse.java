package com.phone.tool.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExecutionResponse {
    private String response;

    @JsonCreator
    public ExecutionResponse() {
    }

    @JsonCreator
    public ExecutionResponse(@JsonProperty("response") String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
