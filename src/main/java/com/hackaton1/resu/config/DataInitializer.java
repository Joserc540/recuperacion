package com.hackaton1.resu.config;

import com.hackaton1.resu.model.Product;
import com.hackaton1.resu.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for initializing sample data.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final ProductRepository productRepository;

    /**
     * Initialize sample data.
     *
     * @return CommandLineRunner to run on application startup
     */
    @Bean
    public CommandLineRunner initData() {
        return args -> {
            log.info("Initializing sample data...");
            
            if (productRepository.count() == 0) {
                log.info("Creating sample products");
                
                Product product1 = Product.builder()
                        .name("Laptop")
                        .price(1200.0)
                        .stock(10)
                        .build();
                
                Product product2 = Product.builder()
                        .name("Smartphone")
                        .price(800.0)
                        .stock(20)
                        .build();
                
                Product product3 = Product.builder()
                        .name("Headphones")
                        .price(150.0)
                        .stock(30)
                        .build();
                
                Product product4 = Product.builder()
                        .name("Monitor")
                        .price(300.0)
                        .stock(15)
                        .build();
                
                Product product5 = Product.builder()
                        .name("Keyboard")
                        .price(80.0)
                        .stock(25)
                        .build();
                
                productRepository.save(product1);
                productRepository.save(product2);
                productRepository.save(product3);
                productRepository.save(product4);
                productRepository.save(product5);
                
                log.info("Sample products created successfully");
            } else {
                log.info("Products already exist, skipping initialization");
            }
            
            log.info("Sample data initialization completed");
        };
    }
}