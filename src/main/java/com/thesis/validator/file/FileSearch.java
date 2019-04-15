package com.thesis.validator.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileSearch {

    private String fileName;
    private List<String> result = new ArrayList<>();
    private static FileSearch instance = null;

    private FileSearch() {
        // Exists only to defeat instantiation.
    }

    public static FileSearch getInstance() {
        if (instance == null) {
            instance = new FileSearch();
        }
        return instance;
    }

    private String getFileName() {
        return fileName;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<String> getResult() {
        return result;
    }

    public void clearResults(){
        this.result.clear();
    }

    public List<String> searchDirectory(File directory, String fileNameToSearch) {
        setFileName(fileNameToSearch);
        if (directory.isDirectory()) {
            search(directory);
            return result;
        } //else {
          //  System.out.println(directory.getAbsoluteFile() + " is not a directory!");
        //}
        return null;
    }

    private void search(File file) {
        if (file.isDirectory()) {
            //System.out.println("Searching directory ... " + file.getAbsoluteFile());
            //do you have permission to read this directory?
            if (file.canRead()) {
                for (File temp : Objects.requireNonNull(file.listFiles())) {
                    if (temp.isDirectory()) {
                        search(temp);
                    } else {
                        if (getFileName().equals(temp.getName())) {
                            result.add(temp.getAbsoluteFile().toString());
                        }
                    }
                }
            } //else {
                //System.out.println(file.getAbsoluteFile() + "Permission Denied");
            //}
        }
    }

}