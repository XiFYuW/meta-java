package com.meta.nat.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MetaNatAppStart {

    public static void main(String[] args) {
        SpringApplication.run(MetaNatAppStart.class, args);
    }
}
