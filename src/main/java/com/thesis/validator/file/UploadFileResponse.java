package com.thesis.validator.file;

import java.util.ArrayList;

public class UploadFileResponse {
//    private String fileName;
//    private String fileDownloadUri;
//    private String fileType;
//    private long size;
    private ArrayList<String> results;

    public UploadFileResponse(/*String fileName, String fileDownloadUri, String contentType, long size,*/ ArrayList<String> results) {
//        this.fileName = fileName;
//        this.fileDownloadUri = fileDownloadUri;
//        this.fileType = contentType;
//        this.size = size;
        this.results = results;
    }

    public ArrayList<String> getResults() {
        return results;
    }

    public void setResults(ArrayList<String> results) {
        this.results = results;
    }

//    public String getFileName() {
//        return fileName;
//    }
//
//    public void setFileName(String fileName) {
//        this.fileName = fileName;
//    }
//
//    public String getFileDownloadUri() {
//        return fileDownloadUri;
//    }
//
//    public void setFileDownloadUri(String fileDownloadUri) {
//        this.fileDownloadUri = fileDownloadUri;
//    }
//
//    public String getFileType() {
//        return fileType;
//    }
//
//    public void setFileType(String fileType) {
//        this.fileType = fileType;
//    }
//
//    public long getSize() {
//        return size;
//    }
//
//    public void setSize(long size) {
//        this.size = size;
//    }
}
