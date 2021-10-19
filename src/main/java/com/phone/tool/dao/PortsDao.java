package com.phone.tool.dao;

import com.phone.tool.entity.Ports;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortsDao extends CrudRepository<Ports, String> {
    List<Ports> findAll();

    Ports getById(String id);

    @Override
    void deleteById(String s);

    default Ports createPorts(Ports ports) {
        return save(ports);
    }
}
