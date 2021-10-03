package com.phone.tool.service;

import com.phone.tool.dao.CommandDao;
import com.phone.tool.dto.CommandDTO;
import com.phone.tool.entity.Commands;
import com.phone.tool.exception.ToolException;
import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@Service
@Slf4j
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
        commands.setFunction_name(function);
        commands.setDescription(description);
        return commandDao.createCommands(commands);
    }

    public Commands insertCommands(CommandDTO commandDTO) {
        validateDelay(commandDTO.getDelay());
        Commands commands = new Commands();
        commands.setRequest(commandDTO.getRequest());
        commands.setResponse(commandDTO.getResponse());
        commands.setDelay(commandDTO.getDelay());
        commands.setSender(commandDTO.getSender());
        commands.setReceiver(commandDTO.getReceiver());
        commands.setFunction_name(commandDTO.getFunction());
        commands.setDescription(commandDTO.getDescription());
        log.info("commands : {}", commands);
        return commandDao.createCommands(commands);
    }

    public void updateDelay(String id, String delay) {
        validateDelay(delay);
        commandDao.updateDelay(delay, id);
    }

    public void updateResponse(String id, String response) {
        commandDao.updateResponse(response, id);
    }

    public void validateDelay(String delay) {
        if (ObjectUtils.isEmpty(delay)) return;
        try {
            Integer.valueOf(delay);
        } catch (Exception e) {
            throw new ToolException(SC_BAD_REQUEST, "invalid delay param !");
        }
    }
}
