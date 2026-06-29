package com.tickets.bff.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient authRestClient(@Value("${services.ms-auth.url}") String url) {
        return RestClient.builder().baseUrl(url).build();
    }

    @Bean
    public RestClient eventRestClient(@Value("${services.ms-event.url}") String url) {
        return RestClient.builder().baseUrl(url).build();
    }

    @Bean
    public RestClient ticketSalesRestClient(@Value("${services.ms-ticket-sales.url}") String url) {
        return RestClient.builder().baseUrl(url).build();
    }
}
