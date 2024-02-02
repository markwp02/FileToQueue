package com.markp.FileToQueue.Consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.markp.FileToQueue.Model.ProductMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductMessageConsumer {

    @Value("${queue-name}")
    private String queueName;

    @Autowired
    private JmsTemplate jmsTemplate;
    
    public ProductMessage receiveMessage() {
        String jsonMessage = (String) jmsTemplate.receiveAndConvert(queueName);
        
        ProductMessage sentMessage = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try { 
            sentMessage = objectMapper.readValue(jsonMessage, ProductMessage.class);
        } catch (JsonProcessingException e) {
            System.out.println("JsonProcessing error" + e);
        } finally {
            return sentMessage;
        }
    }
}
