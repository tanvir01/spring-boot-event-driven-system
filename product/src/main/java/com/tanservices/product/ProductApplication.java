package com.tanservices.product;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(Faker faker, ProductRepository productRepository) {
		return args -> {

			// Delete all existing records
			productRepository.deleteAll();

			for (int i = 0; i < 50; i++) {
				Product product = new Product();
				product.setName(faker.commerce().productName());
				product.setDescription(faker.lorem().sentence());
				product.setPrice(faker.number().randomDouble(2, 10, 1000));
				product.setQuantity(faker.number().numberBetween(1, 100));

				productRepository.save(product);
			}
		};
	}
}
