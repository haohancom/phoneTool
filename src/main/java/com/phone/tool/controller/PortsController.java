package com.phone.tool.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phone.tool.dto.DTOData;
import com.phone.tool.dto.PortsDTO;
import com.phone.tool.service.PortsService;
import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/phone/tool/api")
public class PortsController {
    @Autowired
    PortsService portsService;

    @GetMapping(path = "/ports")
    @ResponseBody
    public DTOData getAllPorts() {
        return new DTOData(portsService.getAllPorts());
    }

    @DeleteMapping(path = "/port")
    @ResponseBody
    public void deletePortById(@NotNull @RequestParam("port") String port) {
        portsService.deletePorts(port);
    }

    @PostMapping(path = "/port")
    @ResponseBody
    public void addPort(@NotNull @RequestParam("port") String port) throws JsonProcessingException {
        PortsDTO portsDTO = new PortsDTO(port);
        portsService.insertPorts(portsDTO);
    }
}
