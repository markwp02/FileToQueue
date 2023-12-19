package com.markp.FileToQueue.Routes;

import com.markp.FileToQueue.Processor.FileToQueueProcessor;
import org.apache.camel.LoggingLevel;
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
    @Value("${await.folder}")
    private String awaitFolder;
    @Value("${error.folder}")
    private String errorFolder;
    @Autowired
    private FileToQueueProcessor fileToQueueProcessor;

    @Override
    public void configure() throws Exception {

        onException(Exception.class)
                .handled(true)
                .log(LoggingLevel.ERROR, "Error occurred: ${exception.message}\n${exception.stacktrace}")
                .to("file://" + errorFolder);

        from("file://" + sourceFolder + "?include=" + sourceSuffix)
            .id("FILE_ROUTE")
            .log("Received file: ${header.CamelFileName}")
            .process(fileToQueueProcessor)
            .log("Sent message to queue? ${header.Success}")
            .choice()
               .when(exchangeProperty("Success").isEqualTo(true))
                    .to("file://" + destinationFolder)
               .otherwise()
                    .log("Waiting for image file to proceed")
                    .to("file://" + awaitFolder)
           .end();
    }
}
