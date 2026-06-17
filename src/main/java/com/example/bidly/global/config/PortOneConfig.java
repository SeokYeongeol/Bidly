package com.example.bidly.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class PortOneConfig {

    @Bean
    public RestClient portOneRestClient() {
        return RestClient.builder()
                .baseUrl("https://api.portone.io")
                .build();
    }
}
