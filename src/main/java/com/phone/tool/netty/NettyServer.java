package com.phone.tool.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class NettyServer {
    @Autowired
    NettyServerHandler nettyServerHandler;

    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Channel channel;
    private Map<String, Integer> clientMap = new HashMap<>();

    @PostConstruct
    public void init() {
        nettyServerHandler.setNettyServer(NettyServer.this);
    }

    public synchronized void setClient(String name) {
        this.clientMap.put(name, 1);
    }

    public synchronized void removeClient(String name) {
        this.clientMap.remove(name);
    }

    public synchronized boolean getClientMapSize() {
        return this.clientMap.size() > 0;
    }

    private Map<String, Channel> channelMap = new HashMap<>();

    public synchronized void setChannel(String name, Channel channel) {
        this.channelMap.put(name, channel);
    }

    public synchronized Map<String, Channel> getChannelMap() {
        return this.channelMap;
    }

    public ChannelFuture start(String hostname, int port) {
        ChannelFuture f = null;
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(hostname,port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(nettyServerHandler);
                        }
                    });

            f = b.bind().sync();
            channel = f.channel();
            log.info("======EchoServer startup=========");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (f != null && f.isSuccess()) {
                log.info("Netty server listening " + hostname + " on port " + port + " and ready for connections...");
            } else {
                log.error("Netty server start up Error!");
            }
        }
        return f;
    }

    public boolean writeMsg(String msg) {
        boolean errorFlag = false;
        channelMap = getChannelMap();
        if (channelMap.size() == 0) {
            log.info("channel size is 0");
            return true;
        }
        Set<String> keySet = clientMap.keySet();
        for (String key : keySet) {
            try {
                channel = channelMap.get(key);
                if (!channel.isActive()) {
                    log.info("channel {} is inactive", channel);
                    errorFlag = true;
                    continue;
                }
                log.info("channel {} is active", channel);
                String s = msg + System.getProperty("line.separator");
                // can not send string directly without using Unpooled.copiedBuffer
                channel.writeAndFlush(Unpooled.copiedBuffer(s, CharsetUtil.UTF_8));
            } catch (Exception e) {
                errorFlag = true;
            }
        }
        return errorFlag;
    }


    public void destroy() {
        log.info("Shutdown Netty Server...");
        if (channel != null) {
            channel.close();
        }
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        log.info("Shutdown Netty Server Success!");
    }
}
