package com.honda.ahm.lc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.honda.ahm.lc" })
public class LcLotSequencingClientApplication {
	public static void main(String[] args) {
		SpringApplication.run(LcLotSequencingClientApplication.class, args);
	}

}
