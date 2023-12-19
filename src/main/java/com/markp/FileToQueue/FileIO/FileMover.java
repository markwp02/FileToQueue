package com.markp.FileToQueue.FileIO;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
public class FileMover {
    public static String moveToDestination(String fileName, String filePath, String destinationBasePath) {
        if(fileName == null || fileName.isEmpty()) {
            return "";
        }

        String destinationPath = destinationBasePath + File.separator + fileName;

        Path source = Paths.get(filePath);
        Path destination = Paths.get(destinationPath);

        try {
            Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info(fileName + " moved successfully: " + new File(destinationPath).isFile());
        return destinationPath;
    }
}
