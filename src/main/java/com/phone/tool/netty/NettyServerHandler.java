package com.phone.tool.netty;

import com.phone.tool.dao.CommandDao;
import com.phone.tool.dto.CommandDTO;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class NettyServerHandler extends ChannelHandlerAdapter {
    @Autowired
    private CommandDao commandDao;

    private NettyServer nettyServer;
    private int counter = 0;

    public NettyServerHandler(NettyServer nettyServer){
        this.nettyServer = nettyServer;
    }

    public void setNettyServer(NettyServer nettyServer) {
        this.nettyServer = nettyServer;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        if (nettyServer.isWaitForResponse()) { // receive response from client
            String response = in.toString(StandardCharsets.UTF_8);
            nettyServer.setResponse(response);
        } else { // receive request from client
            String id = in.toString(CharsetUtil.UTF_8);
            log.info("server received command: {}", id);
            CommandDTO commandDTO = new CommandDTO(commandDao.getById(id));

            // no need to return response
            if (ObjectUtils.isEmpty(commandDTO.getResponse())) return;

            // response to client
            try {
                TimeUnit.MILLISECONDS.sleep(Long.parseLong(commandDTO.getDelay()));
            } catch (Exception ignored) {}

            log.info("should return response : {}", commandDTO.getResponse());
            ctx.writeAndFlush(Unpooled.copiedBuffer(commandDTO.getResponse(), CharsetUtil.UTF_8));
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String clientName = ctx.channel().remoteAddress().toString();
        log.info("RemoteAddress : {} active !", clientName);
        nettyServer.setClient(clientName);
        nettyServer.setChannel(clientName,ctx.channel());
        super.channelActive(ctx);
        counter = 0;
    }
}
