package com.wavemaker.connector.rabbitmq;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.DeliverCallback;
import com.wavemaker.runtime.connector.annotation.WMConnector;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


@WMConnector(name = "Rabbitmq",
        description = "A simple connector Rabbitmq that can be used in WaveMaker application to send messages " +
                "to RabbitMQ and consume messages from RabbitMQ")
public interface WaveMakerRabbitmqConnector {
     /**
      * Api to send simple text message to rabbitmq
      * @param exchangeName contains the exchange name of rabbitmq
      * @param routingKey   contains the routing key of exchange
      * @param message      contains the text to send rabbitmq queue
      */
     void sendMessage(String exchangeName, String routingKey, String message, String messageId);

     /**
      * Api to send json message to rabbitmq
      * @param exchangeName contains the exchange name of rabbitmq
      * @param routingKey   contains the routing key of exchange
      * @param obj          contains the json to send rabbitmq queue
      */
     void sendJsonMessage(String exchangeName, String routingKey, Object obj, String messageId);

     /**
      * Api to consume the text/json from rabbitmq
      * @param queueName contains the queue name of rabbitmq
      * @param flag
      * @param deliverCallback contains the response body of message
      * @param cancelCallback
      * @throws IOException
      * @throws TimeoutException
      */
     void consumeMessage(String queueName, Boolean flag, DeliverCallback deliverCallback, CancelCallback cancelCallback) throws IOException, TimeoutException;

     /**
      * Api to cancel the consumer of rabbitmq
      * @param consumerTag contains the consumerTag of the consumer
      * @throws IOException
      */
     void cancelConsume(String consumerTag) throws IOException;
}