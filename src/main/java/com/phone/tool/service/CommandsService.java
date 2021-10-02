package com.phone.tool.service;

import com.phone.tool.dao.CommandDao;
import com.phone.tool.dto.CommandDTO;
import com.phone.tool.entity.Commands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommandsService {
    @Autowired
    CommandDao commandDao;

    public List<CommandDTO> getAllCommands() {
        List<Commands> commandsList = commandDao.findAll();
        return commandsList.stream().map(CommandDTO::new).collect(Collectors.toList());
    }

    public CommandDTO getCommandsById(String id) {
        return new CommandDTO(commandDao.getById(id));
    }

    public void deleteCommands(String id) {
        commandDao.deleteById(id);
    }

    public Commands insertCommands(String request, String response, String delay, String sender, String receiver, String function, String description) {
        Commands commands = new Commands();
        commands.setRequest(request);
        commands.setResponse(response);
        commands.setDelay(delay);
        commands.setSender(sender);
        commands.setReceiver(receiver);
        commands.setFunction(function);
        commands.setDescription(description);
        return commandDao.createCommands(commands);
    }
}
