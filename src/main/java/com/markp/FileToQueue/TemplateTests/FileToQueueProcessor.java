package com.markp.FileToQueue.TemplateTests;

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
        ObjectMapper mapper = new ObjectMapper();
        FileActions fileActions = mapper.readValue(in, FileActions.class);

        fileToQueueTemplate.sendMessageTemplate(fileActions);
    }
}
