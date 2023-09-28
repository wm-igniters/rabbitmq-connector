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

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
public class RabbitMQConsumer{

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @Autowired
    private ConnectionFactory connectionFactory;

    private Connection connection;

    private Channel channel;

    public String consumeMessage(String queueName, Boolean flag, DeliverCallback deliverCallback, CancelCallback cancelCallback) throws IOException, TimeoutException {
        connection = connectionFactory.createConnection();
        channel = connection.createChannel(true);
        return channel.basicConsume(queueName, flag, deliverCallback, cancelCallback);
    }

    public void cancelConsumer(String consumerTag) throws IOException, TimeoutException {
        if(channel != null && channel.isOpen()){
           channel.basicCancel(consumerTag);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        logger.info("inside finalize method");
        if(channel != null && channel.isOpen()){
            logger.info("channel.isOpen() "+channel.isOpen());
            channel.close();
            logger.info("after closing, checking channel.isOpen() "+channel.isOpen());
        }
        if(connection != null && connection.isOpen()){
            logger.info("connection.isOpen() "+connection.isOpen());
            connection.close();
            logger.info("after closing, checking connection.isOpen() "+connection.isOpen());
        }
    }
}
