package com.phone.tool;

import com.phone.tool.netty.EchoServer;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ToolApplication implements CommandLineRunner {

	@Value("${netty.port}")
	private int port;

	@Value("${netty.url}")
	private String url;

	@Autowired
	private EchoServer echoServer;

	public static void main(String[] args) {
		SpringApplication.run(ToolApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ChannelFuture future = echoServer.start(url, port);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				echoServer.destroy();
			}
		});
		future.channel().closeFuture().syncUninterruptibly();
	}
}
