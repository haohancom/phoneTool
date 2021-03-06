package com.phone.tool.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phone.tool.dto.CommandDTO;
import com.phone.tool.dto.DTOData;
import com.phone.tool.dto.ExecutionResponse;
import com.phone.tool.service.CommandsService;
import com.phone.tool.service.NettyService;
import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/phone/tool/api")
public class CommandsController {
    @Autowired
    CommandsService commandsService;

    @Autowired
    NettyService nettyService;

    @GetMapping(path = "/commands")
    @ResponseBody
    public DTOData getAllCommands() {
        return new DTOData(commandsService.getAllCommands());
    }

    @PostMapping(path = "/command/query")
    @ResponseBody
    public DTOData getCommandById(@NotNull @RequestParam("id") String id) {
        return new DTOData(commandsService.getCommandsById(id));
    }

    @DeleteMapping(path = "/command")
    @ResponseBody
    public void deleteCommandById(@NotNull @RequestParam("id") String id) {
        commandsService.deleteCommands(id);
    }

    @PostMapping(path = "/command")
    @ResponseBody
    public void addCommand(@RequestBody String content) throws JsonProcessingException {
        CommandDTO commandDTO = new ObjectMapper().readValue(content, CommandDTO.class);
        commandsService.insertCommands(commandDTO);
    }

    @PostMapping(path = "/command/update/delay")
    @ResponseBody
    public void updateCommandDelay(@NotNull @RequestParam("id") String id, @NotNull @RequestParam("delay") String delay) {
        commandsService.updateDelay(id, delay);
    }

    @PostMapping(path = "/command/update/response")
    @ResponseBody
    public void updateCommandResponse(@NotNull @RequestParam("id") String id, @NotNull @RequestParam("response") String response) {
        commandsService.updateResponse(id, response);
    }

    @PostMapping(path = "/command/execute")
    @ResponseBody
    public ExecutionResponse executeCommand(@NotNull @RequestParam("port") String port, @NotNull @RequestParam("id") String id) {
        return new ExecutionResponse(nettyService.execute(Integer.parseInt(port), id));
    }
}
