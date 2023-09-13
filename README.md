# Connector Introduction
Connector is a Java based backend extension for WaveMaker applications. Connectors are built as Java modules & exposes java based SDK to interact with the connector implementation. Each connector is built for a specific purpose and can be integrated with one of the external services. Connectors are imported & used in the WaveMaker application. Each connector runs on its own container thereby providing the ability to have it’s own version of the third party dependencies.

# RabbitMQ
RabbitMQ is an open-source message broker software that provides a way for different software systems to communicate with each other by sending and receiving messages. It is often used to implement messaging patterns such as message queuing, publish/subscribe, and request/reply in distributed systems.

![image](https://github.com/wm-igniters/rabbitmq-connector/assets/144779049/96d91944-472a-458e-ae6c-5b90ba786e37)


Here are some key concepts and features of RabbitMQ:

+ **Message Broker**: RabbitMQ acts as a middleman or message broker between different components of a distributed system. It accepts messages from producers (applications that send messages) and delivers them to consumers (applications that receive messages).

+ **Message Queues**: RabbitMQ uses message queues to store messages until they are consumed by the intended recipient. Queues enable asynchronous communication and decouple producers from consumers.

+ **Publish/Subscribe**: RabbitMQ supports the publish/subscribe messaging pattern, where a message can be broadcast to multiple consumers. This is useful for scenarios where multiple consumers need to receive the same message.

+ **Routing**: RabbitMQ allows for sophisticated message routing based on message attributes, allowing messages to be directed to specific queues or consumers based on criteria you define.

+ **Exchange**: In RabbitMQ, an exchange is responsible for receiving messages from producers and routing them to the appropriate queues. Different types of exchanges, such as direct, topic, fanout, and headers, provide flexibility in routing messages.

+ **Bindings**: Bindings define the relationship between exchanges and queues, specifying how messages should be routed from exchanges to queues.

+ **AMQP Protocol**: RabbitMQ uses the Advanced Message Queuing Protocol (AMQP) as the communication protocol between clients and the server. AMQP is an open standard for messaging.

+ **Durable and Persistent**: RabbitMQ supports durability for messages and queues, which ensures that messages are not lost even if the broker or a consumer crashes.

+ **Clustering**: RabbitMQ can be set up in a cluster configuration to provide high availability and scalability. Clusters distribute messages across multiple nodes for fault tolerance.

+ **Plugins and Extensions**: RabbitMQ offers a plugin architecture that allows you to extend its functionality. There are various plugins available for features like message authentication, authorization, and more.

RabbitMQ is commonly used in microservices architectures, cloud-native applications, and any scenario where you need to reliably and efficiently pass messages between different components of a distributed system. It supports multiple programming languages and has a wide range of client libraries, making it accessible to developers working in various environments.

# rabbitmq-connector
This connector is used to publish/send the messagaes(json and normal text format) to RabbitMQ and consume from the same.

# Prerequisite
+ Rabbitmq(host, port, username and password)
+ Create exchange, queue and bind queue with exchange in RabbitMQ server 
+ Java 1.8 or above
+ Maven 3.1.0 or above
+ Any java editor such as Eclipse, Intelij..etc

# Project setup and build
+ Download this project code
+ Open in any editor
+ Open terminal go to root of the downloaded project
+ Use **mvn clean install** comand to build the connector zip file which will be available in **dist** folder 

# Usage in wavemaker application
+ Go to file explorer
+ Click on import resource
+ Go to connector section
+ upload the zip file from **dist** folder
+ Provide Rabbitmq details(host, port, username and password)
  ![image](https://github.com/wm-igniters/rabbitmq-connector/assets/144779049/ece74240-d796-4c79-a938-3f16084d6508)
## Publisher 
   + Import below in java service
```
import com.wavemaker.connector.rabbitmq.WaveMakerRabbitmqConnector;
```
   + Autowire the connector
     
```
    @Autowired
    private WaveMakerRabbitmqConnector rabbitmqConnector;
```

  + To publish the json message, use **sendJsonMessage** from the connector
    ```
    rabbitmqConnector.sendJsonMessage(exchangeName, routingKey, obj, String.valueOf(Math.random()));
    ```
  + Example

   ```

package com.employeeonboarding.myjavaservice;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;


import com.wavemaker.runtime.security.SecurityService;
import com.wavemaker.runtime.service.annotations.ExposeToClient;
import com.wavemaker.runtime.service.annotations.HideFromClient;

import com.wavemaker.connector.rabbitmq.WaveMakerRabbitmqConnector;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

//import com.employeeonboarding.myjavaservice.model.*;

@ExposeToClient
public class MyJavaService {

    private static final Logger logger = LoggerFactory.getLogger(MyJavaService.class);

    
    @Autowired
    private WaveMakerRabbitmqConnector rabbitmqConnector;
   
    /**
     * Api to send Json object to Rabbitmq server
     */
    public void addEmployee(Object obj, String exchangeName, String routingKey, HttpServletRequest request){
        logger.info("New Employee " + obj.toString());
        rabbitmqConnector.sendJsonMessage(exchangeName, routingKey, obj, String.valueOf(Math.random()));
    }
    
}
```

## Consumer
 + Import below in java service
```
import com.wavemaker.connector.rabbitmq.WaveMakerRabbitmqConnector;
import com.rabbitmq.client.DeliverCallback;
```
   + Autowire the connector
     
```
    @Autowired
    private WaveMakerRabbitmqConnector rabbitmqConnector;
```

  + To consume the  message, use **consumeMessage** from the connector
 ```
    rabbitmqConnector.consumeMessage(queueName, true, deliverCallback, consumerTag -> { });
 ```
  + Configure the queueName in App Environment
    ![image](https://github.com/wm-igniters/rabbitmq-connector/assets/144779049/7c8dcf75-557b-47e5-b6af-976c35cb669b)

    
  + Example
```
import javax.servlet.http.HttpServletRequest;
import com.employeesdb.wavemaker.Employee;
import com.employeesdb.wavemaker.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.wavemaker.runtime.security.SecurityService;
import com.wavemaker.runtime.service.annotations.ExposeToClient;
import com.wavemaker.runtime.service.annotations.HideFromClient;
import com.wavemaker.connector.rabbitmq.WaveMakerRabbitmqConnector;
import com.rabbitmq.client.DeliverCallback;
import java.util.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;

//import com.employeesdb.myjavaservice.model.*;

@ExposeToClient
public class MyJavaService {

    private static final Logger logger = LoggerFactory.getLogger(MyJavaService.class);
    
    private WaveMakerRabbitmqConnector rabbitmqConnector;

    private EmployeeService employeeService;
   
    private String queueName;
   
    MyJavaService(WaveMakerRabbitmqConnector rabbitmqConnector, EmployeeService employeeService, @Value("${app.environment.queueName}") String queueName )throws IOException, TimeoutException{
       this.rabbitmqConnector = rabbitmqConnector;
       this.employeeService = employeeService;
       this.queueName = queueName;
       logger.info("invoking the consumer on application start up for the queue: "+queueName);
        getMessage(queueName);
     }
   
     private final ConcurrentHashMap<String, Boolean> deduplicationStore = new ConcurrentHashMap<>();
   
     public void cancelConsume(String consumerTag, HttpServletRequest request)throws IOException{
         logger.info("Cancelling the consumer: "+consumerTag);
         rabbitmqConnector.cancelConsume(consumerTag);
     }
    
     public String getMessage(String queueName)throws IOException, TimeoutException{
         logger.info("Consuming message from RabbitMQ");
         DeliverCallback deliverCallback = (consumerTag, delivery) -> {
         String messageId = delivery.getProperties().getMessageId();
         logger.info("messageId: "+messageId);
          if (!isMessageProcessed(messageId)) {
                 String message = new String(delivery.getBody(), "UTF-8");
                 ObjectMapper objectMapper = new ObjectMapper();
                 Map<String, Object> resultMap = objectMapper.readValue(message, Map.class);
                 logger.info("resultMap "+resultMap);
                 Map<String, Object> input = (Map<String, Object>) resultMap.get("Employee");
                 logger.info("inputMap "+input);
                 Employee employee = new Employee();
                 employee.setFirstName(String.valueOf(input.get("firstName")));
                 employee.setLastName(String.valueOf(input.get("lastName")));
                 employee.setAddress(String.valueOf(input.get("address")));
                 employee.setDob(String.valueOf(input.get("dob")));
                 employee.setEmail(String.valueOf(input.get("email")));
                 employee.setPhone((Integer) input.get("phone"));
                 employeeService.create(employee);
                 markMessageAsProcessed(messageId);
            }
         };
         rabbitmqConnector.consumeMessage(queueName, true, deliverCallback, consumerTag -> { });
         return "Success";
    }
    
    private boolean isMessageProcessed(String messageId) {
        // Check if the message ID exists in the deduplication store
         logger.info("Inside isMessageProcessed");
        return deduplicationStore.containsKey(messageId);
    }

    private void markMessageAsProcessed(String messageId) {
        // Mark the message ID as processed in the deduplication store
         logger.info("Inside markMessageAsProcessed");
        deduplicationStore.put(messageId, true);
    }  
}
```


