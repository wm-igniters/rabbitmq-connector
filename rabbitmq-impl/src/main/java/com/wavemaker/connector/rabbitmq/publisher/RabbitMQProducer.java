package com.wavemaker.connector.rabbitmq.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

    @Service
    public class RabbitMQProducer {

        private static final Logger logger = LoggerFactory.getLogger(RabbitMQProducer.class);

        @Autowired
        private RabbitTemplate rabbitTemplate;

        private MessageConverter messageConverter = new SimpleMessageConverter();

        private String messageId = String.valueOf(Math.random());

        public void sendMessage(String exchangeName, String routingKey, String message, String messageId){
            logger.debug("exchangeName: {}, routingKey: {}", exchangeName, routingKey);
            logger.info(String.format("Message -> %s", message));
            rabbitTemplate.convertAndSend(exchangeName, routingKey, message, messagePostProcessor(messageId));
        }

        public void sendJsonMessage(String exchangeName, String routingKey, Object obj, String messageId){
            logger.debug("exchangeName: {}, routingKey: {}", exchangeName, routingKey);
            logger.info(String.format("Json message -> %s", obj.toString()));
            rabbitTemplate.convertAndSend(exchangeName, routingKey, obj, messagePostProcessor(messageId));
        }

        private MessagePostProcessor messagePostProcessor(String messageId){
            MessagePostProcessor messagePostProcessor = message -> {
                MessageProperties properties = message.getMessageProperties();
                properties.setMessageId(messageId);
                return message;
            };
            return messagePostProcessor;
        }
    }
