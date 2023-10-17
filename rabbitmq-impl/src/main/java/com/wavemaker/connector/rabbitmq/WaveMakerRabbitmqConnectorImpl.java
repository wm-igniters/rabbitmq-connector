package com.wavemaker.connector.rabbitmq;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.DeliverCallback;
import com.wavemaker.connector.rabbitmq.consumer.RabbitMQConsumer;
import com.wavemaker.connector.rabbitmq.publisher.RabbitMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.core.MessageListener;
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
    public void sendMessage(String exchangeName, String routingKey, String message){
        rabbitMQProducer.sendMessage(exchangeName, routingKey, message);
    }

    @Override
    public void sendJsonMessage(String exchangeName, String routingKey, Object obj) {
        rabbitMQProducer.sendJsonMessage(exchangeName, routingKey, obj);
    }

    @Override
    public String consumeMessage(String queueName, Boolean flag, DeliverCallback deliverCallback, CancelCallback cancelCallback) throws IOException, TimeoutException {
       return rabbitMQConsumer.consumeMessage(queueName, flag, deliverCallback, cancelCallback);
    }

    @Override
    public void cancelConsumer(String consumerTag)  {
        rabbitMQConsumer.cancelConsumer(consumerTag);
    }
}