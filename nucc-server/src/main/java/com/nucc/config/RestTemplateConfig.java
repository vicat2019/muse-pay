package com.nucc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Http连接池
 */
@Configuration
public class RestTemplateConfig {
    private Logger log = LoggerFactory.getLogger("RestTemplateConfig");

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory simpleClientHttpRequestFactory){
        return new RestTemplate(simpleClientHttpRequestFactory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(30000);       //单位为ms
        factory.setConnectTimeout(30000);    //单位为ms
        return factory;
    }

}
