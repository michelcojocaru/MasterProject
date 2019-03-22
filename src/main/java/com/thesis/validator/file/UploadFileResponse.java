package com.thesis.validator.file;

import com.thesis.validator.enums.Result;

public class UploadFileResponse {

    public Result granularity;
    public Result cohesion;
    public Result coupling;

    public UploadFileResponse(/*String fileName, String fileDownloadUri, String contentType, long size, ArrayList<String> results,String downloadResultURI,*/ Result granularity, Result cohesion, Result coupling ) {
        this.granularity = granularity;
        this.cohesion = cohesion;
        this.coupling = coupling;
    }
}
