#  RabbitMQ Messaging with Spring Boot

This guide provides a step-by-step setup for a Spring Boot application that sends and receives messages using RabbitMQ. The application is configured to use JSON serialization and deserialization, enabling objects to be directly converted to JSON for transmission between producer and consumer.

##  Table of Contents

- Introduction
- Prerequisites
- Project Setup
- RabbitMQ Configuration
- Producer Setup
- Consumer Setup
- JSON Message Conversion

##  1. Introduction
RabbitMQ is a messaging broker that allows applications to communicate with each other through messages. In this setup:

## Setup RabbitMQ using following docker command
```cmd
docker run -d --hostname rabbitmq-host --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```


Producer: Sends JSON messages to an exchange.
Consumer: Listens to a queue and processes incoming messages.
Jackson2JsonMessageConverter: Used for automatic JSON conversion of messages.

##  2. Prerequisites
Java 17+
Spring Boot
RabbitMQ server installed locally or accessible remotely

##  3. Project Setup
Add the following dependencies to your pom.xml:

`Pom.xml`
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
```

##  4. RabbitMQ Configuration
In this application, RabbitMQ is configured to use an exchange, a queue, and bindings between them.

###  4.1 Define Configuration Class
Create a configuration class RabbitMQConfig to set up the exchange, queue, and binding:

```java
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private static final String EXCHANGE_NAME = "your_exchange_name";
    private static final String QUEUE_NAME = "your_queue_name";
    private static final String ROUTING_KEY = "your_routing_key";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
```
###  Exchange: Routes messages to the queue based on a routing key.
###  Queue: Stores messages for consumers.
###  Binding: Links the queue to the exchange using the routing key.

##  5. Producer Setup
The producer service sends JSON messages to the configured exchange.

###  5.1 Define a Message Model
Create a class MessageReq that represents the structure of your message:


###  5.2 Define the Producer Service
In the ProducerService class, use the RabbitTemplate to send a message to the exchange.

```java
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey}")
    private String routingKey;

    public ProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(MessageReq message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        System.out.println("Message sent: " + message);
    }
}
```
sendMessage: Converts the MessageReq object to JSON and sends it to the exchange using the routing key.

##  6. Consumer Setup
The consumer service listens to a queue and processes incoming messages.

##  6.1 Implement ConsumerService Class
The ConsumerService class processes incoming JSON messages by deserializing them directly into MessageReq.

```java
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    @RabbitListener(queues = "your_queue_name")
    public void receiveMessage(MessageReq message) {
        System.out.println("Received message: " + message);
        // Process the message as needed
    }
}
```
@RabbitListener: Listens to the specified queue and automatically deserializes JSON messages into MessageReq objects using Jackson2JsonMessageConverter.

##  7. JSON Message Conversion
The Jackson2JsonMessageConverter automatically converts Java objects to JSON for sending and JSON to Java objects when receiving. This eliminates the need for manual serialization/deserialization in the producer and consumer services.

###  Configuration: The Jackson2JsonMessageConverter bean in RabbitMQConfig handles all JSON conversion.
Producer: Uses RabbitTemplate with JSON conversion to send MessageReq as JSON.
Consumer: Uses @RabbitListener to receive messages and automatically convert JSON payloads into MessageReq objects.

##  Summary
This setup provides a robust solution for exchanging JSON messages between a producer and a consumer using RabbitMQ in a Spring Boot application. The configuration ensures easy serialization and deserialization, simplifying message handling while ensuring consistency and type safety.

