package com.example.consumer.consumer.listerners;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.stereotype.Component;

import com.example.consumer.consumer.config.MqConfig;
import com.example.consumer.consumer.models.CustomMessage;
import com.example.consumer.consumer.services.ServerPortService;

@Component
public class MQListener {
    @Autowired
    private ServerPortService serverPortService;
    
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
     
    @RabbitListener(queues = "#{mqConfig.buildFor()}")
    public void listener(CustomMessage message) throws UnknownHostException{

        
        System.out.println(message + " --- by : "+ InetAddress.getLocalHost().getHostAddress()+":"+serverPortService.getPort());
        message.setMessage(message.getMessage()+" -- by "+ InetAddress.getLocalHost().getHostAddress()+":"+serverPortService.getPort());
        rabbitTemplate.convertAndSend(MqConfig.RESULT_EXCHANGE,MqConfig.RESULT_ROUTING_KEY, message);
        
    }
    

}
