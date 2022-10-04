package com.example.producer.demo.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import ch.qos.logback.core.net.SocketConnector.ExceptionHandler;


@Configuration
public class MQConfig {

    @Autowired
    private Environment env;

    public static String QUEUE;

    public static String EXCHANGE;

    public static String ROUTING_KEY;



   

    @Bean
    public Queue queue(@Value("${queue.name}") String queue){
        QUEUE = queue;
        System.out.println(queue);
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange exchange(@Value("${topic_exchange.name}") String exchange){
        EXCHANGE = exchange;
        System.out.println(EXCHANGE);
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(@Value("${routing_key.name}") String routing_key, Queue queue, TopicExchange exchange){
        ROUTING_KEY = routing_key;
        System.out.println(ROUTING_KEY);
        return BindingBuilder
            .bind(queue)
            .to(exchange)
            .with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory){

        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;

    }
}
