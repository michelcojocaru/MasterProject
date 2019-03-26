package com.thesis.validator.file;

import com.thesis.validator.enums.Result;

import java.util.HashMap;

public class Response {

    public String errorMessage;
    public HashMap<String, Result> results;

    public Response(HashMap<String, Result> results) {
        this.results = results;
    }

    public Response(String error) {
        this.errorMessage = error;
    }
}
