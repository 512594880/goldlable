package com.hitales.goldlable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GoldlableApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoldlableApplication.class, args);
	}
}
