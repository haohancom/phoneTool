package com.phone.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@Slf4j
public class ToolApplication {
	public static void main(String[] args) {
		SpringApplication.run(ToolApplication.class, args);
	}
}