package com.markp.FileToQueue.Producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.markp.FileToQueue.Model.ProductMessage;
import jakarta.jms.JMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductMessageProducer {

    @Value("${queue-name}")
    private String queueName;

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(ProductMessage message) throws JsonProcessingException, JMSException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonMessage = objectMapper.writeValueAsString(message);
        jmsTemplate.convertAndSend(queueName, jsonMessage);
    }
}
