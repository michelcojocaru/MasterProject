package com.thesis.validator.file;

import com.thesis.validator.enums.Result;

import java.util.HashMap;
import java.util.List;

public class Response {

    public String errorMessage;
    public HashMap<String, HashMap<String,Double>> results;

    public Response(HashMap<String, HashMap<String,Double>> results) {
        this.results = results;
    }

    public Response(String error) {
        this.errorMessage = error;
    }
}
