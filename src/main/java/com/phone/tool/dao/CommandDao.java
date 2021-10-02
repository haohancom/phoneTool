package com.phone.tool.dao;

import com.phone.tool.entity.Commands;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandDao extends CrudRepository<Commands, String> {
    List<Commands> findAll();

    Commands getById(String id);

    @Override
    void deleteById(String s);

    default Commands createCommands(Commands commands) {
        return save(commands);
    }
}
