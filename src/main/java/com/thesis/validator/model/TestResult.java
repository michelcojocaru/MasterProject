package com.thesis.validator.model;

import com.thesis.validator.enums.Tests;

public class TestResult {

    private Tests name;
    private double score;
    private String cause;
    private String treatment;
    private String details;

    public TestResult(Tests name){
        this.name = name;
    }

    public TestResult(Tests name, double score){
        this.name = name;
        this.score = score;
    }

    public Tests getTestName() {
        return name;
    }

    public double getScore() {
        return score;
    }

    public String getCause() {
        return cause;
    }

    public String getTreatment() {
        return treatment;
    }

    public String getDetails() {
        return details;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void appendDetail(String detail){
        if(this.details == null){
            this.details = "";
        }
        this.details += detail;
    }

    public void setName(Tests name) {
        this.name = name;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
