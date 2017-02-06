package com.main.merchandising;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class MerchandisingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MerchandisingServiceApplication.class, args);
	}
}
