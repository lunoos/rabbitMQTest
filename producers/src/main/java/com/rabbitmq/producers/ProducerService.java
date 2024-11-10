package com.rabbitmq.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

	private final RabbitTemplate rabbitTemplate;

    public ProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(MessageReq messageReq) {
        rabbitTemplate.convertAndSend("example.exchange", "routing.key", messageReq);
        System.out.println("Message sent: " + messageReq.getMessage());
    }
}
