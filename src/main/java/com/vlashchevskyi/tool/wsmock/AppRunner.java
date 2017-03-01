package com.vlashchevskyi.tool.wsmock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:app.properties")
public class AppRunner {
	public static void main(String[] args) {
		SpringApplication.run(AppRunner.class, args);
	}
}