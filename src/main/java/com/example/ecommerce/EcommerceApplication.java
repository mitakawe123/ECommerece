package com.example.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.example.ecommerce.repository"})
@EntityScan(basePackages = {"com.example.ecommerce.model"})
public class EcommerceApplication {
	public static void main(String[] args) {

		SpringApplication.run(EcommerceApplication.class, args);
	}
}
