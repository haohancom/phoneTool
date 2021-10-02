package com.phone.tool.service;

import com.phone.tool.netty.NettyServer;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class NettyService {
    @Autowired
    NettyServer nettyServer;

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

    public void writeMsg(String msg) {
        log.info("server try to write msg : {}", msg);
        nettyServer.writeMsg(msg);

    }
}