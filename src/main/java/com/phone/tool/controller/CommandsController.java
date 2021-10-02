package com.phone.tool.controller;

import com.phone.tool.dto.DTOData;
import com.phone.tool.service.CommandsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/phone/tool/api")
public class CommandsController {
    @Autowired
    CommandsService commandsService;

    @GetMapping(path = "/commands")
    @ResponseBody
    public DTOData getAllCommands() {
        return new DTOData(commandsService.getAllCommands());
    }

    @GetMapping(path = "/command/{id}")
    @ResponseBody
    public DTOData getCommandById(@PathVariable("id") String id) {
        return new DTOData(commandsService.getCommandsById(id));
    }

    @DeleteMapping(path = "/command/{id}")
    @ResponseBody
    public void deleteCommandById(@PathVariable("id") String id) {
        commandsService.deleteCommands(id);
    }

    @PostMapping(path = "/command")
    @ResponseBody
    public void addCommand(@RequestBody String content) {

    }
}
