package com.vaiak.moto_compare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MotoCompareApplication {

  public static void main(String[] args) {
    SpringApplication.run(MotoCompareApplication.class, args);
  }
}
