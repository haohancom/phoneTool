package com.phone.tool.dao;

import com.phone.tool.entity.Commands;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
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

    @Transactional
    @Modifying
    @Query("update Commands c set c.delay = ?1 where c.request = ?2")
    void updateDelay(String delay, String id);

    @Transactional
    @Modifying
    @Query("update Commands c set c.response = ?1 where c.request = ?2")
    void updateResponse(String response, String id);
}
