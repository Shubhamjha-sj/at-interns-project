package com.geofence.geo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestService {
    public static final Logger restServiceLogger = LoggerFactory.getLogger("Rest Service Logger");
    private final RestTemplate restTemplate;

    public RestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getPostsPlainJSON(String url) {
        restServiceLogger.info("Inside getPostsPlainJSON");
        return this.restTemplate.getForObject(url, String.class);
    }
}
