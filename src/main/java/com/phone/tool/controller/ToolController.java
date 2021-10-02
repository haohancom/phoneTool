package com.phone.tool.controller;


import com.phone.tool.netty.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_OK;

@Controller
@RequestMapping("/netty/api")
public class ToolController {
    @Autowired
    NettyServer nettyServer;

    @PostMapping(path = "/sendmessage")
    @ResponseBody
    public void writeMessage(@RequestBody String msg, HttpServletResponse response){
        nettyServer.writeMsg(msg);
        response.setStatus(SC_OK);
    }
}
