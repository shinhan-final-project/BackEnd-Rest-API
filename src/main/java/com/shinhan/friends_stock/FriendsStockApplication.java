package com.shinhan.friends_stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FriendsStockApplication {

	public static void main(String[] args) {
		SpringApplication.run(FriendsStockApplication.class, args);
	}

}
