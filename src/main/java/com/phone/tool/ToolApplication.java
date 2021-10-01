package com.phone.tool;

import com.phone.tool.netty.NettyServer;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@Slf4j
public class ToolApplication implements CommandLineRunner {

	@Value("${netty.port}")
	private int port;

	@Value("${netty.url}")
	private String url;

	@Autowired
	private NettyServer nettyServer;

	public static void main(String[] args) {
		SpringApplication.run(ToolApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ChannelFuture future = nettyServer.start(url, port);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> nettyServer.destroy()));
		TimeUnit.SECONDS.sleep(5);
		log.info("server try to write msg");
		nettyServer.writeMsg("server try to write msg");
		future.channel().closeFuture().syncUninterruptibly();
	}
}
