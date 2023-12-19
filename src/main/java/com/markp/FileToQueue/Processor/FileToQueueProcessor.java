package com.markp.FileToQueue.Processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.markp.FileToQueue.Model.FileActions;
import com.markp.FileToQueue.Template.FileToQueueTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileToQueueProcessor implements Processor {

    @Autowired
    FileToQueueTemplate fileToQueueTemplate;

    @Override
    public void process(Exchange exchange) throws Exception {
        String in = exchange.getIn().getBody(String.class);
        String filePath = exchange.getIn().getHeader("CamelFileParent", String.class);

        ObjectMapper mapper = new ObjectMapper();
        FileActions fileActions = mapper.readValue(in, FileActions.class);
        fileActions.setFilePath(filePath);

        boolean sentToQueue = fileToQueueTemplate.sendMessageTemplate(fileActions);
        exchange.setProperty("Success", sentToQueue);
    }
}
