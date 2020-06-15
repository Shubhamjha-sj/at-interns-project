package com.geofence.geo.security;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.geofence.geo.constants.StringConstants.*;

@Configuration
public class RabbitConfig {

    @Value("${spring.rabbitmq.username}")
    private String rabbitUsername;

    @Value("${spring.rabbitmq.password}")
    private String rabbitPassword;

    @Value("${spring.rabbitmq.host}")
    private String rabbitHost;
    @Bean
    Queue enterQueue() {
        return new Queue(ENTER_QUEUE, true);
    }

    @Bean
    Queue exitQueue() {
        return new Queue(EXIT_QUEUE, true);
    }
    @Bean
    Queue dwellQueue() {
        return new Queue(DWELL_QUEUE, true);
    }

    @Bean
    Exchange myTestExchange() {
        return new TopicExchange(GEOFENCE_EXCHANGE);
    }

    @Bean
    Binding enterQueueBinding() {
        return new Binding(ENTER_QUEUE, Binding.DestinationType.QUEUE, GEOFENCE_EXCHANGE, QUEUE_ENTER, null);
    }

    @Bean
    Binding exitQueueBinding() {
        return new Binding(EXIT_QUEUE, Binding.DestinationType.QUEUE, GEOFENCE_EXCHANGE, QUEUE_EXIT, null);
    }

    @Bean
    Binding dwellQueueBinding() {
        return new Binding(DWELL_QUEUE, Binding.DestinationType.QUEUE, GEOFENCE_EXCHANGE, QUEUE_DWELL, null);
    }


    @Bean
    ConnectionFactory connectionFactory() {

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitHost);
        connectionFactory.setUsername(rabbitUsername);
        connectionFactory.setPassword(rabbitPassword);
        return connectionFactory;
    }


}
