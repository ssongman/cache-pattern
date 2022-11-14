package com.kt.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableCaching
@EnableRetry //Retry 활성화
public class CacheUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(CacheUsersApplication.class, args);
	}

}
