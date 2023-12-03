package com.markp.FileToQueue.Template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.markp.FileToQueue.FileIO.FileMover;
import com.markp.FileToQueue.Model.FileActions;
import com.markp.FileToQueue.Model.MyMessage;
import com.markp.FileToQueue.Producer.MyMessageProducer;
import com.markp.FileToQueue.Validation.FileToQueueValidation;
import jakarta.jms.JMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class FileToQueueTemplate {

    @Value("${product-importer.inbox}")
    private String productImporterInbox;

    @Autowired
    MyMessageProducer myMessageProducer;
    
    public void sendMessageTemplate(FileActions fileActions) throws JMSException, JsonProcessingException {
        // check required files exist
        boolean hasRequiredFile = FileToQueueValidation.checkAllRequiredFileExist("");

        // if true
        if(hasRequiredFile) {
            // move files to output directory
            String imageImporterFilePath = FileMover.moveToDestination(fileActions.getProductImageFilename(), productImporterInbox);

            // add message to queue
            MyMessage message = MyMessage.builder()
                                .productName(fileActions.getProductName())
                                .productCategory(fileActions.getProductCategory())
                                .productDescription(fileActions.getProductDescription())
                                .productPrice(fileActions.getProductPrice())
                                .productStock(fileActions.getProductStock())
                                .productImageFilepath(imageImporterFilePath)
                                .build();

            myMessageProducer.sendMessage(message);
        }

    }
}
