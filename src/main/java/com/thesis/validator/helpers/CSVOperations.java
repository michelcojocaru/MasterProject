package com.thesis.validator.helpers;

import java.io.*;
import java.nio.file.Paths;


public class CSVOperations {

    private static String inputAppName;
    private static String inputAppVersion;
    private static String path;
    private static String fileName;

    private static void createFileIfNotExist(String fileName){
        File file = new File(fileName);
        try {
            file.createNewFile(); // if file already exists will do nothing
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String initRunningTimesLogFile(String systemName, String purpose, String extension){
        String[] tokens = systemName.split("_");
        inputAppName = tokens[0];
        inputAppVersion = tokens[1];
        path  = Paths.get("test-files").toAbsolutePath().toString() + "/cuts-analysis/" + inputAppName;
        fileName = path + "/" + inputAppName + "_" + inputAppVersion + "_" + purpose + "." + extension;
        createFileIfNotExist(fileName);

        return fileName;
    }

    public static void logRunTimes(String systemName, String[] data){
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(fileWriter != null){

            try(PrintWriter writer = new PrintWriter(fileWriter)){
                StringBuilder sb = new StringBuilder();
                for(String item:data){
                    sb.append(item + (item.equals("\n") ? "":","));
                }
                writer.write(sb.toString());
            }
        }
    }
}

