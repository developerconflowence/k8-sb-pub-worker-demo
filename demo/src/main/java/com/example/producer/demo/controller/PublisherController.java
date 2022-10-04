package com.example.producer.demo.controller;

import java.util.Date;
import java.util.UUID;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.producer.demo.config.MQConfig;
import com.example.producer.demo.models.CustomMessage;

@RestController
@RequestMapping("/api")
public class PublisherController {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/publish")
    public String publishMessage(@RequestBody CustomMessage message){
        message.setMessage_id(UUID.randomUUID().toString());
        message.setMessageDate(new Date().toString());
        rabbitTemplate.convertAndSend(MQConfig.EXCHANGE,MQConfig.ROUTING_KEY, message);
        return "Message published with message ID: "+message.getMessage_id();
    }

    @GetMapping("/result")
    public CustomMessage getResult(){
        ParameterizedTypeReference<CustomMessage> type = new ParameterizedTypeReference<CustomMessage>() {
            
        };
       CustomMessage message = rabbitTemplate.receiveAndConvert("result_queue", type);

       if (message == null){
        return new CustomMessage(null, "Work is in progress...",null);
       }
       return message;
    }
}
