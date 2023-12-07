package com.markp.FileToQueue.Consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.markp.FileToQueue.Model.MyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MyMessageConsumer {

    @Value("${queue-name}")
    private String queueName;

    @Autowired
    private JmsTemplate jmsTemplate;
    
    public MyMessage receiveMessage() {
        String jsonMessage = (String) jmsTemplate.receiveAndConvert(queueName);
        
        MyMessage sentMessage = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try { 
            sentMessage = objectMapper.readValue(jsonMessage, MyMessage.class);
        } catch (JsonProcessingException e) {
            System.out.println("JsonProcessing error" + e);
        } finally {
            return sentMessage;
        }
    }
}
