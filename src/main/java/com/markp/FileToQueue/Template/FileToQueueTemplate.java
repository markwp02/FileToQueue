package com.markp.FileToQueue.Template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.markp.FileToQueue.FileIO.FileMover;
import com.markp.FileToQueue.Model.FileActions;
import com.markp.FileToQueue.Model.ProductMessage;
import com.markp.FileToQueue.Producer.ProductMessageProducer;
import com.markp.FileToQueue.Validation.FileToQueueValidation;
import jakarta.jms.JMSException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class FileToQueueTemplate {

    @Value("${product-importer.inbox}")
    private String productImporterInbox;

    @Autowired
    ProductMessageProducer productMessageProducer;
    public boolean sendMessageTemplate(FileActions fileActions) throws JMSException, JsonProcessingException {

        boolean sentMessage = false;
        String imageFileName = fileActions.getProductImageFilename();
        String imageFile = fileActions.getFilePath() + File.separator + imageFileName;
        log.info("Image file required: " + imageFile);
        boolean hasRequiredFile = FileToQueueValidation.checkRequiredFileExist(imageFileName, imageFile);
        log.info("Has required file to proceed: " + hasRequiredFile);

        if(hasRequiredFile) {
            // move files to output directory
            String imageImporterFilePath = FileMover.moveToDestination(imageFileName, imageFile, productImporterInbox);

            // add message to queue
            ProductMessage message = ProductMessage.builder()
                                .productName(fileActions.getProductName())
                                .productCategory(fileActions.getProductCategory())
                                .productDescription(fileActions.getProductDescription())
                                .productPrice(fileActions.getProductPrice())
                                .productStock(fileActions.getProductStock())
                                .productImageFilename(fileActions.getProductImageFilename())
                                .productImageFilepath(imageImporterFilePath)
                                .build();

            productMessageProducer.sendMessage(message);
            sentMessage = true;
        }
        return sentMessage;
    }
}
