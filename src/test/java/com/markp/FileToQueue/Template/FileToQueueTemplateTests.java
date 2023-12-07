package com.markp.FileToQueue.Template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.markp.FileToQueue.Consumer.MyMessageConsumer;
import com.markp.FileToQueue.Model.FileActions;
import com.markp.FileToQueue.Model.MyMessage;
import jakarta.jms.JMSException;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FileToQueueTemplateTests {

    @Autowired
    FileToQueueTemplate fileToQueueTemplate;

    @Autowired
    MyMessageConsumer myMessageConsumer;

    public void copyImageFile(String filename) throws IOException {
        String sourcePath = "src/test/resources/" + filename;
        Path source = Paths.get(sourcePath);
        Path destination = Paths.get(filename);

        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
    }

    public boolean deleteCopiedFile(String filename) {
        File file = new File(filename);
        boolean success = file.delete();
        if (success) {
            System.out.println("File deleted successfully " + filename);
        } else {
            System.out.println("Failed to delete file " + filename);
        }
        return success;
    }

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
                .build();

        System.out.println("About to send message");
        boolean sentMessageSuccess = fileToQueueTemplate.sendMessageTemplate(fileActions);
        assertTrue(sentMessageSuccess);

        // then
        // Message should have been sent to the queue

        MyMessage sentMessage = myMessageConsumer.receiveMessage();
        System.out.println("Received message: " + sentMessage);
        assertNotNull(sentMessage);

        // Image file should not have been moved to the product importer destination path
        String imageFinalDestination = sentMessage.getProductImageFilepath();
        boolean imageExistsAtDestination = new File(imageFinalDestination).isFile();
        assertFalse(imageExistsAtDestination);

    }

    @Test
    public void messageWithRequiredImageFile() throws JMSException, IOException {
        // given there is an image file to wait for
        // when

        String fileName = "airfryer.jpg";
        copyImageFile(fileName);

        FileActions fileActions = FileActions.builder()
                .productName("Air Fryer")
                .productCategory("Kitchen Appliances")
                .productDescription("Air Fryer that can create crispy food with 99% less oil")
                .productPrice(BigDecimal.valueOf(129))
                .productStock(30)
                .productImageFilename(fileName)
                .build();

        fileToQueueTemplate.sendMessageTemplate(fileActions);

        // then
        // Message should have been sent to the queue

        MyMessage sentMessage = myMessageConsumer.receiveMessage();

        assertNotNull(sentMessage);

        // Image file should not have been moved to the product importer destination path
        String imageFinalDestination = sentMessage.getProductImageFilepath();

        boolean imageExistsAtDestination = new File(imageFinalDestination).isFile();
        assertTrue(imageExistsAtDestination);

        assertTrue(deleteCopiedFile(fileName));

        assertTrue(deleteCopiedFile(imageFinalDestination));
    }
}
