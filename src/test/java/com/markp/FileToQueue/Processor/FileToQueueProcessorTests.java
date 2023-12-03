package com.markp.FileToQueue.Processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.markp.FileToQueue.FileIO.FileMover;
import com.markp.FileToQueue.Model.FileActions;
import com.markp.FileToQueue.Model.MyMessage;
import com.markp.FileToQueue.Template.FileToQueueTemplate;
import com.markp.FileToQueue.Validation.FileToQueueValidation;
import jakarta.jms.JMSException;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;

import java.io.File;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FileToQueueProcessorTests {

    @Autowired
    FileToQueueTemplate fileToQueueTemplate;

    @Value("${product-importer.inbox}")
    private String productImporterInbox;

    @Value("${queue-name}")
    private String queueName;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Test
    public void messageWithNoRequiredImageFile() throws JMSException, JsonProcessingException {
        // given there is no image file to wait for
        // when

        FileActions fileActions = FileActions.builder()
                .productName("Air Fryer")
                .productCategory("Kitchen Appliances")
                .productDescription("Air Fryer that can create crispy food with 99% less oil")
                .productPrice(BigDecimal.valueOf(129))
                .productStock(30)
                .productImageFilename("")
                .build();

        fileToQueueTemplate.sendMessageTemplate(fileActions);

        // then
        // Message should have been sent to the queue

        String jsonMessage = (String) jmsTemplate.receiveAndConvert(queueName);

        ObjectMapper objectMapper = new ObjectMapper();
        MyMessage sentMessage = objectMapper.readValue(jsonMessage, MyMessage.class);
        assertTrue(sentMessage != null);

        // Image file should not have been moved to the product importer destination path
        String imageFinalDestination = sentMessage.getProductImageFilepath();
        boolean imageExistsAtDestination = new File(imageFinalDestination).isFile();
        assertFalse(imageExistsAtDestination);

    }
}
