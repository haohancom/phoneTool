package com.phone.tool.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.phone.tool.entity.Commands;

public class CommandDTO {
    private String request;
    private String response;
    private String delay;
    private String sender;
    private String receiver;
    private String function_name;
    private String description;

    @JsonCreator
    public CommandDTO(Commands commands) {
        request = commands.getRequest();
        response = commands.getResponse();
        delay = commands.getDelay();
        sender = commands.getSender();
        receiver = commands.getReceiver();
        function_name = commands.getFunction_name();
        description = commands.getDescription();
    }

    @JsonCreator
    public CommandDTO(@JsonProperty("request") String request,
                      @JsonProperty("response") String response,
                      @JsonProperty("delay") String delay,
                      @JsonProperty("sender") String sender,
                      @JsonProperty("receiver") String receiver,
                      @JsonProperty("function_name") String function_name,
                      @JsonProperty("description") String description) {
        this.request = request;
        this.response = response;
        this.delay = delay;
        this.sender = sender;
        this.receiver = receiver;
        this.function_name = function_name;
        this.description = description;
    }

    public String getRequest() {
        return request;
    }

    public String getResponse() {
        return response;
    }

    public String getDelay() {
        return delay;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getFunction_name() {
        return function_name;
    }

    public String getDescription() {
        return description;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setFunction_name(String function_name) {
        this.function_name = function_name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
