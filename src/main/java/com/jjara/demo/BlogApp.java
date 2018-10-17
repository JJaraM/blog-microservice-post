package com.jjara.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Application that needs to run to start the application.
 * 
 * @since 0.0.1
 * @author jonathan
 */
@SpringBootApplication
@EnableDiscoveryClient
public class BlogApp {

    public static void main(String[] args) {
        SpringApplication.run(BlogApp.class, args);
    }
}