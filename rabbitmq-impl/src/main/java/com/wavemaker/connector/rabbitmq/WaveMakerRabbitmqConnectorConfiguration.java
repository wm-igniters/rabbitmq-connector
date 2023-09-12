package com.wavemaker.connector.rabbitmq;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.wavemaker.runtime.connector.configuration.ConnectorConfigurationBase;

@Configuration
@ComponentScan(basePackages = "com.wavemaker.connector.rabbitmq")
public class WaveMakerRabbitmqConnectorConfiguration extends ConnectorConfigurationBase {

     private static final Logger logger = LoggerFactory.getLogger(WaveMakerRabbitmqConnectorConfiguration.class);

    /**
     * Connector default properties files are load in super class.If you
     * have additional properties file, specify in this api and they will be loaded in super class.
     * @return
     */
    @Override
    public List<Resource> getClasspathPropertyResources() {
        return new ArrayList<>();
    }

}