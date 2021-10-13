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
import java.util.concurrent.CountDownLatch;
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

    private ChannelFuture future = null;
    private CountDownLatch latch = new CountDownLatch(1);

    @PostConstruct
    public void initNetty() {
        new Thread(() -> {
            log.info("init netty ...");
            ChannelFuture startFuture = null;
            try {
                startFuture = nettyServer.start(url, port);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Runtime.getRuntime().addShutdownHook(new Thread(() -> nettyServer.destroy()));
            assert startFuture != null;
            startFuture.channel().closeFuture().syncUninterruptibly();
        }).start();
    }

    public ChannelFuture addNetty(int nettyPort) throws InterruptedException {
        new Thread(() -> {
            log.info("add netty whose port is {} ...", nettyPort);
            try {
                future = nettyServer.start(url, nettyPort);
                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Runtime.getRuntime().addShutdownHook(new Thread(() -> nettyServer.destroy()));
            assert future != null;
            future.channel().closeFuture().syncUninterruptibly();
        }).start();
        latch.await();
        latch = new CountDownLatch(1);
        return future;
    }

    public void stopNetty(ChannelFuture channelFuture) {
        new Thread(() -> {
            try {
                channelFuture.channel().close().sync();
                log.info("netty whose port is {} is stopped", port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public String execute(Integer port, String request) {
        CommandDTO commandDTO = new CommandDTO(commandDao.getById(request));
        if (!"server".equals(commandDTO.getSender())) throw new ToolException(SC_BAD_REQUEST, "sender must be server!");

        nettyServer.setResponse("");

        if (ObjectUtils.isEmpty(commandDTO.getResponse())) { // no response
            nettyServer.writeMsg(port, request);
            return null;
        }

        // waiting for response from client
        nettyServer.setWaitForResponse(true);
        nettyServer.writeMsg(port, request);
        try {
            TimeUnit.MILLISECONDS.sleep(200);
            if (!ObjectUtils.isEmpty(commandDTO.getDelay()))
                TimeUnit.MILLISECONDS.sleep(Long.parseLong(commandDTO.getDelay()));
        } catch (Exception ignored) {}
        nettyServer.setWaitForResponse(false);
        return nettyServer.getResponse();
    }
}