package com.markp.FileToQueue.Validation;

import java.io.File;

public class FileToQueueValidation {

    public static boolean checkRequiredFileExist(String requiredFile)  {
        if(requiredFile == null || requiredFile.isEmpty()) {
            return true;
        }

        File file = new File(requiredFile);
        return file.isFile();
    }
}
