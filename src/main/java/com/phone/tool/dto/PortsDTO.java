package com.phone.tool.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.phone.tool.entity.Ports;

public class PortsDTO {
    private String port;

    @JsonCreator
    public PortsDTO(Ports ports) {
        port = ports.getPort();
    }

    @JsonCreator
    public PortsDTO(@JsonProperty("port") String port) {
        this.port = port;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
