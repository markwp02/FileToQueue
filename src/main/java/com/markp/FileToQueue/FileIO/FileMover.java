package com.markp.FileToQueue.FileIO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileMover {

    public static String moveToDestination(String filename, String destinationBasePath) {
        if(filename == null || filename.isEmpty()) {
            return "";
        }

        String destinationPath = destinationBasePath + File.separator + filename;

        Path source = Paths.get(filename);
        Path destination = Paths.get(destinationPath);

        try {
            Files.copy(source, destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(filename + " copied successfully: " + new File(destinationPath).isFile());
        return destinationPath;
    }
}
