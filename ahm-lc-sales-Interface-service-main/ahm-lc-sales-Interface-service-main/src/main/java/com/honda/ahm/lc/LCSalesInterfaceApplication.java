package com.honda.ahm.lc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJms
@ComponentScan(basePackages = { "com.honda.ahm.lc" })
@EnableCaching
@EnableScheduling
public class LCSalesInterfaceApplication {



  public static void main(String[] args) {
      SpringApplication.run(LCSalesInterfaceApplication.class, args);
  }

}