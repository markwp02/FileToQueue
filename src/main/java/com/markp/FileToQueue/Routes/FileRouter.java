package com.markp.FileToQueue.Routes;

import com.markp.FileToQueue.TemplateTests.FileToQueueProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileRouter extends RouteBuilder {

    @Value("${source.folder}")
    private String sourceFolder;

    @Value("${source.suffix}")
    private String sourceSuffix;

    @Value("${destination.folder}")
    private String destinationFolder;

    @Autowired
    private FileToQueueProcessor fileToQueueProcessor;

    @Override
    public void configure() throws Exception {

        from("file://" + sourceFolder + "?include=" + sourceSuffix)
                .id("FILE_ROUTE")
                .log("Received file: ${header.CamelFileName}")
                ///.unmarshal().json(JsonLibrary.Jackson, FileActions.class)
                .process(fileToQueueProcessor)
                .to("file://" + destinationFolder)
                .end();
    }
}
