package com.honda.ahm.lc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
//@EnableCaching
public class LcProductSearchVdbServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LcProductSearchVdbServiceApplication.class, args);
	}
	
	

}
