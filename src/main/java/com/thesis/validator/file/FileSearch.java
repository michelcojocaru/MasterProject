package com.thesis.validator.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileSearch {

    private String fileName;
    private List<String> result = new ArrayList<>();
    private static FileSearch instance = null;

    // Exists only to defeat instantiation.
    private FileSearch() {}

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
        }
        return null;
    }

    private void search(File file) {
        if (file.isDirectory()) {
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
            }
        }
    }
}