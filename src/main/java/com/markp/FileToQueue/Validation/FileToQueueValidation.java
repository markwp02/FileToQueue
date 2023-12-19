package com.markp.FileToQueue.Validation;

import java.io.File;

public class FileToQueueValidation {

    public static boolean checkRequiredFileExist(String requiredFileName, String requiredFile)  {
        if(requiredFileName == null || requiredFileName.isEmpty()) {
            return true;
        }

        File file = new File(requiredFile);
        System.out.println(file.getAbsolutePath());
        System.out.println(file.isFile());
        System.out.println(file.exists());
        System.out.println(file.getPath());

        return file.isFile();
    }
}
