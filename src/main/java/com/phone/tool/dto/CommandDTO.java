package com.phone.tool.dto;


import com.phone.tool.entity.Commands;

public class CommandDTO {
    private String request;
    private String response;
    private String delay;
    private String sender;
    private String receiver;
    private String function;
    private String description;

    public CommandDTO(Commands commands) {
        request = commands.getRequest();
        response = commands.getResponse();
        delay = commands.getDelay();
        sender = commands.getSender();
        receiver = commands.getReceiver();
        function = commands.getFunction();
        description = commands.getDescription();
    }

    public CommandDTO(String request, String response, String delay, String sender, String receiver, String function, String description) {
        this.request = request;
        this.response = response;
        this.delay = delay;
        this.sender = sender;
        this.receiver = receiver;
        this.function = function;
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

    public String getFunction() {
        return function;
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

    public void setFunction(String function) {
        this.function = function;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
