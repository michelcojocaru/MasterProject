package com.thesis.validator.file;

import com.thesis.validator.enums.Result;

import java.util.List;

public class Response {

    public Result granularity;
    public Result cohesion;
    public Result coupling;

    public String errorMessage;

    public List<Result> results;

    public Response(List<Result> results) {
        this.results = results;
    }

    public Response(Result granularity, Result cohesion, Result coupling) {
        this.granularity = granularity;
        this.cohesion = cohesion;
        this.coupling = coupling;
    }

    public Response(String error) {
        this.errorMessage = error;
    }
}
