package com.unibet.worktest.bank.launcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring boot application main class (Startup class)
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "com.unibet.worktest.*" })
@ComponentScan(basePackages = { "com.unibet.worktest.*" })
@EntityScan(basePackages = { "com.unibet.worktest.*" })
public class BankApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankApplication.class, args);
    }
}
