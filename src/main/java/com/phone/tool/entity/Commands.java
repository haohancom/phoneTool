package com.phone.tool.entity;

import javax.persistence.*;

@Table(name = "Commands")
@Entity
public class Commands {
    @Id
    @Column(name = "request")
    private String request;

    @Column(name = "response")
    private String response;

    @Column(name = "delay") // seconds
    private String delay;

    @Column(name = "sender")
    private String sender;

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "function_name")
    private String function_name;

    @Column(name = "description")
    private String description;

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

    public void setFunction_name(String function) {
        this.function_name = function;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
