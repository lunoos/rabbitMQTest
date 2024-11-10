package com.rabbitmq.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    @RabbitListener (queues = "testQueue")
    public void receiveMessage(MessageReq messageReq) {
        System.out.println("Received message: " + messageReq.getMessage());
        // Process message
    }
}
