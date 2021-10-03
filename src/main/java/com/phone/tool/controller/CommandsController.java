package com.phone.tool.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phone.tool.dto.CommandDTO;
import com.phone.tool.dto.DTOData;
import com.phone.tool.service.CommandsService;
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

    // todo : add update response function
    // todo : add update delay function
    @GetMapping(path = "/commands")
    @ResponseBody
    public DTOData getAllCommands() {
        return new DTOData(commandsService.getAllCommands());
    }

    @GetMapping(path = "/command/{id}")
    @ResponseBody
    public DTOData getCommandById(@PathVariable("id") String id) {
        log.info("get command by id : {}", id);
        return new DTOData(commandsService.getCommandsById(id));
    }

    @DeleteMapping(path = "/command/{id}")
    @ResponseBody
    public void deleteCommandById(@PathVariable("id") String id) {
        commandsService.deleteCommands(id);
    }

    @PostMapping(path = "/command")
    @ResponseBody
    public void addCommand(@RequestBody String content) throws JsonProcessingException {
        CommandDTO commandDTO = new ObjectMapper().readValue(content, CommandDTO.class);
        commandsService.insertCommands(commandDTO);
    }
}
