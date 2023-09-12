package com.wavemaker.connector.rabbitmq;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.DeliverCallback;
import com.wavemaker.connector.rabbitmq.consumer.RabbitMQConsumer;
import com.wavemaker.connector.rabbitmq.publisher.RabbitMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import com.wavemaker.connector.rabbitmq.WaveMakerRabbitmqConnector;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
@Primary
public class WaveMakerRabbitmqConnectorImpl implements WaveMakerRabbitmqConnector{

    private static final Logger logger = LoggerFactory.getLogger(WaveMakerRabbitmqConnectorImpl.class);

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @Autowired
    private RabbitMQConsumer rabbitMQConsumer;

    @Override
    public void sendMessage(String exchangeName, String routingKey, String message, String messageId){
        rabbitMQProducer.sendMessage(exchangeName, routingKey, message, messageId);
    }

    @Override
    public void sendJsonMessage(String exchangeName, String routingKey, Object obj, String messageId) {
        rabbitMQProducer.sendJsonMessage(exchangeName, routingKey, obj, messageId);
    }

    @Override
    public void consumeMessage(String queueName, Boolean flag, DeliverCallback deliverCallback, CancelCallback cancelCallback) throws IOException, TimeoutException {
        rabbitMQConsumer.consumeMessage(queueName, flag, deliverCallback, cancelCallback);
    }

    @Override
    public void cancelConsume(String consumerTag) throws IOException {
        rabbitMQConsumer.cancelConsume(consumerTag);
    }

}