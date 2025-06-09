package com.honda.ahm.sums.vin.bom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.honda.ahm.sums.vin.bom"})
public class LcVinBomClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(LcVinBomClientApplication.class, args);
		//test
	}

}
