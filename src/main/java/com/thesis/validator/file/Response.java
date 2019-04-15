package com.thesis.validator.file;


import com.thesis.validator.model.TestResult;

import java.util.HashMap;

public class Response {

    public String errorMessage;
    public HashMap<String, HashMap<String,TestResult>> results;

    public Response(HashMap<String, HashMap<String, TestResult>> results) {
        this.results = results;
    }

    public Response(String error) {
        this.errorMessage = error;
    }
}
