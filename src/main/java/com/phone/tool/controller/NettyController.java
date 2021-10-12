package com.phone.tool.controller;


import com.phone.tool.netty.NettyServer;
import com.phone.tool.service.NettyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_OK;

@Controller
@RequestMapping("/netty/api")
public class NettyController {
    @Autowired
    NettyServer nettyServer;

    @Autowired
    NettyService nettyService;

    @PostMapping(path = "/sendmessage")
    @ResponseBody
    public void writeMessage(@RequestBody String msg, HttpServletResponse response){
        nettyServer.writeMsg(msg);
        response.setStatus(SC_OK);
    }

    @PostMapping(path = "/addnetty")
    @ResponseBody
    public void addNetty(@RequestParam("port") String port, HttpServletResponse response) {
        nettyService.addNetty(Integer.parseInt(port));
    }
}
