package com.example.consumer.consumer.config;

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import ch.qos.logback.core.net.SocketConnector.ExceptionHandler;


@Configuration
public class MqConfig {

    @Autowired
    private Environment env;

    public static String QUEUE_WORK;

    public static String QUEUE_RESULT;

    public static String WORK_EXCHANGE;

    public static String RESULT_EXCHANGE;

    public static String WORK_ROUTING_KEY;

    public static String RESULT_ROUTING_KEY;
   
    @Value("${work.queue.name}")
    private String controlQueueName;

    public String buildFor() {
        return controlQueueName;
    }

    @Bean
    public Queue queueWork(@Value("${work.queue.name}") String queue){
        QUEUE_WORK = queue;
        System.out.println(queue);
        return new Queue(QUEUE_WORK);
    }

    @Bean
    public Queue queueResult(@Value("${result.queue.name}") String queue){
        QUEUE_RESULT = queue;
        System.out.println(queue);
        return new Queue(QUEUE_RESULT);
    }

    @Bean
    public TopicExchange exchangeWork(@Value("${work.topic_exchange.name}") String exchange){
        WORK_EXCHANGE = exchange;
        System.out.println(WORK_EXCHANGE);
        return new TopicExchange(WORK_EXCHANGE);
    }

    @Bean
    public TopicExchange exchangeResult(@Value("${result.topic_exchange.name}") String exchange){
        RESULT_EXCHANGE = exchange;
        System.out.println(RESULT_EXCHANGE);
        return new TopicExchange(RESULT_EXCHANGE);
    }

    @Bean
    public Binding bindingWork(@Value("${work.routing_key.name}") String routing_key, @Qualifier("queueWork") Queue queue, @Qualifier("exchangeWork")TopicExchange exchange){
        WORK_ROUTING_KEY = routing_key;
        System.out.println(WORK_ROUTING_KEY);
        return BindingBuilder
            .bind(queue)
            .to(exchange)
            .with(WORK_ROUTING_KEY);
    }

    @Bean
    public Binding bindingResult(@Value("${result.routing_key.name}") String routing_key, @Qualifier("queueResult") Queue queue, @Qualifier("exchangeResult") TopicExchange exchange){
        RESULT_ROUTING_KEY = routing_key;
        System.out.println(RESULT_ROUTING_KEY);
        return BindingBuilder
            .bind(queue)
            .to(exchange)
            .with(RESULT_ROUTING_KEY);
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
