package com.phone.tool.controller;


import com.phone.tool.netty.NettyServer;
import com.phone.tool.service.NettyService;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

import static javax.servlet.http.HttpServletResponse.SC_OK;

@Controller
@RequestMapping("/netty/api")
public class NettyController {
    @Autowired
    NettyServer nettyServer;

    @Autowired
    NettyService nettyService;

    private final Map<Integer, ChannelFuture> channelFutureMap = new HashMap<>();
    @PostMapping(path = "/sendmessage")
    @ResponseBody
    public void writeMessage(@RequestParam("port") String port, @RequestBody String msg, HttpServletResponse response){
        nettyServer.writeMsg(Integer.parseInt(port), msg);
        response.setStatus(SC_OK);
    }

    @PostMapping(path = "/addnetty")
    @ResponseBody
    public void addNetty(@RequestParam("port") String port, HttpServletResponse response) throws InterruptedException {
        ChannelFuture channelFuture = nettyService.addNetty(Integer.parseInt(port));
        channelFutureMap.put(Integer.valueOf(port), channelFuture);
    }

    @PostMapping(path = "/stopnetty")
    @ResponseBody
    public void stopNetty(@RequestParam("port") String port, HttpServletResponse response) {
        nettyService.stopNetty(channelFutureMap.get(Integer.parseInt(port)));
    }
}
