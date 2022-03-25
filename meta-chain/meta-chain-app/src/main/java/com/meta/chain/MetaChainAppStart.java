package com.meta.chain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MetaChainAppStart {

    public static void main(String[] args) {
        SpringApplication.run(MetaChainAppStart.class, args);
    }
}
