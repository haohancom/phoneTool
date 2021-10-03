package com.phone.tool.service;

import com.phone.tool.dao.CommandDao;
import com.phone.tool.dto.CommandDTO;
import com.phone.tool.exception.ToolException;
import com.phone.tool.netty.NettyServer;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@Slf4j
@Service
public class NettyService {
    @Autowired
    NettyServer nettyServer;

    @Autowired
    CommandDao commandDao;

    @Value("${netty.port}")
    private int port;

    @Value("${netty.url}")
    private String url;

    @PostConstruct
    public void initNetty() {
        new Thread(() -> {
            log.info("init netty ...");
            ChannelFuture future = null;
            try {
                future = nettyServer.start(url, port);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Runtime.getRuntime().addShutdownHook(new Thread(() -> nettyServer.destroy()));
            assert future != null;
            future.channel().closeFuture().syncUninterruptibly();
        }).start();
    }

    public String execute(String request) {
        CommandDTO commandDTO = new CommandDTO(commandDao.getById(request));
        if (!"server".equals(commandDTO.getSender())) throw new ToolException(SC_BAD_REQUEST, "sender must be server!");

        nettyServer.setResponse("");

        if (ObjectUtils.isEmpty(commandDTO.getResponse())) { // no response
            nettyServer.writeMsg(request);
            return null;
        }

        // waiting for response from client
        nettyServer.setWaitForResponse(true);
        nettyServer.writeMsg(request);
        try {
            TimeUnit.MILLISECONDS.sleep(200);
            if (!ObjectUtils.isEmpty(commandDTO.getDelay()))
                TimeUnit.MILLISECONDS.sleep(Long.parseLong(commandDTO.getDelay()));
        } catch (InterruptedException ignored) {}
        nettyServer.setWaitForResponse(false);
        return nettyServer.getResponse();
    }
}