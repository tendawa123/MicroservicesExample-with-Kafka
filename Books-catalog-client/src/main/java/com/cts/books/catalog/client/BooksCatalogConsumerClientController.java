package com.cts.books.catalog.client;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@EnableHystrix
public class BooksCatalogConsumerClientController {
	
	@Autowired
	private static DiscoveryClient discoveryClient;
	public static final Logger logger = LoggerFactory.getLogger(BooksCatalogConsumerClientController.class);
	public static final String REST_SERVICE_URI = "http://10.97.25.235:8088/api/catalog/books";
	/*public static void main(String[] args) {
		SpringApplication.run(BooksCatalogConsumerClientController.class, args);
		try {
			getAllUsers();
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}*/

	private static void getAllUsers() {
		logger.info("Testing all users get API.....");
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(REST_SERVICE_URI, String.class);
		logger.info(result);
	}

	public static void getAllBooks() throws RestClientException, IOException {
		List<ServiceInstance> instances = discoveryClient.getInstances("books-catalog-service");
		System.out.println("instances------>" + instances.toString());
		ServiceInstance serviceInstance = instances.get(0);
		String baseUrl = serviceInstance.getUri().toString();
		baseUrl = baseUrl + "/api/catalog/applications";
		System.out.println();
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(baseUrl, HttpMethod.GET, getHeaders(), String.class);
		} catch (Exception ex) {
			System.out.println(ex);
		}
		System.out.println(response.getBody());
	}

	private static HttpEntity<?> getHeaders() throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return new HttpEntity<>(headers);
	}
}
