package com.java.test.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * As a manager you should be able to add goods to db so clients will be able to buy them.
 * <p>
 * As a manager/client you should be able to list all available goods and their prices and quantities.
 * <p>
 * As a client you should be able to place orders for goods (multiple allowed for example in you order you can buy
 * iphone 13 Pro - 5 units, and Iphone 11 - 10 units).
 * <p>
 * Your app should manage risks and automatically delete not paid orders after 10 minutes after creation.
 * <p>
 * As a client you should be able to pay for your order so provide endpoint that will mark clients order as paid.
 * <p>
 * Cover logic with tests.
 */

@SpringBootApplication
@EnableJpaRepositories
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

