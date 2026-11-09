package com.fintect.Banking_Engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BankingEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingEngineApplication.class, args);
	}

}
