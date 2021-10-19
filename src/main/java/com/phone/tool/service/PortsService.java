package com.phone.tool.service;

import com.phone.tool.dao.PortsDao;
import com.phone.tool.dto.PortsDTO;
import com.phone.tool.entity.Ports;
import com.phone.tool.exception.ToolException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@Service
@Slf4j
public class PortsService {
    @Autowired
    PortsDao portsDao;

    public List<PortsDTO> getAllPorts() {
        List<Ports> portsList= portsDao.findAll();
        return portsList.stream().map(PortsDTO::new).collect(Collectors.toList());
    }

    public PortsDTO getPortsById(String id) {
        return new PortsDTO(portsDao.getById(id));
    }

    public void deletePorts(String id) {
        portsDao.deleteById(id);
    }

    public Ports insertPorts(PortsDTO portsDTO) {
        validatePort(portsDTO.getPort());
        Ports ports = new Ports();
        ports.setPort(portsDTO.getPort());
        return portsDao.createPorts(ports);
    }

    public Ports insertPorts(String port) {
        validatePort(port);
        Ports ports = new Ports();
        ports.setPort(ports.getPort());
        return portsDao.createPorts(ports);
    }

    private void validatePort(String port) {
        if (ObjectUtils.isEmpty(port)) return;
        try {
            Long.valueOf(port);
        } catch (Exception e) {
            throw new ToolException(SC_BAD_REQUEST, "invalid port param !");
        }
    }
}
