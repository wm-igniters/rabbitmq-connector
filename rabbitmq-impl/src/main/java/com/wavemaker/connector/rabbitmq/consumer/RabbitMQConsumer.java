package com.wavemaker.connector.rabbitmq.consumer;


import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
public class RabbitMQConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @Autowired
    private ConnectionFactory connectionFactory;

    private Connection connection;

    private Channel channel;

    public void consumeMessage(String queueName, Boolean flag, DeliverCallback deliverCallback, CancelCallback cancelCallback) throws IOException, TimeoutException {
        connection = connectionFactory.createConnection();
        channel = connection.createChannel(true);
        channel.basicConsume(queueName, flag, deliverCallback, cancelCallback);
    }

    @PreDestroy
    public void preDestroy() throws IOException, TimeoutException {
        if(channel != null){
            channel.close();
        }
        connection.close();
    }

    public void cancelConsume(String consumerTag) throws IOException{
        Connection connection = connectionFactory.createConnection();
        Channel channel = connection.createChannel(true);
        channel.basicCancel(consumerTag);
    }
}
