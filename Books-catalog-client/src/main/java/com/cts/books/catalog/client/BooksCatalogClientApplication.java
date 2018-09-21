package com.cts.books.catalog.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
@EnableDiscoveryClient
@SpringBootApplication
public class BooksCatalogClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooksCatalogClientApplication.class, args);
	}
}
