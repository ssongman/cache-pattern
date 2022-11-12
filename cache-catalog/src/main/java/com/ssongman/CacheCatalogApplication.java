package com.ssongman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CacheCatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(CacheCatalogApplication.class, args);
	}

}